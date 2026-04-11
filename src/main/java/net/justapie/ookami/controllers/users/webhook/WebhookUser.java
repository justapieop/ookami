package net.justapie.ookami.controllers.users.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class WebhookUser {
    private UUID id;

    @JsonProperty("standard_attributes")
    private StandardAttributes standardAttributes;
}
