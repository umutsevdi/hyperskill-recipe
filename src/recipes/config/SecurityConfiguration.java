package recipes.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import recipes.service.UserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    UserService userDetailsService;
    RecipeAuthenticationProvider recipeAuthenticationProvider;
    PasswordEncoderService passwordEncoderService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoderService.passwordEncoder())
                .and().authenticationProvider(recipeAuthenticationProvider);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().csrf().disable().headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/actuator/shutdown", "/h2", "/h2/**", "/api/register").anonymous()
                .anyRequest().authenticated();
    }
}
