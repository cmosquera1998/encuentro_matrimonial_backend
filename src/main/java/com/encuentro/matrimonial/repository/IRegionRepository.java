package com.encuentro.matrimonial.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.Region;

public interface IRegionRepository extends CrudRepository<Region, Long> {

	@Query("Select r FROM Region r WHERE r.id = :id")
	Region findByID(@Param("id") Long id);

}
