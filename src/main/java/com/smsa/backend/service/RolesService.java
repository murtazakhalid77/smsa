package com.smsa.backend.service;



import com.smsa.backend.Exception.RecordNotFoundException;

import com.smsa.backend.dto.RoleDto;
import com.smsa.backend.model.Permission;
import com.smsa.backend.model.Roles;
import com.smsa.backend.repository.RolesRepository;
import jdk.nashorn.internal.runtime.options.Option;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RolesService {
    @Autowired
    RolesRepository roleRepository;



    public Roles addRole(Roles role) {
        Optional<Roles> existingRole = roleRepository.findById(role.getId());
        Set<Permission> truePermissions = new HashSet<>();
        if (existingRole.isPresent() && role!=null) {
            for(Permission permission: role.getPermissions()){
                if(permission.isValue()){
                    truePermissions.add(permission);
                }
            }
            existingRole.get().setPermissions(truePermissions);

        }

        // Save the role (either the existing one or the new one)
        return roleRepository.save(existingRole.get());
    }

    public List<Roles> getAllRoles() {
        List<Roles> roles = roleRepository.findAll();
        return roles;
    }

    public Roles findRoleById(Long id) {
        Roles role = roleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Role Couldn't Found"));
        return (role);
    }
    public Roles findRoleByName(String name) {
        Roles role = roleRepository.findByName(name);
        return (role);
    }

    public Roles updateRole(Roles role) {
        Roles existingRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new RecordNotFoundException("Role Couldn't Found"));

        return   roleRepository.save(role);

    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RecordNotFoundException("Role Couldn't Found");
        }

        roleRepository.deleteById(id);
    }



}

