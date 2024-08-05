package org.cenchev.hoamanagerapp.repository;

import org.cenchev.hoamanagerapp.model.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
