package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.Role;
import org.cenchev.hoamanagerapp.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(RoleType roleType);
}
