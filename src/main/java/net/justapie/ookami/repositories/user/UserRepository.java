package net.justapie.ookami.repositories.user;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

@CacheConfig("users")
public interface UserRepository extends Repository<User, UUID> {
    @CachePut(value = "id", key = "#id", unless = "#result == null")
    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") UUID id);

    @CachePut(value = "username", key = "#username", unless = "#result == null")
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);

    @Caching(
            cacheable = {
                    @Cacheable(value = "id", key = "#{input.id}", unless = "#result == null"),
                    @Cacheable(value = "username", key = "#{input.username}", unless = "#result == null")
            }
    )
    @Modifying
    @Query("INSERT INTO User (id, email, username, displayName, avatarUrl) VALUES (#{input.id}, #{input.email}, #{input.username}, #{input.displayName}, #{input.avatarUrl})")
    User createUser(@Param("input") UserCreateInput input);
}
