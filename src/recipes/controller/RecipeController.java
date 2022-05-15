package recipes.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import recipes.exception.NoSuchRecipeException;
import recipes.exception.NoSuchUserException;
import recipes.exception.UnauthorizedException;
import recipes.model.RecipeDTO;
import recipes.model.UserDTO;
import recipes.service.RecipeService;
import recipes.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
@AllArgsConstructor
@Log4j2
public class RecipeController {
    RecipeService recipeService;
    UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDTO>> searchRecipe(
            @Param("category") Optional<String> category,
            @Param("name") Optional<String> name) {
        log.info("searchRecipe:{category:" + category + ", name:" + name + "}");

        if (category.isPresent() && name.isEmpty()) {
            return new ResponseEntity<>(
                    recipeService.search(RecipeService.searchParam.CATEGORY, category.get()),
                    HttpStatus.OK);
        } else if (name.isPresent() && category.isEmpty()) {
            return new ResponseEntity<>(
                    recipeService.search(RecipeService.searchParam.NAME, name.get()),
                    HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipe(@PathVariable("id") Long id) {
        log.info("getRecipe:" + id);
        try {
            RecipeDTO response = recipeService.getRecipe(id);
            log.info("=>found:" + response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (NoSuchRecipeException e) {
            log.info("=>not_found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Map<String, Long>> saveRecipe(@AuthenticationPrincipal UserDTO authentication, @RequestBody RecipeDTO recipe) {
        log.info("postRecipe:" + recipe);
        try {
            UserDTO user = userService.findByEmail(authentication.getEmail());
            recipe.setDate(LocalDateTime.now());
            RecipeDTO recipeDTO = recipeService.saveRecipe(recipe, user.getId());
            log.info("postRecipe: success =>" + recipeDTO);
            return new ResponseEntity<>(Map.of("id", recipeDTO.getId()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.valueOf(400));
        } catch (NoSuchUserException | UnauthorizedException e) {
            log.warn(e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipe(@AuthenticationPrincipal UserDTO authentication, @PathVariable("id") Long id, @RequestBody RecipeDTO recipe) {
        log.info("updateRecipe:" + id + ":" + recipe);
        try {
            UserDTO user = userService.findByEmail(authentication.getEmail());
            recipe.setDate(LocalDateTime.now());
            RecipeDTO recipeDTO = recipeService.updateRecipe(recipe, id, user.getId());
            log.info("updateRecipe: success =>" + recipeDTO);
        } catch (NoSuchRecipeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@AuthenticationPrincipal UserDTO authentication, @PathVariable("id") Long id) {
        log.info("deleteRecipe:" + id);
        UserDTO user = userService.findByEmail(authentication.getEmail());
        try {
            RecipeDTO recipeDTO = recipeService.getRecipe(id);
            if (recipeDTO.getAuthorId().equals(user.getId())) {
                log.info("=>found+deleted");
                recipeService.deleteRecipe(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NoSuchRecipeException e) {
            log.info("=>not_found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
