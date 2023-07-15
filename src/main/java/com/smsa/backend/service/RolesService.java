package com.smsa.backend.service;



import com.smsa.backend.model.Roles;
import com.smsa.backend.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RolesService {
    @Autowired
    RolesRepository rolesRepository;

    public List<Roles> getAllRoles() {
        try {
            return rolesRepository.findAll().stream()
                    .map(roles -> roles)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return null;
        }
    }
}