package com.smsa.backend.controller;

import com.smsa.backend.model.User;
import com.smsa.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
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
    @GetMapping("/user")
    ResponseEntity<List<User>> users(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    ResponseEntity<User> findUserById(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.findUserById(id));
    }

    @PatchMapping("/user/{id}")
    ResponseEntity<User> updateUser(@RequestBody User user){
        return ResponseEntity.ok(this.userService.updateUser(user));
    }
}
