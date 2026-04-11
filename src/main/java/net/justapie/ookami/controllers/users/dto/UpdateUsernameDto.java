package net.justapie.ookami.controllers.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUsernameDto {
    @NotBlank
    @Size(min = 1, max = 255)
    private String username;
}
