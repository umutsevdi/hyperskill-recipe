package recipes.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import recipes.model.UserDTO;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Data
public class RecipeAuthentication implements Authentication {
    private String username;
    private String password;
    private boolean authenticated;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Object getCredentials() {
        return getPassword();
    }

    @Override
    public Object getDetails() {
        return new UserDTO(username, password);
    }

    @Override
    public Object getPrincipal() {
        return new UserDTO(username, password);
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}
