package net.justapie.ookami.services;

import net.justapie.ookami.controllers.users.webhook.WebhookUser;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.repositories.user.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {
    private static final String ID_CACHE_KEY = "users:id";
    private static final String USERNAME_CACHE_KEY = "users:username";

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = USERNAME_CACHE_KEY, key = "#username"),
                    @CacheEvict(value = ID_CACHE_KEY, key = "#id")
            }
    )
    public void updateUsername(UUID id, String username) {
        this.repository.updateUsername(id, username);
    }

    @Caching(
            put = {
                    @CachePut(value = ID_CACHE_KEY, key = "#id", unless = "#result == null"),
            }
    )
    public User getUserById(UUID id) {
        return this.repository.findUserById(id).orElseThrow();
    }

    @Caching(
            put = {
                    @CachePut(value = USERNAME_CACHE_KEY, key = "#username", unless = "#result == null"),
            }
    )
    public User getUserByUsername(String username) {
        return this.repository.findUserByUsername(username).orElseThrow();
    }

    @Caching(put = {
            @CachePut(value = ID_CACHE_KEY, key = "#result.id", unless = "#result == null"),
            @CachePut(value = USERNAME_CACHE_KEY, key = "#result.username", unless = "#result == null && #result.onboarded == false")
    })
    public User saveUser(WebhookUser whUser) {
        final User user = User.builder()
                .id(whUser.getId())
                .username(whUser.getId().toString())
                .displayName(whUser.getStandardAttributes().getName())
                .email(whUser.getStandardAttributes().getEmail())
                .avatarUrl(whUser.getStandardAttributes().getPicture())
                .suspended(false)
                .onboarded(false)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        try {
            return this.repository.save(user);
        } catch (Exception e) {
            this.repository.mergeUser(user);
            return this.repository.findUserById(user.getId()).orElseThrow();
        }
    }


}
