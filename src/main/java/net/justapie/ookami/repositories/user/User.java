package net.justapie.ookami.repositories.user;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(columnList = "id", unique = true),
                @Index(columnList = "username", unique = true)
        })
public class User implements Serializable {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "display_name")
    private String displayName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(nullable = false)
    private boolean suspended = false;
}
