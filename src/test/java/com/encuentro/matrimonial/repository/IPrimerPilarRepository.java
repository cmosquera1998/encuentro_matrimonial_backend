package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.PrimerPilar;

public interface IPrimerPilarRepository extends CrudRepository<PrimerPilar, Long> {

	@Query("Select p FROM primer_pilar p WHERE p.id = :id")
	PrimerPilar findByPrimerPilar(@Param("id") Long id);

	@Query("Select p FROM primer_pilar p WHERE p.fechaCreacion = :fecha")
	PrimerPilar findByFiltroPrimerPilar(@Param("fecha") String fecha);

	@Query(value = "SELECT p FROM primer_pilar p WHERE p.ciudad.pais.id = :idPais")
	List<PrimerPilar> obtenerPilarPorPais(Long idPais);

	@Query(value = "SELECT p FROM primer_pilar p WHERE p.ciudad.id = :idCiudad")
	List<PrimerPilar> obtenerPilarPorCiudad(Long idCiudad);

}
