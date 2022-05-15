package recipes.repository.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table
@Entity(name = "recipe")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String category;
    @Column
    private String ingredients;
    @Column
    private String directions;
    @Column
    private LocalDateTime date;
    @Column(nullable = false)
    private Long authorId;

    public Recipe(String name, String description, String category, String ingredients, String directions, LocalDateTime date, Long authorId) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.ingredients = ingredients;
        this.directions = directions;
        this.date = date;
        this.authorId = authorId;
    }
}
