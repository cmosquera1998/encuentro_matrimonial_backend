package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro.matrimonial.constants.Mensaje;
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

	@RequestMapping(value = "/getPaises", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Pais>>> getPaises(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (us.isPresent()) {
				Usuario usuario = us.get();
				List<Role> roles = (List<Role>) usuario.getRoles();
				List<Pais> listadoPises = new ArrayList<Pais>();
				if (!roles.isEmpty()) {
					Role primerRol = roles.get(0);
					if (primerRol.getName().equals("ROLE_LATAM")) {
						listadoPises = paisRepository.findAll();
					} else {
						listadoPises = paisRepository.findById(usuario.getCiudad().getPais().getId());
					}
				}

				ErrorMessage<List<Pais>> lista = listadoPises.isEmpty()
						? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
						: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de Paises", listadoPises);
				return ResponseEntity.ok(lista);
			} else {
				ErrorMessage<List<Pais>> error = new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null);
				return ResponseEntity.ok(error);
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			ErrorMessage<List<Pais>> body = new ErrorMessage<>(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}

	@RequestMapping(value = "/getCiudadPaises", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Ciudad>>> getCiudadPaises(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (us.isPresent()) {
				Usuario usuario = us.get();
				List<Role> roles = (List<Role>) usuario.getRoles();
				List<Ciudad> listadoCiudades = new ArrayList<Ciudad>();
				if (!roles.isEmpty()) {
					Role primerRol = roles.get(0);
					if (primerRol.getName().equals("ROLE_NACIONAL")) {
						listadoCiudades = ciudadRepository
								.obtenerCiudadesPorPais(usuario.getCiudad().getPais().getId());
					} else if (primerRol.getName().equals("ROLE_LATAM")) {
						listadoCiudades = (List<Ciudad>) ciudadRepository.findAll();
					} else {
						listadoCiudades = ciudadRepository.obtenerCiudadUsuario(usuario.getCiudad().getId());
					}
				}
				ErrorMessage<List<Ciudad>> lista = listadoCiudades.isEmpty()
						? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
						: new ErrorMessage<>(0, "Lista de ciudades", listadoCiudades);

				return ResponseEntity.ok(lista);
			} else {
				ErrorMessage<List<Ciudad>> error = new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null);
				return ResponseEntity.ok(error);
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			ErrorMessage<List<Ciudad>> body = new ErrorMessage<>(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}
}
