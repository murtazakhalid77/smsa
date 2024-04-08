package com.smsa.backend.repository;

import com.smsa.backend.model.ManifestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManifestDataRepository extends JpaRepository<ManifestData,Long> {
}
