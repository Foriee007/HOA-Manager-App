package org.cenchev.hoamanagerapp.repository;

import jakarta.transaction.Transactional;
import org.cenchev.hoamanagerapp.model.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO admin (user_id) VALUES (:userId)", nativeQuery = true)
    void saveAdmin(@Param("userId") Long userId);
}
