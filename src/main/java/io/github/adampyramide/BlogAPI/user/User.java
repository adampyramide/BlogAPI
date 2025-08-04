package io.github.adampyramide.BlogAPI.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 320)
    private String email;

    @Column
    private String avatarId;

    @Column(length = 20)
    private String displayName;

    @Column(length = 500)
    private String description;

    @Column
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNSPECIFIED;

    @PrePersist
    public void prePersist() {
        preChange();

        if (gender == null) gender = Gender.UNSPECIFIED;
    }

    @PreUpdate
    public void preUpdate() {
        preChange();
    }

    private void preChange() {
        if (email != null) email = email.toLowerCase();
    }

}
