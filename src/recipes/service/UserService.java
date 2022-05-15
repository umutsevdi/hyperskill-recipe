package recipes.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import recipes.controller.model.AuthenticationRequest;
import recipes.exception.NoSuchUserException;
import recipes.mapper.UserMapper;
import recipes.model.UserDTO;
import recipes.repository.UserRepository;
import recipes.repository.entity.User;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return findByEmail(email);
        } catch (NoSuchUserException e) {
            throw new NoSuchUserException();
        }
    }

    public UserDTO findByEmail(String email) throws NoSuchUserException {
        if (userRepository.existsByEmail(email)) {
            return userMapper.mapToDTO(userRepository.findByEmail(email).get());
        }
        throw new NoSuchUserException();
    }

    public Long register(AuthenticationRequest request) throws IllegalArgumentException {
        if (request.getEmail() != null && request.getPassword() != null &&
                request.getPassword().length() >= 8 && !request.getPassword().isBlank() &&
                request.getEmail().matches("\\w+@\\w+\\.\\w+")) {
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            return userRepository.save(new User(request.getEmail(), request.getPassword())).getId();
        }
        throw new IllegalArgumentException();
    }

}
