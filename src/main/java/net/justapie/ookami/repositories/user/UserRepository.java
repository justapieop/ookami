package net.justapie.ookami.repositories.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(@Param("id") UUID id);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);
}
