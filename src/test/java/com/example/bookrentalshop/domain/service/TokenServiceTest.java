package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.domain.constant.UserAuthority;
import com.example.bookrentalshop.domain.entity.TokenEntity;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.example.bookrentalshop.repository.TokenRepository;
import com.example.bookrentalshop.support.security.JwtTokenProvider;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private TokenRepository tokenRepository;

    @Test
    void issueToken_WhenIssueToken_ShouldReturnToken() {
        // arrange
        var roles = Lists.newArrayList(
                UserAuthority.ROLE_BOOK_READ,
                UserAuthority.ROLE_RENTAL_READ_OWNER,
                UserAuthority.ROLE_RENTAL_REQUEST
        );
        var user = UserEntity.builder()
                .email("user@example.com")
                .password("password")
                .name("User1")
                .build();
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(roles)
                .build();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);

        when(jwtTokenProvider.createAccessToken(user, userDetails)).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(user, userDetails)).thenReturn(refreshToken);
        when(tokenRepository.save(any(TokenEntity.class))).thenReturn(token);

        // act
        TokenEntity result = tokenService.issueToken(user, roles);

        // assert
        assertNotNull(result);
        assertEquals(token, result);

        verify(jwtTokenProvider).createAccessToken(user, userDetails);
        verify(jwtTokenProvider).createRefreshToken(user, userDetails);
        verify(tokenRepository).save(any(TokenEntity.class));

        verify(jwtTokenProvider, times(1)).createAccessToken(user, userDetails);
        verify(jwtTokenProvider, times(1)).createRefreshToken(user, userDetails);
        verify(tokenRepository, times(1)).save(any(TokenEntity.class));
    }

    @Test
    void reissueToken_WhenRenewAccessToken_ShouldReturnToken() {
        // arrange
        var roles = Lists.newArrayList(
                UserAuthority.ROLE_BOOK_READ,
                UserAuthority.ROLE_RENTAL_READ_OWNER,
                UserAuthority.ROLE_RENTAL_REQUEST
        );
        var user = UserEntity.builder()
                .email("user@example.com")
                .password("password")
                .name("User1")
                .build();
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(roles)
                .build();

        String recreatedAccessToken = "recreated-access-token";
        String refreshToken = "refresh-token";
        var recreatedToken = TokenEntity.newInstance(user, recreatedAccessToken, refreshToken);

        when(jwtTokenProvider.createAccessToken(user, userDetails)).thenReturn(recreatedAccessToken);
        when(tokenRepository.save(any(TokenEntity.class))).thenReturn(recreatedToken);

        // act
        TokenEntity result = tokenService.reissueToken(user, roles, refreshToken);

        // assert
        assertNotNull(result);
        assertEquals(recreatedToken, result);

        verify(jwtTokenProvider).createAccessToken(user, userDetails);
        verify(tokenRepository).save(any(TokenEntity.class));

        verify(jwtTokenProvider, times(1)).createAccessToken(user, userDetails);
        verify(jwtTokenProvider, times(0)).createRefreshToken(user, userDetails);
        verify(tokenRepository, times(1)).save(any(TokenEntity.class));
    }
}
