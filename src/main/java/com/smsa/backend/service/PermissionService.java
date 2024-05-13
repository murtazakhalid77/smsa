package com.smsa.backend.service;

import com.smsa.backend.Exception.RecordNotFoundException;
import com.smsa.backend.model.Permission;
import com.smsa.backend.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository  permissionRepository;


    public Permission  addPermission(Permission permission) {
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
