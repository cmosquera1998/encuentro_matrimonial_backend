package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro.matrimonial.constants.ResourceMapping;
import com.encuentro.matrimonial.modelo.Ciudad;
import com.encuentro.matrimonial.modelo.Pais;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.CiudadRepository;
import com.encuentro.matrimonial.repository.PaisRepository;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;

@RestController
@RequestMapping(ResourceMapping.UBICACION)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
public class UbicacionController {

	private static final Logger log = LoggerFactory.getLogger(UbicacionController.class);

	@Autowired
	private CiudadRepository ciudadRepository;

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private IUserService userService;

	private List<Pais> listadoPises = new ArrayList<Pais>();

	private List<Ciudad> listadoCiudades = new ArrayList<Ciudad>();

	@RequestMapping(value = "/getPaises", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Pais>>> getPaises(@RequestParam Long id) {
		Optional<Usuario> us = userService.findByIdUsuario(id);
		us.ifPresent(usuario -> {
			List<Role> roles = (List<Role>) usuario.getRoles();
			if (!roles.isEmpty()) {
				Role primerRol = roles.get(0);
				if (primerRol.getName().equals("ROLE_LATAM")) {
					listadoPises = paisRepository.findAll();
				} else {
					listadoPises = paisRepository.findById(usuario.getCiudad().getPais().getId());
				}
			}
		});
		ErrorMessage<List<Pais>> error = listadoPises.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de Paises", listadoPises);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@RequestMapping(value = "/getCiudadPaises", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Ciudad>>> getCiudadPaises(@RequestParam Long id) {
		Optional<Usuario> us = userService.findByIdUsuario(id);
		us.ifPresent(usuario -> {
			List<Role> roles = (List<Role>) usuario.getRoles();
			if (!roles.isEmpty()) {
				Role primerRol = roles.get(0);
				if (primerRol.getName().equals("ROLE_ADMIN")) {
					listadoCiudades = ciudadRepository.obtenerCiudadesPorPais(usuario.getCiudad().getPais().getId());
				} else if (primerRol.getName().equals("ROLE_LATAM")) {
					listadoCiudades = (List<Ciudad>) ciudadRepository.findAll();
				} else {
					listadoCiudades = ciudadRepository.obtenerCiudadUsuario(usuario.getCiudad().getId());
				}
			}
		});
		ErrorMessage<List<Ciudad>> error = listadoCiudades.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de ciudades", listadoCiudades);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

}
