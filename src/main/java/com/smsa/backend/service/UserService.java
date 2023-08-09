package com.smsa.backend.service;


import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.model.Roles;
import com.smsa.backend.model.User;
import com.smsa.backend.repository.RolesRepository;
import com.smsa.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
            User user = userRepository.save(userDto);
            user.setPassword("");
            return user;

        }catch (Exception e){
            throw new RuntimeException("Some thing went wrong in adding new user "+e);
        }
    }


    public Set<Roles> assignRolesToUser(User userDto){
        Set<Roles> rolesSet = new HashSet<>();
        rolesSet.add(rolesRepository.getRoleByName("ROLE_ADMIN"));
        return rolesSet;
    }

    public List<User> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        for(User user: users){
            user.setPassword("");
        }

        return users;
    }

    public User findUserById(Long id) {
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()){
            user.get().setPassword("");
            return user.get();
        }else{
            throw new RecordNotFoundException("User Couldn't Found");
        }
    }

    public User updateUser(User user) {
        Optional<User> userExist = this.userRepository.findById(user.getId());
        if(userExist.isPresent()){
            if(user.getPassword().length() == 0){
                user.setPassword(userExist.get().getPassword());
            }else{
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            }
            User user1 = this.userRepository.save(user);
            user1.setPassword("");
            return user1;
        }else{
            throw new RecordNotFoundException("User Couldn't Found");
        }
    }
}
