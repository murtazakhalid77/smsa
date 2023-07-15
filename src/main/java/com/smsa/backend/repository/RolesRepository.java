package com.smsa.backend.repository;


import com.smsa.backend.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    HashSet<Roles> findByName(String name);

    @Query("SELECT r FROM Roles r WHERE r.name = :name")
    Roles getRoleByName(@Param("name") String name);
}
