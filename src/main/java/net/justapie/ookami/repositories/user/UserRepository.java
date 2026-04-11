package net.justapie.ookami.repositories.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    Optional<User> findUserById(UUID id);

    Optional<User> findUserByUsername(String username);

    @Transactional
    User save(User user);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.email = :#{#user.email}, u.avatarUrl = :#{#user.avatarUrl}, u.createdAt = CURRENT_TIMESTAMP, u.onboarded = true WHERE u.id = :#{#user.id}")
    void mergeUser(@Param("user") User user);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :username, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :id")
    void updateUsername(@Param("id") UUID id, @Param("username") String username);
}
