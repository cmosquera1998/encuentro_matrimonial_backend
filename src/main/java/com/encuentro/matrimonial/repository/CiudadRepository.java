package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.encuentro.matrimonial.modelo.Ciudad;

public interface CiudadRepository extends CrudRepository<Ciudad, Long> {

	@Query(value = "SELECT c FROM Ciudad c WHERE c.name = :name")
	Ciudad findByName(String name);

	@Query(value = "SELECT c FROM Ciudad c WHERE c.pais.id = :idPais")
	List<Ciudad> obtenerCiudadesPorPais(Long idPais);
	
	@Query(value = "SELECT c FROM Ciudad c WHERE c.id = :idCiudad")
	List<Ciudad> obtenerCiudadUsuario(Long idCiudad);

}
