package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.dto.PermissionDto;
import com.smsa.backend.dto.RoleDto;
import com.smsa.backend.model.Permission;
import com.smsa.backend.model.Roles;
import com.smsa.backend.repository.PermissionRepository;
import com.smsa.backend.repository.RolesRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository  permissionRepository;


    public Permission addPermission(Permission permission) {
       return permissionRepository.save(permission);
    }

    public List<Permission> getAllPermissions() {
        List<Permission> permissions = new ArrayList<>();
        for (Permission permission : permissionRepository.findAll()){
            permissions.add(permission);
        }
        return permissions;
    }

    public Permission findPermissionById(Long id) {
        Optional<Permission> permission = permissionRepository.findById(id);
        if(permission.isPresent()){
            return permission.get();
        }else{
            throw new RecordNotFoundException("Permission Couldn't Found");
        }
    }

    public Permission updatePermission(Permission permission) {
        Optional<Permission> permissionExist = permissionRepository.findById(permission.getId());
        if(permissionExist.isPresent()){
            return permissionRepository.save(permission);
        }else{
            throw new RecordNotFoundException("Permission Couldn't Found");
        }
    }


}
