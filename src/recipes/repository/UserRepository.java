package recipes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import recipes.repository.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long aLong);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    User save(User entity);
}
