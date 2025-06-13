package com.example.bookrentalshop.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static UserEntity newInstance(String email, String password, String name) {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserEntity user)) return false;
        return Objects.equals(id, user.id)
               && Objects.equals(email, user.email)
               && Objects.equals(password, user.password)
               && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name);
    }
}
