package com.example.bookrentalshop.controller;

import com.example.bookrentalshop.controller.dto.*;
import com.example.bookrentalshop.domain.entity.TokenEntity;
import com.example.bookrentalshop.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@Valid @RequestBody UserLoginRequest req) {
        var token = userService.loginUser(req);
        var cookieString = createRefreshTokenCookie(token);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieString);
        var response = UserLoginResponse.builder()
                .accessToken(token.getAccessToken())
                .build();
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@Valid @RequestBody UserRegisterRequest req) {
        var token = userService.registerUser(req);
        var cookieString = createRefreshTokenCookie(token);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieString);
        var response = UserRegisterResponse.builder()
                .accessToken(token.getAccessToken())
                .build();
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshUserToken(@Valid @RequestBody RefreshRequest req) {
        var token = userService.refreshUserToken(req);
        var cookieString = createRefreshTokenCookie(token);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookieString);
        var response = RefreshResponse.builder()
                .accessToken(token.getAccessToken())
                .build();
        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    private static String createRefreshTokenCookie(TokenEntity token) {
        var cookie = ResponseCookie.from("refresh-token", token.getRefreshToken())
                .path("/api/v1/users/refresh")
                .maxAge(3600)
                .secure(false)
                .httpOnly(false)
                .build();
        return cookie.toString();
    }
}
