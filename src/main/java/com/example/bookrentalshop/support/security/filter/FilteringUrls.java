package com.example.bookrentalshop.support.security.filter;

import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilteringUrls {

    public static final Set<String> PUBLIC_PATHS = Sets.newHashSet(
            "/api/v1/users/login",
            "/api/v1/users/register",
            "/api/v1/users/refresh"
    );
}
