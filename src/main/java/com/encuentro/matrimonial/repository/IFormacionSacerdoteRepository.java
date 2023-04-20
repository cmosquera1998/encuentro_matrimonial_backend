package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.FormacionSacerdote;

public interface IFormacionSacerdoteRepository extends CrudRepository<FormacionSacerdote, Long> {

	@Query("Select f FROM FormacionSacerdote f WHERE f.id = :id")
	FormacionSacerdote findByFormacionSacerdote(@Param("id") Long id);

	@Query("Select f FROM FormacionSacerdote f WHERE f.fechaCreacion = :fecha")
	FormacionSacerdote findByFiltroFormacionSacerdote(@Param("fecha") String fecha);

}
