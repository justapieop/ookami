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
    @Query("UPDATE User u SET " +
            "u.displayName = COALESCE(:#{#user.displayName}, u.displayName), " +
            "u.avatarUrl = COALESCE(:#{#user.avatarUrl}, u.avatarUrl), " +
            "u.updatedAt = :#{#user.updatedAt} " +
            "WHERE u.email = :#{#user.email}")
    void updateUser(User user);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.username = :username WHERE u.id = :id")
    void updateUsername(@Param("id") UUID id, @Param("username") String username);
}
