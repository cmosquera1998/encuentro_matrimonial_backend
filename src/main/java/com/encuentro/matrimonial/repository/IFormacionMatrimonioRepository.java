package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.FormacionMatrimonio;

public interface IFormacionMatrimonioRepository extends CrudRepository<FormacionMatrimonio, Long> {

	@Query("Select f FROM FormacionMatrimonio f WHERE f.id = :id")
	FormacionMatrimonio findByFormacionMatrimonio(@Param("id") Long id);

	@Query("Select f FROM FormacionMatrimonio f WHERE f.fechaCreacion = :fecha")
	FormacionMatrimonio findByFiltroFormacionMatrimonio(@Param("fecha") String fecha);

}
