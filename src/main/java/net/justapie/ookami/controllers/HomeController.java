package net.justapie.ookami.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController("/")
public class HomeController {
    public record HomeResponse(String status) implements Serializable {
    }

    @GetMapping("/")
    private HomeResponse getHome() {
        return new HomeResponse("ok");
    }
}