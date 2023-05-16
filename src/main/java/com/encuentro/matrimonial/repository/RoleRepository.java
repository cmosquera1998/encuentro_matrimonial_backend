package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.encuentro.matrimonial.modelo.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query(value = "SELECT * FROM ROLE WHERE name = ?1", nativeQuery = true)
	Role findByName(String name);

	@Query(value = "SELECT * FROM ROLE WHERE id = ?1", nativeQuery = true)
	List<Role> findById(String id);
}
