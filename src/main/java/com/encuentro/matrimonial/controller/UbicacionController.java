package com.encuentro.matrimonial.controller;

import java.util.List;

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
	public ResponseEntity<ErrorMessage<List<Pais>>> getPaises() {
		List<Pais> listado = paisRepository.findAll();
		ErrorMessage<List<Pais>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de Paises", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getCiudadPaises", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Ciudad>>> getCiudadPaises(@RequestParam Long idPais) {
		List<Ciudad> listado = ciudadRepository.obtenerCiudadesPorPais(idPais);
		ErrorMessage<List<Ciudad>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de ciudades", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getCiudades", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Ciudad>>> getCiudades() {
		List<Ciudad> listado = (List<Ciudad>) ciudadRepository.findAll();
		ErrorMessage<List<Ciudad>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de ciudades", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	
}
