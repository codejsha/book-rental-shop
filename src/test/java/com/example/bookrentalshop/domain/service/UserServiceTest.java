package com.example.bookrentalshop.domain.service;

import com.example.bookrentalshop.controller.dto.RefreshRequest;
import com.example.bookrentalshop.controller.dto.UserLoginRequest;
import com.example.bookrentalshop.controller.dto.UserRegisterRequest;
import com.example.bookrentalshop.domain.constant.UserAuthority;
import com.example.bookrentalshop.domain.entity.TokenEntity;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.example.bookrentalshop.domain.entity.UserRoleEntity;
import com.example.bookrentalshop.repository.UserRepository;
import com.example.bookrentalshop.repository.UserRoleRepository;
import com.google.common.collect.Lists;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Test
    void loginUser_WhenCredentialsAreValid_ReturnsToken() {
        // arrange
        var roles = Lists.newArrayList(
                UserAuthority.ROLE_BOOK_READ,
                UserAuthority.ROLE_RENTAL_READ_OWNER,
                UserAuthority.ROLE_RENTAL_REQUEST
        );
        var req = UserLoginRequest.builder()
                .email("user1@example.com")
                .password("password")
                .build();
        var user = UserEntity.builder()
                .id(1L)
                .email("user1@example.com")
                .password("encoded-password")
                .name("User1")
                .build();
        var userRoles = roles.stream()
                .map(role -> UserRoleEntity.newInstance(user, role))
                .toList();
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password("encoded-password")
                .authorities(roles)
                .build();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(user));
        when(userRoleRepository.findAllByUserId(user.getId())).thenReturn(userRoles);
        when(passwordEncoder.matches(req.getPassword(), userDetails.getPassword())).thenReturn(true);
        when(tokenService.issueToken(any(UserEntity.class), ArgumentMatchers.<List<UserAuthority>>any()))
                .thenReturn(token);

        // act
        TokenEntity result = userService.loginUser(req);

        // assert
        assertNotNull(result);
        assertEquals(token, result);
    }

    @Test
    void registerUser_WhenNewUser_ReturnsToken() {
        // arrange
        var req = UserRegisterRequest.builder()
                .email("user1@example.com")
                .password("password")
                .build();
        var user = UserEntity.builder()
                .id(1L)
                .email("user1@example.com")
                .password("password")
                .name("User1")
                .build();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(req.getPassword())).thenReturn("encoded-password");
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(userRoleRepository.saveAll(ArgumentMatchers.<List<UserRoleEntity>>any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(tokenService.issueToken(any(UserEntity.class), ArgumentMatchers.<List<UserAuthority>>any()))
                .thenReturn(token);

        // act
        TokenEntity result = userService.registerUser(req);

        // assert
        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
    }

    @Test
    void refreshUserToken_WhenTokenIsValid_ReturnsNewToken() {
        // arrange
        var roles = Lists.newArrayList(
                UserAuthority.ROLE_BOOK_READ,
                UserAuthority.ROLE_RENTAL_READ_OWNER,
                UserAuthority.ROLE_RENTAL_REQUEST
        );
        var user = UserEntity.builder()
                .id(1L)
                .email("user1@example.com")
                .password("encoded-password")
                .name("User1")
                .build();
        var userRoles = roles.stream()
                .map(role -> UserRoleEntity.newInstance(user, role))
                .toList();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);

        var req = RefreshRequest.builder()
                .refreshToken(refreshToken)
                .build();
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .build();

        when(tokenService.decodeToken(req.getRefreshToken())).thenReturn(claims);
        when(userRepository.findById(Long.valueOf(claims.getSubject()))).thenReturn(Optional.of(user));
        when(userRoleRepository.findAllByUserId(user.getId())).thenReturn(userRoles);
        when(tokenService.reissueToken(any(UserEntity.class), ArgumentMatchers.<List<UserAuthority>>any(), any(String.class)))
                .thenReturn(token);

        // act
        TokenEntity result = userService.refreshUserToken(req);

        // assert
        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());
        assertEquals(refreshToken, result.getRefreshToken());
    }
}
