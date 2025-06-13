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
import com.example.bookrentalshop.support.exception.auth.PasswordInvalidException;
import com.example.bookrentalshop.support.exception.auth.UserNotFoundException;
import com.example.bookrentalshop.support.exception.resource.ResourceAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public TokenEntity loginUser(UserLoginRequest req) {
        // retrieve user info
        var user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var roles = userRoleRepository.findAllByUserId(user.getId()).stream()
                .map(UserRoleEntity::getAuthority)
                .toList();

        // check password
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new PasswordInvalidException("Invalid password");
        }

        // issue token
        var token = tokenService.issueToken(user, roles);
        tokenService.authenticateToken(user.getId(), user.getEmail(), token.getAccessToken(), roles);
        return token;
    }

    @Transactional
    public TokenEntity registerUser(UserRegisterRequest req) {
        // check duplicate user
        userRepository.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new ResourceAlreadyExistsException("User already exists");
        });

        // save user
        var encodedPassword = passwordEncoder.encode(req.getPassword());
        req.setEncryptedPassword(encodedPassword);
        var user = UserEntity.newInstance(req.getEmail(), req.getPassword(), req.getName());
        userRepository.save(user);

        // assign user roles
        var roles = UserAuthority.USER_DEFAULT_AUTHORITIES;
        var userRoles = roles.stream()
                .map(role -> UserRoleEntity.newInstance(user, role))
                .toList();
        userRoleRepository.saveAll(userRoles);

        var token = tokenService.issueToken(user, roles);
        return token;
    }

    @Transactional
    public TokenEntity refreshUserToken(RefreshRequest req) {
        // retrieve user info
        var claims = tokenService.decodeToken(req.getRefreshToken());
        var user = userRepository.findById(Long.valueOf(claims.getSubject()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        var roles = userRoleRepository.findAllByUserId(user.getId()).stream()
                .map(UserRoleEntity::getAuthority)
                .toList();

        // issue token
        var token = tokenService.reissueToken(user, roles, req.getRefreshToken());
        return token;
    }
}
