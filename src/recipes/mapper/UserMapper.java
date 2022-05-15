package recipes.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import recipes.model.UserDTO;
import recipes.repository.entity.User;


@Component
@AllArgsConstructor
public class UserMapper {

    public UserDTO mapToDTO(User db) {
        return new UserDTO(
                db.getId(),
                db.getEmail(),
                db.getPassword()
        );
    }

    public User mapToDB(UserDTO dto) {
        return new User(
                dto.getEmail(),
                dto.getPassword()
        );
    }
}
