package com.example.testapp.security.service;

import com.example.testapp.model.AppUser;
import com.example.testapp.repository.AppUserRepository;
import com.example.testapp.security.dto.JwtAuthenticationRequest;
import com.example.testapp.security.dto.JwtAuthenticationResponse;
import com.example.testapp.security.exception.AuthenticationException;
import com.example.testapp.security.exception.TokenNotFoundException;
import com.example.testapp.security.exception.TokenRefreshException;
import com.example.testapp.security.model.JwtUser;
import com.example.testapp.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;

    @Autowired
    private AppUserRepository appUserRepository;




    public JwtAuthenticationResponse createAuthenticationToken(JwtAuthenticationRequest request){
        authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return new JwtAuthenticationResponse(token);
    }

    public JwtAuthenticationResponse refreshToken(String oldToken){
        if(Objects.isNull(oldToken) || oldToken.length() < 7)
            throw new TokenNotFoundException("No old token");

        String token = oldToken.substring(7);

        if(jwtTokenUtil.canTokenBeRefreshed(token)){
            String newToken = jwtTokenUtil.refreshToken(token);
            return new JwtAuthenticationResponse(newToken);
        }
        throw new TokenRefreshException("token cannot be refreshed");
    }

    public JwtUser getUserByToken(String authToken){
        if(Objects.isNull(authToken) || authToken.length() < 7)
            throw new TokenNotFoundException("cant get user by token");

        String token = authToken.substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    private void authenticate(String username, String password){
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch (DisabledException e){
            throw new AuthenticationException("user is disabled", e);
        }catch (BadCredentialsException e){
            throw new AuthenticationException("bad credentials", e);
        }
    }

    public AppUser save(AppUser appUser) {
        try{
            return appUserRepository.save(appUser);
        }catch(Throwable t){
            t.printStackTrace();
            return null;
        }
    }


    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Autowired
    @Qualifier("jwtUserDetailsService")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
