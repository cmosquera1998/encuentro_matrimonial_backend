package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.encuentro.matrimonial.modelo.Privilege;



public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

	@Query(value = "SELECT * FROM PRIVILEGE WHERE name = ?1", nativeQuery = true)
	Privilege findByName(String name);
	
}
