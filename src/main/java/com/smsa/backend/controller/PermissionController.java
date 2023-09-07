package com.smsa.backend.controller;

import com.smsa.backend.dto.PermissionDto;
import com.smsa.backend.model.Permission;
import com.smsa.backend.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    //    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/permission")
    public Permission addPermission(@RequestBody Permission permission){
        try{
            return permissionService.addPermission(permission);
        }catch (Exception e){
            return null;
        }
    }
    @GetMapping("/permission")
    ResponseEntity<List<Permission>> permissions(){
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/permission/{id}")
    ResponseEntity<Permission> findPermissionById(@PathVariable Long id){
        return ResponseEntity.ok(permissionService.findPermissionById(id));
    }

    @PatchMapping("/permission/{id}")
    ResponseEntity<Permission> updatePermission(@RequestBody Permission permission){
        return ResponseEntity.ok(permissionService.updatePermission(permission));
    }
}
