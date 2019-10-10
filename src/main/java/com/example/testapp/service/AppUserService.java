package com.example.testapp.service;

import com.example.testapp.model.AppUser;
import com.example.testapp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserService {


    @Autowired
    private AppUserRepository appUserRepository;

    public AppUser findById(Long id){
        return appUserRepository.findById(id).orElse(null);
    }

    public AppUser findByUsername(String username){
        return appUserRepository.findByUsername(username).orElse(null);
    }

    public void save(AppUser appUser){
        appUserRepository.save(appUser);
    }


}
