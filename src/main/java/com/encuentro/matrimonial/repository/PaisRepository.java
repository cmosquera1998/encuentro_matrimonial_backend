package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.encuentro.matrimonial.modelo.Pais;

public interface PaisRepository extends JpaRepository<Pais, Integer> {

	@Query(value = "SELECT * FROM PAIS WHERE name = ?1", nativeQuery = true)
	Pais findByName(String name);

}
