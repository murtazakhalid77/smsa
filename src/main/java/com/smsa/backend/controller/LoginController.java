package com.smsa.backend.controller;


import com.smsa.backend.dto.LoginCredentials;
import com.smsa.backend.security.util.AuthenticationResponse;
import com.smsa.backend.security.util.JwtUtil;
import com.smsa.backend.service.MyUserDetailService;
import com.smsa.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginCredentials loginCredentials) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCredentials.getName(),loginCredentials.getPassword())
            );
        }
        catch(BadCredentialsException e){
            throw new Exception("Incorrect Username or Password ! ",e);
        }

        UserDetails userDetails = myUserDetailService.loadUserByUsername(loginCredentials.getName());
        String jwtToken = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}