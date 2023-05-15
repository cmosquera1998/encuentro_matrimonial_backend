package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.Zona;

public interface IZonaRepository extends CrudRepository<Zona, Long> {

	@Query("Select z FROM Zona z WHERE z.id = :id")
	Zona findByID(@Param("id") Long id);

}
