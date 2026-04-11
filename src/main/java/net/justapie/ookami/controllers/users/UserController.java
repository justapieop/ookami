package net.justapie.ookami.controllers.users;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.justapie.ookami.annotations.AuthenticatedUser;
import net.justapie.ookami.annotations.VerifyAccessToken;
import net.justapie.ookami.annotations.VerifyWebhookSignature;
import net.justapie.ookami.controllers.users.dto.GetUserRequestParam;
import net.justapie.ookami.controllers.users.dto.UpdateUsernameDto;
import net.justapie.ookami.controllers.users.webhook.WebhookDto;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.services.UserService;
import net.justapie.ookami.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@Tag(name = "Users")
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
        return this.service.saveUser(dto.getPayload().getUser());
    }

    @GetMapping
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
                    @ApiResponse(responseCode = "200", description = "User found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    })
            }
    )
    public User getUser(GetUserRequestParam param) {
        try {
            return param.getId() != null ? this.service.getUserById(UUID.fromString(param.getId())) : this.service.getUserByUsername(param.getUsername());
        } catch (NoSuchElementException e) {
            throw new ErrorResponseException(HttpStatus.NOT_FOUND, e.getCause());
        }
    }

    @SecurityRequirement(name = Constants.BEARER_AUTH_KEY)
    @VerifyAccessToken
    @PatchMapping
    public void updateUsername(@RequestBody UpdateUsernameDto dto, @AuthenticatedUser User user) {
        this.service.updateUsername(user.getId(), dto.getUsername());
    }

    @SecurityRequirement(name = Constants.BEARER_AUTH_KEY)
    @VerifyAccessToken
    @GetMapping("/me")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "401", description = "Unauthenticated", content = @Content),
                    @ApiResponse(responseCode = "200", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    })
            }
    )
    public User getMe(@AuthenticatedUser User user) {
        return user;
    }
}
