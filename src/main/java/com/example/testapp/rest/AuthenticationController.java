package com.example.testapp.rest;

import com.example.testapp.security.dto.JwtAuthenticationRequest;
import com.example.testapp.security.dto.JwtAuthenticationResponse;
import com.example.testapp.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private AuthenticationService authenticationService;


    @PostMapping("/signin")
    public JwtAuthenticationResponse signin(@RequestBody JwtAuthenticationRequest request){
        return authenticationService.createAuthenticationToken(request);
    }


    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }
}
