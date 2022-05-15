package recipes.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import recipes.model.RecipeDTO;
import recipes.repository.entity.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class RecipeMapper {
    /*
     *   public List<Recipe> mapToDbList(List<RecipeDTO> dtoList) {
     *       List<Recipe> dbList = new ArrayList<>();
     *       dtoList.forEach(i -> dbList.add(mapToDb(i)));
     *       return dbList;
     *   }
     */
    public List<RecipeDTO> mapToDTOList(List<Recipe> dbList) {
        List<RecipeDTO> dtoList = new ArrayList<>();
        dbList.forEach(i -> dtoList.add(map2DTO(i)));
        return dtoList;
    }

    public Recipe mapToDb(RecipeDTO dto) {
        StringBuilder ingredientsData = new StringBuilder();

        dto.getIngredients().forEach(i -> ingredientsData.append(i).append("<ne>"));
        StringBuilder directionsData = new StringBuilder();
        dto.getDirections().forEach(i -> directionsData.append(i).append("<ne>"));

        return new Recipe(
                dto.getName(),
                dto.getDescription(),
                dto.getCategory(),
                ingredientsData.toString(),
                directionsData.toString(),
                dto.getDate(),
                dto.getAuthorId()
        );
    }

    public RecipeDTO map2DTO(Recipe db) {
        return new RecipeDTO(
                db.getId(),
                db.getName(),
                db.getDescription(),
                db.getCategory(),
                Arrays.asList(db.getIngredients().split("<ne>")),
                Arrays.asList(db.getDirections().split("<ne>")),
                db.getDate(),
                db.getAuthorId()
        );
    }
}
