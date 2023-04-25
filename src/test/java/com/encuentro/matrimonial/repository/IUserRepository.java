package com.encuentro.matrimonial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.encuentro.matrimonial.modelo.Usuario;

public interface IUserRepository extends CrudRepository<Usuario, Long> {

	@Query("Select u FROM Usuario u WHERE u.username = :user")
	Usuario findByUser(@Param("user") String user);

	@Query("Select u FROM Usuario u WHERE u.document = :user")
	Usuario findByDocumento(@Param("user") String user);

	@Query("Select u FROM Usuario u WHERE u.username = :user AND u.password = :pss")
	Usuario findByLogin(@Param("user") String user, @Param("user") String pss);

	@Query("Select u FROM Usuario u WHERE u.id = :user")
	Usuario findBy(@Param("user") Long user);

	@Query(value = "SELECT u FROM Usuario u WHERE u.ciudad.pais.id = :idPais")
	List<Usuario> obtenerUsuariosPorPais(Long idPais);

	@Query(value = "SELECT u FROM Usuario u WHERE u.ciudad.id = :idCiudad")
	List<Usuario> obtenerUsuariosPorCiudad(Long idCiudad);

}
