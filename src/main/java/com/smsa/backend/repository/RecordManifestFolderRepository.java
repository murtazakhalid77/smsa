package com.smsa.backend.repository;

import com.smsa.backend.model.RecordManifestFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordManifestFolderRepository extends JpaRepository<RecordManifestFolder, Long> {
}
