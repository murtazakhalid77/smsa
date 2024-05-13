package com.smsa.backend.controller;


import com.smsa.backend.model.Roles;
import com.smsa.backend.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class RolesController {
    private final RolesService roleService;

    @Autowired
    public RolesController(RolesService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/role")
    public ResponseEntity<Roles> addRole(@RequestBody Roles roles) {
            return ResponseEntity.status(HttpStatus.CREATED).body(roleService.addRole(roles));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<Roles> findRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }



    @DeleteMapping("/role/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok("Role deleted successfully");
    }
}

