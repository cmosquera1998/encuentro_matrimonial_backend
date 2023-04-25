package com.encuentro.matrimonial.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro.matrimonial.constants.Mensaje;
import com.encuentro.matrimonial.constants.ResourceMapping;
import com.encuentro.matrimonial.modelo.FormacionSacerdote;
import com.encuentro.matrimonial.repository.IFormacionSacerdoteRepository;
import com.encuentro.matrimonial.service.IFormacionSacerdoteService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(ResourceMapping.FORMACION_SACERDOTE)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
@Log4j2
public class FormacionSacerdoteController {

	@Autowired
	private IFormacionSacerdoteService formacionService;
	
	@Autowired
	IFormacionSacerdoteRepository formacionDTO;

	//servicio que trae una formacion  de sacerdote
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			FormacionSacerdote formacion = formacionService.findByFormacionSacerdote(id);
			ErrorMessage<?> error = formacion == null 
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Formacion de sacerdote", formacion);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	//servicio que trae el listado de formacion  de sacerdotes
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<FormacionSacerdote>>> getAll() {
		try {
			List<FormacionSacerdote> listado = formacionService.getAll();
			ErrorMessage<List<FormacionSacerdote>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de Formacion de sacerdotes", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage body = new ErrorMessage(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}
	
	//servicio que trae el listado de formacion  de sacerdotes por pais
		@RequestMapping(value = "/getAllPais", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<FormacionSacerdote>>> getAllPais(@RequestParam Long idPais) {
			List<FormacionSacerdote> listado = formacionDTO.obtenerFormacionPorPais(idPais);
			ErrorMessage<List<FormacionSacerdote>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de Formacion de sacerdotes", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

		//servicio que trae el listado de formacion  de sacerdotes por ciudad
		@RequestMapping(value = "/getAllCiudad", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<FormacionSacerdote>>> getAllCiudad(@RequestParam Long idCiudad) {
			List<FormacionSacerdote> listado = formacionDTO.obtenerFormacionPorCiudad(idCiudad);
			ErrorMessage<List<FormacionSacerdote>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de Formacion de sacerdotes", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}
		

	//servicio que trae el listado de formacion  de sacerdotes por fecha
	@RequestMapping(value = "/getFilter", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getFilter(@RequestParam String dateString) {
		log.debug("Fecha:-" + dateString);
		try {
			List<FormacionSacerdote> listado = formacionService.findByFiltroFormacionSacerdote(dateString);
			ErrorMessage<List<FormacionSacerdote>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de Formacion de sacerdotes", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	//servicio para crear una formacion  de sacerdote
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody FormacionSacerdote formacion) {
		log.debug("DataBody:-" + formacion);
		if (formacion != null) {
			try {
				formacion.setFechaCreacion(new Date());
				formacionService.create(formacion);
				return ResponseEntity.ok().body(new ErrorMessage2(Mensaje.CODE_OK, Mensaje.CREATE_OK));
			} catch (Exception e) {
				log.error("Error:-" + e.getMessage());
				ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
				return ResponseEntity.internalServerError().body(body);
			}
		}
		return ResponseEntity.badRequest().body(new ErrorMessage2(1, Mensaje.BAD_REQUEST));
	}

	//servicio para actualizar una formacion  de sacerdote
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> update(@RequestBody FormacionSacerdote formacion) {
		log.info("DataBody:-" + formacion);
		try {
			Optional<FormacionSacerdote> pl = Optional.ofNullable(formacionService.findByFormacionSacerdote(formacion.getId()));
			if (!pl.isPresent()) {
				return ((BodyBuilder) ResponseEntity.notFound())
						.body(new ErrorMessage2(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND));
			}
			formacion.setFechaCreacion(pl.get().getFechaCreacion());
			log.debug("DataBody:-" + formacion);
			formacionService.update(formacion);
			return ResponseEntity.ok().body(new ErrorMessage2(Mensaje.CODE_OK, Mensaje.UPDATE_OK));
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	//servicio para eliminar una formacion  de sacerdote
	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> delete(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			Optional<FormacionSacerdote> fs = Optional.ofNullable(formacionService.findByFormacionSacerdote(id));
			if (!fs.isPresent()) {
				return ((BodyBuilder) ResponseEntity.notFound())
						.body(new ErrorMessage2(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND));
			}
			formacionService.delete(id);
			return ResponseEntity.ok(new ErrorMessage2(Mensaje.CODE_OK, Mensaje.DELETE_OK));
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

}
