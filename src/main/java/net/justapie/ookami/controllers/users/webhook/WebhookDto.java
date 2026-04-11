package net.justapie.ookami.controllers.users.webhook;

import lombok.Getter;

@Getter
public class WebhookDto {
    private String type;
    private WebhookPayload payload;
}
