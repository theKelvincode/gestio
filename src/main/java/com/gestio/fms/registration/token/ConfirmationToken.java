package com.gestio.fms.registration.token;

import com.gestio.fms.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )
    private Long id;

     @Column(nullable = false)
     private String token;

     @Column(nullable = false)
     private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    // let us tie this token to a user
    @ManyToOne
    // because an application user can have many confirmation tokens
    @JoinColumn(
            nullable = false,
            name = "gestio_user_id")
    private User user;

    public ConfirmationToken(String token, LocalDateTime createdAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public User getUser() {
        return user;
    }
}

