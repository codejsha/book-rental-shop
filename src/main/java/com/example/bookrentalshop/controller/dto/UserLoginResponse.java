package com.example.bookrentalshop.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {

    private String accessToken;
}
