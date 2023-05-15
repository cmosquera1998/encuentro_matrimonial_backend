package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.FormacionSacerdote;

public interface IFormacionSacerdoteRepository extends CrudRepository<FormacionSacerdote, Long> {

	@Query("Select f FROM FormacionSacerdote f WHERE f.id = :id")
	FormacionSacerdote findByFormacionSacerdote(@Param("id") Long id);

	@Query(value = "SELECT f FROM FormacionSacerdote f WHERE f.ciudad.pais.id = :idPais")
	List<FormacionSacerdote> obtenerFormacionPorPais(Long idPais);

	@Query(value = "SELECT f FROM FormacionSacerdote f WHERE f.ciudad.id = :idCiudad")
	List<FormacionSacerdote> obtenerFormacionPorCiudad(Long idCiudad);

	@Query(value = "SELECT f FROM FormacionSacerdote f WHERE f.ciudad.zona.id = :idZona")
	List<FormacionSacerdote> obtenerPilarPorZona(Long idZona);

}
