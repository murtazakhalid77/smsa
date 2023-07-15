package com.smsa.backend.controller;


import com.smsa.backend.model.Roles;
import com.smsa.backend.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RolesController {
    @Autowired
    private RolesService rolesService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/roles")
    ResponseEntity<List<Roles>> getAllRoles(){
        return ResponseEntity.ok(rolesService.getAllRoles());
    }

}
