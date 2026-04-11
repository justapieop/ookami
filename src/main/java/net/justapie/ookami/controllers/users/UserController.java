package net.justapie.ookami.controllers.users;

import net.justapie.ookami.annotations.VerifyWebhookSignature;
import net.justapie.ookami.controllers.users.webhook.WebhookDto;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.services.UserService;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @VerifyWebhookSignature
    @PostMapping
    public User createUser(@RequestBody WebhookDto dto) {
        return this.service.createUser(dto);
    }

    @GetMapping
    public User getUser(GetUserRequestParam param) {
        try {
            return param.getId() != null ? this.service.getUserById(UUID.fromString(param.getId())) : this.service.getUserByUsername(param.getUsername());
        } catch (NoSuchElementException e) {
            throw new ErrorResponseException(HttpStatusCode.valueOf(404));
        }
    }
}
