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
import com.encuentro.matrimonial.modelo.CuartoPilar;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.ICuartoPilarRepository;
import com.encuentro.matrimonial.service.ICuartoPilarService;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(ResourceMapping.CUARTO_PILAR)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
@Log4j2
public class CuartoPilarController {

	@Autowired
	private ICuartoPilarService pilarService;
	
	@Autowired
	ICuartoPilarRepository pilarDTO;
	
	@Autowired
	private IUserService userService;
	
	// servicio que trae el post encuentro
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			CuartoPilar pilar = pilarService.findByCuartoPilar(id);
			ErrorMessage<?> error = pilar == null ? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", pilar);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio que trae el listado de post encuentro
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<CuartoPilar>>> getAll(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (us.isPresent()) {
				Usuario usuario = us.get();
				List<Role> roles = (List<Role>) usuario.getRoles();
				List<CuartoPilar> listadoPilar = new ArrayList<CuartoPilar>();
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

				ErrorMessage<List<CuartoPilar>> lista = listadoPilar.isEmpty()
						? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
						: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de pilares ", listadoPilar);
				return ResponseEntity.ok().body(lista);
			} else {
				ErrorMessage<List<CuartoPilar>> error = new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND,
						null);
				return ResponseEntity.ok(error);
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			ErrorMessage<List<CuartoPilar>> body = new ErrorMessage<>(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(),
					null);
			return ResponseEntity.internalServerError().body(body);
		}
	}

		
		// servicio que trae el listado de de post encuentro por zona
		@RequestMapping(value = "/getAllZona", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<CuartoPilar>>> getAllZona(@RequestParam Long idZona) {
			List<CuartoPilar> listado = pilarDTO.obtenerPilarPorZona(idZona);
			ErrorMessage<List<CuartoPilar>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de pilares por zona", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

	
	// servicio para crear un post encuentro
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody CuartoPilar pilar) {
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

	// servicio para actualizar un post encuentro
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> update(@RequestBody CuartoPilar pilar) {
		log.info("DataBody:-" + pilar);
		try {
			Optional<CuartoPilar> pl = Optional.ofNullable(pilarService.findByCuartoPilar(pilar.getId()));
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
			Optional<CuartoPilar> pl = Optional.ofNullable(pilarService.findByCuartoPilar(id));
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
