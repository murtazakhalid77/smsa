package com.smsa.backend.controller;

import com.smsa.backend.model.User;
import com.smsa.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/user")
    public User addUser(@RequestBody User user){
        try{
            return userService.addUser(user);
        }catch (Exception e){
            return null;
        }
    }
}
