package com.example.bookrentalshop.support.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockPrincipalSecurityContextFactory.class)
public @interface WithMockPrincipal {

    long id() default 1L;

    String email() default "user@example.com";

    String credentials() default "test-token";

    String[] authorities() default {"ROLE_BOOK_READ", "ROLE_RENTAL_READ_OWNER", "ROLE_RENTAL_REQUEST"};
}
