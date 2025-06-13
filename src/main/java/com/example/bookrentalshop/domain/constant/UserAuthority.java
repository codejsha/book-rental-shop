package com.example.bookrentalshop.domain.constant;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum UserAuthority implements GrantedAuthority {

    ROLE_BOOK_READ(Constants.ROLE_BOOK_READ),
    ROLE_BOOK_MANAGE(Constants.ROLE_BOOK_MANAGE),
    ROLE_RENTAL_READ_OWNER(Constants.ROLE_RENTAL_READ_OWNER),
    ROLE_RENTAL_REQUEST(Constants.ROLE_RENTAL_REQUEST),
    ROLE_RENTAL_MANAGE(Constants.ROLE_RENTAL_MANAGE),
    ROLE_ADMIN(Constants.ROLE_ADMIN);

    public static final Map<String, UserAuthority> AUTHORITY_MAP = Stream.of(UserAuthority.values())
            .collect(Collectors.toUnmodifiableMap(UserAuthority::getAuthority, Function.identity()));
    public static final List<UserAuthority> USER_DEFAULT_AUTHORITIES = Lists.newArrayList(
            ROLE_BOOK_READ,
            ROLE_RENTAL_READ_OWNER,
            ROLE_RENTAL_REQUEST
    );

    private final String authority;

    public static class Constants {
        public static final String ROLE_BOOK_READ = "ROLE_BOOK_READ";
        public static final String ROLE_BOOK_MANAGE = "ROLE_BOOK_MANAGE";
        public static final String ROLE_RENTAL_READ_OWNER = "ROLE_RENTAL_READ_OWNER";
        public static final String ROLE_RENTAL_REQUEST = "ROLE_RENTAL_REQUEST";
        public static final String ROLE_RENTAL_MANAGE = "ROLE_RENTAL_MANAGE";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
    }
}
