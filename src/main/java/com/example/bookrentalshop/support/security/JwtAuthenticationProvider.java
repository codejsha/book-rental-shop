package com.example.bookrentalshop.support.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var token = (JwtAuthenticationToken) authentication;
        var accessToken = token.getCredentials();
        var claims = jwtTokenProvider.decodeToken((String) accessToken);
        if (claims == null) {
            return null;
        }
        var principal = jwtTokenProvider.createPrincipalFromClaims(claims);
        var authorities = jwtTokenProvider.createAuthoritiesFromClaims(claims);
        var authToken = new JwtAuthenticationToken(principal, (String) accessToken, authorities);
        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
