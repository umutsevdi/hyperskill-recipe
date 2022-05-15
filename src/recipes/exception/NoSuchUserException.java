package recipes.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class NoSuchUserException extends UsernameNotFoundException {
    public NoSuchUserException() {
        super("NoSuchUserException");
    }
}
