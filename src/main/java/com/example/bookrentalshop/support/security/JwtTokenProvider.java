package com.example.bookrentalshop.support.security;

import com.example.bookrentalshop.config.properties.TokenConfig;
import com.example.bookrentalshop.domain.constant.UserAuthority;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final TokenConfig tokenConfig;
    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(tokenConfig.getSigningKey().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(UserEntity user, UserDetails userDetails) {
        var now = LocalDateTime.now();
        var expiry = now.plus(tokenConfig.getAccessTokenExpiry());

        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .issuedAt(Date.from(now.atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .expiration(Date.from(expiry.atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .add("email", userDetails.getUsername())
                .add("type", "access")
                .add("authorities", userDetails.getAuthorities().stream().map(Object::toString).toList())
                .build();

        var token = Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
        return token;
    }

    public String createRefreshToken(UserEntity user, UserDetails userDetails) {
        var now = LocalDateTime.now();
        var expiry = now.plus(tokenConfig.getRefreshTokenExpiry());
        var jti = UUID.randomUUID().toString();

        var claims = Jwts.claims()
                .id(jti)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now.atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .expiration(Date.from(expiry.atZone(ZoneId.of("Asia/Seoul")).toInstant()))
                .add("email", userDetails.getUsername())
                .add("type", "refresh")
                .build();

        var token = Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
        return token;
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isRefreshToken(String token) {
        var claims = decodeToken(token);
        return "refresh".equals(claims.get("type"));
    }

    public JwtAuthenticationPrincipal createPrincipalFromClaims(Claims claims) {
        var id = claims.get("sub", String.class);
        var email = claims.get("email", String.class);
        return new JwtAuthenticationPrincipal(Long.parseLong(id), email);
    }

    public List<GrantedAuthority> createAuthoritiesFromClaims(Claims claims) {
        var authorities = claims.get("authorities", List.class);
        List<GrantedAuthority> authoritiesList = Lists.newArrayList();
        for (var authority : authorities) {
            authoritiesList.add(UserAuthority.valueOf((String) authority));
        }
        return authoritiesList;
    }
}
