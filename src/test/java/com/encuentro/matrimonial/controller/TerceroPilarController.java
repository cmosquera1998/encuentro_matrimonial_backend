package com.encuentro.matrimonial.controller;

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
import com.encuentro.matrimonial.modelo.TercerPilar;
import com.encuentro.matrimonial.repository.ITercerPilarRepository;
import com.encuentro.matrimonial.service.ITercerPilarService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(ResourceMapping.TERCERO_PILAR)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
@Log4j2
public class TerceroPilarController {

	@Autowired
	private ITercerPilarService pilarService;
	
	@Autowired
	ITercerPilarRepository pilarDTO;

	// servicio que trae el una estructura
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			TercerPilar pilar = pilarService.findByTercerPilar(id);
			ErrorMessage<?> error = pilar == null ? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", pilar);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio que trae el listado de estructuras
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<TercerPilar>>> getAll() {
		try {
			List<TercerPilar> listado = pilarService.getAll();
			ErrorMessage<List<TercerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage body = new ErrorMessage(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}
	
	//servicio que trae el listado de estructuras por pais
	@RequestMapping(value = "/getAllPais", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<TercerPilar>>> getAllPais(@RequestParam Long idPais) {
		List<TercerPilar> listado = pilarDTO.obtenerPilarPorPais(idPais);
		ErrorMessage<List<TercerPilar>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de pilares", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	//servicio que trae el listado de estructuras por ciudad
	@RequestMapping(value = "/getAllCiudad", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<TercerPilar>>> getAllCiudad(@RequestParam Long idCiudad) {
		List<TercerPilar> listado = pilarDTO.obtenerPilarPorCiudad(idCiudad);
		ErrorMessage<List<TercerPilar>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de pilares", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	// servicio que trae el listado de estructuras por fecha
	@RequestMapping(value = "/getFilter", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getFilter(@RequestParam String dateString) {
		log.debug("Fecha:-" + dateString);
		try {
			List<TercerPilar> listado = pilarService.findByFiltroTercerPilar(dateString);
			ErrorMessage<List<TercerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio para crear una estructura
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody TercerPilar pilar) {
		log.debug("DataBody:-" + pilar);
		if (pilar != null) {
			try {
				pilarService.create(pilar);
				return ResponseEntity.ok().body(new ErrorMessage2(Mensaje.CODE_OK, Mensaje.CREATE_OK));
			} catch (Exception e) {
				log.error("Error:-" + e.getMessage());
				ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
				return ResponseEntity.internalServerError().body(body);
			}
		}
		return ResponseEntity.badRequest().body(new ErrorMessage2(1, Mensaje.BAD_REQUEST));
	}

	// servicio para actualizar una estructura
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> update(@RequestBody TercerPilar pilar) {
		log.info("DataBody:-" + pilar);
		try {
			Optional<TercerPilar> pl = Optional.ofNullable(pilarService.findByTercerPilar(pilar.getId()));
			if (!pl.isPresent()) {
				return ((BodyBuilder) ResponseEntity.notFound())
						.body(new ErrorMessage2(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND));
			}
			pilar.setFechaCreacion(pl.get().getFechaCreacion());
			log.debug("DataBody:-" + pilar);
			pilarService.update(pilar);
			return ResponseEntity.ok().body(new ErrorMessage2(Mensaje.CODE_OK, Mensaje.UPDATE_OK));
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio para eliminar un fin de semanqa
	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> delete(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			Optional<TercerPilar> pl = Optional.ofNullable(pilarService.findByTercerPilar(id));
			if (!pl.isPresent()) {
				return ((BodyBuilder) ResponseEntity.notFound())
						.body(new ErrorMessage2(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND));
			}
			pilarService.delete(id);
			return ResponseEntity.ok(new ErrorMessage2(Mensaje.CODE_OK, Mensaje.DELETE_OK));
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

}
