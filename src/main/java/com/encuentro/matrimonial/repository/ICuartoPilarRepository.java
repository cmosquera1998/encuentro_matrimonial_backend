package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.CuartoPilar;

public interface ICuartoPilarRepository extends CrudRepository<CuartoPilar, Long> {

	@Query("Select c FROM cuarto_pilar c WHERE c.id = :id")
	CuartoPilar findByCuartoPilar(@Param("id") Long id);

	@Query(value = "SELECT c FROM cuarto_pilar c WHERE c.ciudad.pais.id = :idPais")
	List<CuartoPilar> obtenerPilarPorPais(Long idPais);

	@Query(value = "SELECT c FROM cuarto_pilar c WHERE c.ciudad.id = :idCiudad")
	List<CuartoPilar> obtenerPilarPorCiudad(Long idCiudad);
	
	@Query(value = "SELECT c FROM cuarto_pilar c WHERE c.ciudad.region.id = :idRegion")
	List<CuartoPilar> obtenerPilarPorRegionPais(Long idRegion);

	@Query(value = "SELECT c FROM cuarto_pilar c WHERE c.ciudad.pais.zona.id = :idZona")
	List<CuartoPilar> obtenerPilarPorZona(Long idZona);

}
