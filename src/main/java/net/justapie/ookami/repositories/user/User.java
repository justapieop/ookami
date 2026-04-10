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

    @Column(unique = true)
    private String username;
}
