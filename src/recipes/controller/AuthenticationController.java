package recipes.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import recipes.controller.model.AuthenticationRequest;
import recipes.service.UserService;

@RestController
@RequestMapping("api/")
@AllArgsConstructor
@Log4j2
public class AuthenticationController {
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody AuthenticationRequest request) {
        log.info("register:" + request);
        try {
            Long id = userService.register(request);
            log.info("register:complete:" + id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
