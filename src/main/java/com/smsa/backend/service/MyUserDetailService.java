package com.smsa.backend.service;

import com.smsa.backend.dto.CustomUserDetail;
import com.smsa.backend.model.User;
import com.smsa.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {

           User user = userRepository.findByName(username);
            if(user == null){
            throw new RuntimeException("Wrong Credentials"+username);
            }
            return new CustomUserDetail(user);
    }

}
