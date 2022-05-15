package recipes.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.exception.NoSuchRecipeException;
import recipes.exception.UnauthorizedException;
import recipes.mapper.RecipeMapper;
import recipes.model.RecipeDTO;
import recipes.repository.RecipeRepository;
import recipes.repository.entity.Recipe;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeMapper recipeMapper;

    public enum searchParam {
        CATEGORY(), NAME()
    }

    public List<RecipeDTO> search(searchParam type, String arg) {
        if (type.equals(searchParam.CATEGORY)) {
            return recipeMapper.mapToDTOList(
                    recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(arg));
        }
        return recipeMapper.mapToDTOList(
                recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(arg));
    }

    public RecipeDTO getRecipe(Long id) throws NoSuchRecipeException {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return recipeMapper.map2DTO(recipe.get());
        } else {
            throw new NoSuchRecipeException();
        }
    }

    public RecipeDTO saveRecipe(RecipeDTO recipe, Long author) throws IllegalArgumentException, UnauthorizedException {
        if (recipe.getName() == null || recipe.getName().isBlank() ||
                recipe.getDescription() == null || recipe.getDescription().isBlank() ||
                recipe.getCategory() == null || recipe.getCategory().isBlank() ||
                recipe.getIngredients() == null || recipe.getIngredients().isEmpty() ||
                recipe.getDirections() == null || recipe.getDirections().isEmpty()) {
            throw new IllegalArgumentException();
        }
        recipe.setAuthorId(author);
        return recipeMapper.map2DTO(
                recipeRepository.save(recipeMapper.mapToDb(recipe))
        );
    }

    public RecipeDTO updateRecipe(RecipeDTO recipe, Long id, Long author) throws IllegalArgumentException, NoSuchRecipeException, UnauthorizedException {
        if (recipe.getName() == null || recipe.getName().isBlank() ||
                recipe.getDescription() == null || recipe.getDescription().isBlank() ||
                recipe.getCategory() == null || recipe.getCategory().isBlank() ||
                recipe.getIngredients() == null || recipe.getIngredients().isEmpty() ||
                recipe.getDirections() == null || recipe.getDirections().isEmpty()) {
            throw new IllegalArgumentException();
        } else if (!recipeRepository.existsById(id)) {
            throw new NoSuchRecipeException();
        }
        Long oldRecipeRepositoryAuthorId = recipeRepository.findById(id).get().getAuthorId();
        if (!oldRecipeRepositoryAuthorId.equals(author)) {
            throw new UnauthorizedException();
        }
        Recipe recipeDB = recipeMapper.mapToDb(recipe);
        recipeDB.setId(id);
        recipeDB.setAuthorId(oldRecipeRepositoryAuthorId);
        return recipeMapper.map2DTO(recipeRepository.save(recipeDB));
    }

    public boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isAuthorized(RecipeDTO recipeDTO, Long author) {
        return recipeDTO.getAuthorId().equals(author);
    }
}
