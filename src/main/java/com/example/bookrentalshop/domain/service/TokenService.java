package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.domain.entity.TokenEntity;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.example.bookrentalshop.repository.TokenRepository;
import com.example.bookrentalshop.support.security.JwtAuthenticationPrincipal;
import com.example.bookrentalshop.support.security.JwtAuthenticationToken;
import com.example.bookrentalshop.support.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public void authenticateToken(Long id, String email, String jwt, List<? extends GrantedAuthority> authorities) {
        var principal = new JwtAuthenticationPrincipal(id, email);
        var authToken = new JwtAuthenticationToken(principal, jwt, authorities);
        authenticationManager.authenticate(authToken);
    }

    public Claims decodeToken(String token) {
        return jwtTokenProvider.decodeToken(token);
    }

    @Transactional
    public TokenEntity issueToken(UserEntity user, List<? extends GrantedAuthority> roles) {
        var userDetails = createUserDetails(user.getEmail(), user.getPassword(), roles);
        var accessToken = jwtTokenProvider.createAccessToken(user, userDetails);
        var refreshToken = jwtTokenProvider.createRefreshToken(user, userDetails);
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);
        return tokenRepository.save(token);
    }

    @Transactional
    public TokenEntity reissueToken(UserEntity user, List<? extends GrantedAuthority> roles, String refreshToken) {
        var userDetails = createUserDetails(user.getEmail(), user.getPassword(), roles);
        var accessToken = jwtTokenProvider.createAccessToken(user, userDetails);
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);
        return tokenRepository.save(token);
    }

    private UserDetails createUserDetails(String email, String password, List<? extends GrantedAuthority> roles) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(password)
                .authorities(roles)
                .build();
    }
}
