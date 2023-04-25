package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.SegundoPilar;

public interface ISegundoPilarRepository extends CrudRepository<SegundoPilar, Long> {

	@Query("Select s FROM segundo_pilar s WHERE s.id = :id")
	SegundoPilar findBySegundoPilar(@Param("id") Long id);

	@Query("Select s FROM segundo_pilar s WHERE s.fechaCreacion = :fecha")
	SegundoPilar findByFiltroSegundoPilar(@Param("fecha") String fecha);

	@Query(value = "SELECT s FROM segundo_pilar s WHERE s.ciudad.pais.id = :idPais")
	List<SegundoPilar> obtenerPilarPorPais(Long idPais);

	@Query(value = "SELECT s FROM segundo_pilar s WHERE s.ciudad.id = :idCiudad")
	List<SegundoPilar> obtenerPilarPorCiudad(Long idCiudad);

}
