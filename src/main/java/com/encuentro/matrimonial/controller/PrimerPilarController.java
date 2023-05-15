package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
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
import com.encuentro.matrimonial.modelo.PrimerPilar;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.IPrimerPilarRepository;
import com.encuentro.matrimonial.service.IPrimerPilarService;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;


@RestController
@RequestMapping(ResourceMapping.PRIMER_PILAR)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
@Log4j2
public class PrimerPilarController {

	@Autowired
	private IPrimerPilarService pilarService;
	
	@Autowired
	IPrimerPilarRepository pilarDTO;
	
	@Autowired
        private IPrimerPilarService pilarService;
	
	private List<PrimerPilar> listadoPilar = new ArrayList<PrimerPilar>();

	// servicio que trae un fin de semana
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			PrimerPilar pilar = pilarService.findByPrimerPilar(id);
			ErrorMessage<?> error = pilar == null ? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", pilar);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio que trae el listado de fines de semana
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<PrimerPilar>>> getAll() {
		try {
			List<PrimerPilar> listado = pilarService.getAll();
			ErrorMessage<List<PrimerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage body = new ErrorMessage(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}
	
	//servicio que trae el listado de fines de semana por pais
		@RequestMapping(value = "/getAllPais", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<PrimerPilar>>> getAllPais(@RequestParam Long idPais) {
			List<PrimerPilar> listado = pilarDTO.obtenerPilarPorPais(idPais);
			ErrorMessage<List<PrimerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares pais", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

		//servicio que trae el listado de fines de semana por ciudad
		@RequestMapping(value = "/getAllCiudad", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<PrimerPilar>>> getAllCiudad(@RequestParam Long idCiudad) {
			List<PrimerPilar> listado = pilarDTO.obtenerPilarPorCiudad(idCiudad);
			ErrorMessage<List<PrimerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares por ciudad", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}
		
		// servicio que trae el listado de fines de semana por zona pais
		@RequestMapping(value = "/getAllZonaPais", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<PrimerPilar>>> getAllZonaPais(@RequestParam Long idZona) {
			List<PrimerPilar> listado = pilarDTO.obtenerPilarPorZonaPais(idZona);
			ErrorMessage<List<PrimerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares por zona en el pais", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}
		
		// servicio que trae el listado de fines de semana por zona
		@RequestMapping(value = "/getAllZonaLatam", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<PrimerPilar>>> getAllZonaLatam(@RequestParam Long idZona) {
			List<PrimerPilar> listado = pilarDTO.obtenerPilarPorZonaLatam(idZona);
			System.out.println("validate fecha:-->" + listado.get(0).getFechaCreacion());
			System.out.println("validate id:-->" + listado.get(0).getId());
			ErrorMessage<List<PrimerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares por zona en latam", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

	// servicio que trae el listado de fines de semana por fecha
	@RequestMapping(value = "/getFilter", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> getFilter(@RequestParam String dateString) {
		log.debug("Fecha:-" + dateString);
		try {
			List<PrimerPilar> listado = pilarService.findByFiltroPrimerPilar(dateString);
			ErrorMessage<List<PrimerPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listado);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio para crear un fin de semana
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody PrimerPilar pilar) {
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

	// servicio para actualizar un fin de semana
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> update(@RequestBody PrimerPilar pilar) {
		log.info("DataBody:-" + pilar);
		try {
			Optional<PrimerPilar> pl = Optional.ofNullable(pilarService.findByPrimerPilar(pilar.getId()));
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
			Optional<PrimerPilar> pl = Optional.ofNullable(pilarService.findByPrimerPilar(id));
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
// servicio que trae el listado de fines de semana
	@RequestMapping(value = "/getAllPrueba", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<PrimerPilar>>> getAllPrueba(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			us.ifPresent(usuario -> {
				List<Role> roles = (List<Role>) usuario.getRoles();
				if (!roles.isEmpty()) {
					Role primerRol = roles.get(0);
					if (primerRol.getName().equals("ROLE_ADMIN")) {
						listadoPilar = pilarDTO.obtenerPilarPorPais(usuario.getCiudad().getPais().getId());
					} else if (primerRol.getName().equals("ROLE_LATAM")) {
						listadoPilar = pilarService.getAll();
					} else {
						listadoPilar = pilarDTO.obtenerPilarPorCiudad(usuario.getCiudad().getId());
					}
				}
			});
			ErrorMessage<List<PrimerPilar>> error = listadoPilar.isEmpty()
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listadoPilar);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage body = new ErrorMessage(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}

}
