package com.smsa.backend.repository;

import com.smsa.backend.model.Permission;
import org.hibernate.cfg.JPAIndexHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
