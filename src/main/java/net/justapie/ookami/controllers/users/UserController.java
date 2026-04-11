package net.justapie.ookami.controllers.users;

import net.justapie.ookami.annotations.VerifyWebhookSignature;
import net.justapie.ookami.controllers.users.webhook.WebhookDto;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @VerifyWebhookSignature
    @PostMapping
    public User createUser(@RequestBody WebhookDto dto) {
        return this.service.createUser(dto);
    }
}
