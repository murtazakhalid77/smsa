package com.smsa.backend.service;


import com.smsa.backend.model.Roles;
import com.smsa.backend.model.User;
import com.smsa.backend.repository.RolesRepository;
import com.smsa.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RolesRepository rolesRepository;

    public User addUser(User userDto) {
        try{
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            userDto.setRoles(assignRolesToUser(userDto));
            return userRepository.save(userDto);

        }catch (Exception e){
            throw new RuntimeException("Some thing went wrong in adding new user "+e);
        }
    }


    public Set<Roles> assignRolesToUser(User userDto){
        Set<Roles> rolesSet = new HashSet<>();
        rolesSet.add(rolesRepository.getRoleByName("ROLE_ADMIN"));
        return rolesSet;
    }

}
