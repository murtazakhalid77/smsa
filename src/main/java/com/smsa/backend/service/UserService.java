package com.smsa.backend.service;


import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.UserDto;
import com.smsa.backend.model.Roles;
import com.smsa.backend.model.User;
import com.smsa.backend.repository.RolesRepository;
import com.smsa.backend.repository.UserRepository;
import org.modelmapper.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RolesRepository rolesRepository;

    public User addUser(UserDto userDto) {

        try{
            User user= User.builder()
                    .name(userDto.getName())
                    .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                    .roles(assignRolesToUser(userDto))
                    .build();


            return userRepository.save(user);

        }catch (Exception e){
            throw new RuntimeException("Some thing went wrong in adding new user "+e);
        }
    }


    public Set<Roles> assignRolesToUser(UserDto userDto){
        Set<Roles> rolesSet = new HashSet<>();
        rolesSet.add(rolesRepository.getRoleByName(userDto.getRole()));
        return rolesSet;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        Page<User> users = this.userRepository.findAll(pageable);
        if(!users.isEmpty()){
            return users;
        }
        throw new RecordNotFoundException(String.format("Users not found"));
    }

    public User findUserById(Long id) {
            Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new RecordNotFoundException("User Couldn't Found");
        }
    }

    public User updateUser(UserDto userDto) {
        Optional<User> userExist = this.userRepository.findById(userDto.getId());
        if (userExist.isPresent()) {
            User user = userExist.get(); // Get the existing user from Optional
            user.setName(userDto.getName());

            // Check if the password needs to be updated and encode it
            if (!userDto.getPassword().startsWith("$")) {
                user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            }

            // Update the roles for the user
            user.setRoles(assignRolesToUser(userDto));

            return this.userRepository.save(user); // Save the updated user
        } else {
            throw new RecordNotFoundException("User Couldn't Be Found");
        }
    }

}
