package com.example.bookrentalshop.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.example.bookrentalshop.controller.dto.RefreshRequest;
import com.example.bookrentalshop.controller.dto.UserLoginRequest;
import com.example.bookrentalshop.controller.dto.UserRegisterRequest;
import com.example.bookrentalshop.domain.entity.TokenEntity;
import com.example.bookrentalshop.domain.entity.UserEntity;
import com.example.bookrentalshop.domain.service.UserService;
import com.example.bookrentalshop.support.restdocs.RestDocsControllerSupport;
import com.example.bookrentalshop.support.security.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
class UserControllerTest extends RestDocsControllerSupport {

    @MockitoBean
    private UserService userService;

    @Test
    void loginUser() throws Exception {
        // arrange
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

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);

        when(userService.loginUser(any(UserLoginRequest.class))).thenReturn(token);

        // act
        var execute = mockMvc.perform(post("/api/v1/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("loginUser"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("User")
                        .summary("User Login")
                        .description("Login a user with email and password.")
                        .requestSchema(Schema.schema("UserLoginRequest"))
                        .requestFields(
                                fieldWithPath("email").description("User email"),
                                fieldWithPath("password").description("User password"))
                        .responseSchema(Schema.schema("UserLoginResponse"))
                        .responseFields(
                                fieldWithPath("accessToken").description("Access token for the user"))
                        .responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE).description("Refresh token cookie"))
                        .build()
                )));
    }

    @Test
    void registerUser() throws Exception {
        // arrange
        var req = UserRegisterRequest.builder()
                .email("user1@example.com")
                .password("password")
                .name("User1")
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

        when(userService.registerUser(any(UserRegisterRequest.class))).thenReturn(token);

        // act
        var execute = mockMvc.perform(post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("registerUser"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("User")
                        .summary("User Registration")
                        .description("Register a new user.")
                        .requestSchema(Schema.schema("UserRegisterRequest"))
                        .requestFields(
                                fieldWithPath("email").description("User email"),
                                fieldWithPath("password").description("User password"),
                                fieldWithPath("name").description("User name"))
                        .responseSchema(Schema.schema("UserRegisterResponse"))
                        .responseFields(
                                fieldWithPath("accessToken").description("Access token for the user"))
                        .responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE).description("Refresh token cookie"))
                        .build()
                )));
    }

    @Test
    void refreshUserToken() throws Exception {
        // arrange
        var user = UserEntity.builder()
                .id(1L)
                .email("user1@example.com")
                .password("encoded-password")
                .name("User1")
                .build();

        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        var token = TokenEntity.newInstance(user, accessToken, refreshToken);

        var req = RefreshRequest.builder()
                .refreshToken(refreshToken)
                .build();

        when(userService.refreshUserToken(any(RefreshRequest.class))).thenReturn(token);

        // act
        var execute = mockMvc.perform(post("/api/v1/users/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON));

        // assert
        execute.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("refreshUserToken"));

        // document
        execute.andDo(MockMvcRestDocumentationWrapper.document(OPENAPI_DOCUMENT_IDENTIFIER,
                ResourceDocumentation.resource(ResourceSnippetParameters.builder()
                        .tag("User")
                        .summary("User Token Refresh")
                        .description("Refresh(reissue) user access token using refresh token.")
                        .requestSchema(Schema.schema("RefreshRequest"))
                        .requestFields(
                                fieldWithPath("refreshToken").description("Refresh token"))
                        .responseSchema(Schema.schema("RefreshResponse"))
                        .responseFields(
                                fieldWithPath("accessToken").description("Access token for the user"))
                        .responseHeaders(
                                headerWithName(HttpHeaders.SET_COOKIE).description("Refresh token cookie"))
                        .build()
                )));
    }
}
