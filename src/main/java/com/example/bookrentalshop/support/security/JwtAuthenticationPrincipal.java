package com.example.bookrentalshop.support.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtAuthenticationPrincipal implements Principal {

    private Long id;
    private String email;

    public JwtAuthenticationPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public String getName() {
        return email;
    }
}
