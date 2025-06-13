package com.example.bookrentalshop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "token")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    private String accessToken;

    private String refreshToken;

    @CreatedDate
    private LocalDateTime createdAt;

    public static TokenEntity newInstance(UserEntity user, String accessToken, String refreshToken) {
        return TokenEntity.builder()
                .user(user)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TokenEntity token)) return false;
        return Objects.equals(id, token.id)
               && Objects.equals(user, token.user)
               && Objects.equals(accessToken, token.accessToken)
               && Objects.equals(refreshToken, token.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, accessToken, refreshToken);
    }
}
