package com.pet.comes.repository;

import java.util.Optional;

import com.pet.comes.model.Entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
	Optional<Address> findByLocationName(String locationName);
}
