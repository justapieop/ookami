package net.justapie.ookami.services;

import net.justapie.ookami.controllers.users.webhook.WebhookDto;
import net.justapie.ookami.repositories.user.User;
import net.justapie.ookami.repositories.user.UserRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.UUID;

@CacheConfig({"users"})
@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Caching(
            put = {
                    @CachePut(key = "'id:' + #result.id", unless = "#result == null"),
            }
    )
    public User getUserById(UUID id) {
        return this.repository.findUserById(id).orElseThrow();
    }

    @Caching(
            put = {
                    @CachePut(key = "'username:' + #result.id", unless = "#result == null"),
            }
    )
    public User getUserByUsername(String username) {
        return this.repository.findUserByUsername(username).orElseThrow();
    }

    @Caching(
            put = {
                    @CachePut(key = "'id:' + #result.id", unless = "#result == null"),
                    @CachePut(key = "'username:' + #result.username", unless = "#result == null")
            }
    )
    public User createUser(WebhookDto dto) {
        final User user = User.builder()
                .id(dto.getPayload().getUser().getId())
                .username(dto.getPayload().getUser().getId().toString())
                .displayName(dto.getPayload().getUser().getStandardAttributes().getName())
                .email(dto.getPayload().getUser().getStandardAttributes().getEmail())
                .avatarUrl(dto.getPayload().getUser().getStandardAttributes().getPicture())
                .build();
        return this.repository.save(user);
    }
}
