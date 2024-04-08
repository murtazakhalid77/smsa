package com.smsa.backend.repository;

import com.smsa.backend.model.RecordManifestFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface RecordManifestFolderRepository extends JpaRepository<RecordManifestFolder, Long> {

//    @Query(value = "SELECT r.file_name FROM record_manifest_folder r")
//    List<String> findAllfileName();

//    @Query(value = "INSERT INTO record_manifest_folder(file_name) VALUES (:fileName)")
//    void saveByName(String fileName);
}
