package com.smsa.backend.repository;


import com.smsa.backend.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findByName(String name);

    @Query("SELECT r FROM Roles r WHERE r.name = :name")
    Roles getRoleByName(@Param("name") String name);
}
