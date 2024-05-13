package com.smsa.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsa.backend.criteria.SearchCriteria;
import com.smsa.backend.dto.UserDto;
import com.smsa.backend.model.User;
import com.smsa.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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
    public User addUser(@RequestBody UserDto userDto){
        try{
            return userService.addUser(userDto);
        }catch (Exception e){
            return null;
        }
    }
    @GetMapping("/user")
    ResponseEntity<List<User>> users(@RequestParam("search") String search,
                                     @RequestParam(value = "page") int page,
                                     @RequestParam(value = "size") int size,
                                     @RequestParam(value = "sort", defaultValue = "id") String sort) throws JsonProcessingException {
        SearchCriteria searchCriteria = new ObjectMapper().readValue(search, SearchCriteria.class);
        Page<User> users = this.userService.getAllUsers(searchCriteria, PageRequest.of(page, size,  Sort.by(sort).descending()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(users.getTotalElements()));
        return ResponseEntity.ok()
                .headers(headers)// Set the headers
                .body(users.getContent());
    }

    @GetMapping("/user/{id}")
    ResponseEntity<User> findUserById(@PathVariable Long id){
        return ResponseEntity.ok(this.userService.findUserById(id));
    }



    @PatchMapping("/user/{id}")
    ResponseEntity<User> updateUser(@RequestBody UserDto user){
        return ResponseEntity.ok(this.userService.updateUser(user));
    }
}
