package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeDTO {
    @JsonIgnore
    private Long id;
    private String name;
    private String description;
    private String category;
    private List<String> ingredients;
    private List<String> directions;
    private LocalDateTime date;
    @JsonIgnore
    private Long authorId;

}
