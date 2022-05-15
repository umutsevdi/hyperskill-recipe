package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import recipes.repository.entity.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository
        extends JpaRepository<Recipe, Long> {
    @Override
    boolean existsById(Long id);

    @Override
    void deleteById(Long id);

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);

}
