package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.PrimerPilar;

public interface IPrimerPilarRepository extends CrudRepository<PrimerPilar, Long> {

	@Query("Select u FROM primer_pilar u WHERE u.id = :id")
	PrimerPilar findByPrimerPilar(@Param("id") Long id);

	@Query("Select u FROM primer_pilar u WHERE u.fechaCreacion = :fecha")
	PrimerPilar findByFiltroPrimerPilar(@Param("fecha") String fecha);

}
