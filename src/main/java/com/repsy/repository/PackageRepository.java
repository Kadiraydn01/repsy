package com.repsy.repository;

import com.repsy.model.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM package_entity WHERE name = ?1 AND version = ?2")
    Optional<PackageEntity> findByNameAndVersion(String name, String version);
}
