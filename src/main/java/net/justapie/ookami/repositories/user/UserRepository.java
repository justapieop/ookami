package net.justapie.ookami.repositories.user;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    Optional<User> findUserById(UUID id);

    Optional<User> findUserByUsername(String username);

    @Transactional
    User save(User user);
}
