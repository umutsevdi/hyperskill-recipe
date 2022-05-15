package recipes.config;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import recipes.model.UserDTO;
import recipes.service.UserService;

@Configuration
@AllArgsConstructor
@Log4j2
public class RecipeAuthenticationProvider implements AuthenticationProvider {
    UserService userService;
    PasswordEncoderService passwordEncoderService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("authentication: {" + authentication.getName() + ", " + authentication.getCredentials().toString() + "}");
        UserDTO user = userService.findByEmail(authentication.getName());
        if (passwordEncoderService.passwordEncoder()
                .matches(authentication.getCredentials().toString(), user.getPassword())) {
            log.info("authentication: confirmed");
            return new RecipeAuthentication(authentication.getName(), authentication.getCredentials().toString(), true);
        }
        log.info("authentication: failed");
        throw new AuthenticationCredentialsNotFoundException("CredentialIsNotFoundException");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
