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
import com.encuentro.matrimonial.modelo.SegundoPilar;
import com.encuentro.matrimonial.repository.ISegundoPilarRepository;
import com.encuentro.matrimonial.service.ISegundoPilarService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(ResourceMapping.SEGUNDO_PILAR)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
@Log4j2
public class SegundoPilarController {

	@Autowired
	private ISegundoPilarService pilarService;
	
	@Autowired
	ISegundoPilarRepository pilarDTO;

	// servicio que trae un matrimonio servidor del fds
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			SegundoPilar pilar = pilarService.findBySegundoPilar(id);
			ErrorMessage<?> error = pilar == null ? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", pilar);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio que trae el listado de matrimonios servidores del fds
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<SegundoPilar>>> getAll() {
		try {
			List<SegundoPilar> listado = pilarService.getAll();
			ErrorMessage<List<SegundoPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage body = new ErrorMessage(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}
	
	//servicio que trae el listado de matrimonios servidores del fds por pais
		@RequestMapping(value = "/getAllPais", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<SegundoPilar>>> getAllPais(@RequestParam Long idPais) {
			List<SegundoPilar> listado = pilarDTO.obtenerPilarPorPais(idPais);
			ErrorMessage<List<SegundoPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

		//servicio que trae el listado matrimonios servidores del fds por ciudad
		@RequestMapping(value = "/getAllCiudad", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<SegundoPilar>>> getAllCiudad(@RequestParam Long idCiudad) {
			List<SegundoPilar> listado = pilarDTO.obtenerPilarPorCiudad(idCiudad);
			ErrorMessage<List<SegundoPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

	// servicio que trae el listado de matrimonios servidores del fds por fecha
	@RequestMapping(value = "/getFilter", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getFilter(@RequestParam String dateString) {
		log.debug("Fecha:-" + dateString);
		try {
			List<SegundoPilar> listado = pilarService.findByFiltroSegundoPilar(dateString);
			ErrorMessage<List<SegundoPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio para crear un matrimonio servidor para el fds
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody SegundoPilar pilar) {
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

	// servicio para actualizar un matrimonio servidor del fds
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> update(@RequestBody SegundoPilar pilar) {
		log.info("DataBody:-" + pilar);
		try {
			Optional<SegundoPilar> pl = Optional.ofNullable(pilarService.findBySegundoPilar(pilar.getId()));
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

	// servicio para eliminar una estructura
	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> delete(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			Optional<SegundoPilar> pl = Optional.ofNullable(pilarService.findBySegundoPilar(id));
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
