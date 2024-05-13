package com.smsa.backend.repository;

import com.smsa.backend.model.ManifestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManifestDataRepository extends JpaRepository<ManifestData,Long> {
    List<ManifestData> findByAwbIn(List<String> awbs);

    @Query(value = "SELECT md.* " +
            "FROM manifest_data md " +
            "WHERE md.manifest_number = :manifestNo", nativeQuery = true)
    List<ManifestData> getManifestDataByManifestNoOnly(@Param("manifestNo") String manifestNo);

    @Query(value = "SELECT md.* " +
            "FROM manifest_data md " +
            "WHERE md.manifest_number = :manifestNo && md.prefix = :prefix", nativeQuery = true)
    List<ManifestData> getManifestDataByManifestNoAndPrefix(@Param("prefix") String prefix, @Param("manifestNo") String manifestNo);

    List<ManifestData> findAllByIdIn(List<Long> manifestDataIds);

    @Query(value = "SELECT md.awb FROM manifest_data md", nativeQuery = true)
    List<String> getAllAwb();
}
