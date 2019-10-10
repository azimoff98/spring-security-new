package com.example.testapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
public class JwtAuthenticationResponse {

    @Getter
    private final String token;
}
