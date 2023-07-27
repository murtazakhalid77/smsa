package com.smsa.backend.repository;

import com.smsa.backend.controller.CustomController;
import com.smsa.backend.model.Custom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRepository extends JpaRepository<Custom, Long> {
}
