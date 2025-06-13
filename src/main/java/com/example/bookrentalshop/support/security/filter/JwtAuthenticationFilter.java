package com.example.bookrentalshop.support.security.filter;

import com.example.bookrentalshop.support.security.JwtAuthenticationToken;
import com.example.bookrentalshop.support.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var tokenString = authHeader.substring(7);
            var claims = jwtTokenProvider.decodeToken(tokenString);
            var principal = jwtTokenProvider.createPrincipalFromClaims(claims);
            var authorities = jwtTokenProvider.createAuthoritiesFromClaims(claims);
            var authToken = new JwtAuthenticationToken(principal, tokenString, authorities);

            var authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var excludedPaths = FilteringUrls.PUBLIC_PATHS;
        var parser = PathPatternParser.defaultInstance;
        var path = PathContainer.parsePath(request.getServletPath());

        return excludedPaths.stream()
                .map(parser::parse)
                .anyMatch(pattern -> pattern.matches(path));
    }
}
