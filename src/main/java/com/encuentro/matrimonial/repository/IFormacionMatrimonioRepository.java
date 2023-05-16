package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.FormacionMatrimonio;

public interface IFormacionMatrimonioRepository extends CrudRepository<FormacionMatrimonio, Long> {

	@Query("Select f FROM FormacionMatrimonio f WHERE f.id = :id")
	FormacionMatrimonio findByFormacionMatrimonio(@Param("id") Long id);
	
	@Query(value = "SELECT f FROM FormacionMatrimonio f WHERE f.ciudad.pais.id = :idPais")
	List<FormacionMatrimonio> obtenerFormacionPorPais(Long idPais);

	@Query(value = "SELECT f FROM FormacionMatrimonio f WHERE f.ciudad.id = :idCiudad")
	List<FormacionMatrimonio> obtenerFormacionPorCiudad(Long idCiudad);

	//@Query(value = "SELECT f FROM FormacionMatrimonio f WHERE f.ciudad.zona.id = :idZona")
	//List<FormacionMatrimonio> obtenerPilarPorZona(Long idZona);

}
