package net.justapie.ookami.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController("/")
public class Home {
    @GetMapping("/")
    private HomeResponse getHome() {
        return new HomeResponse("ok", "1.0");
    }
}

record HomeResponse(String status, String version) implements Serializable {
}