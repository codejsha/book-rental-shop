package com.example.bookrentalshop.domain.entity;

import com.example.bookrentalshop.domain.constant.UserAuthority;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "user_role")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private UserAuthority authority;

    public static UserRoleEntity newInstance(UserEntity user, UserAuthority authority) {
        return UserRoleEntity.builder()
                .user(user)
                .authority(authority)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserRoleEntity userRole)) return false;
        return Objects.equals(id, userRole.id)
               && Objects.equals(user, userRole.user)
               && authority == userRole.authority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, authority);
    }
}
