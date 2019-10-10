package com.example.testapp.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class JwtAuthenticationRequest {

    private String username;
    private String password;


}
