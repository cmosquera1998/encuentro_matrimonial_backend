package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
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
import com.encuentro.matrimonial.modelo.GeneralResponseTotal;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.ICuartoPilarRepository;
import com.encuentro.matrimonial.service.ICuartoPilarService;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;
import com.encuentro.matrimonial.util.GeneralResponse;

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
	
	@Autowired
	private MessageSourceAccessor message;


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
	public ResponseEntity<GeneralResponse<?>> getAll(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (!us.isPresent()) {
				GeneralResponse<List<CuartoPilar>> error = new GeneralResponse<>(Mensaje.CODE_NOT_FOUND,
						Mensaje.NOT_FOUND, null, null);
				return ResponseEntity.ok(error);
			}

			Usuario usuario = us.get();
			List<Role> roles = (List<Role>) usuario.getRoles();
			List<CuartoPilar> listadoPilar = new ArrayList<>();
			List<GeneralResponseTotal> Listotal = new ArrayList<>();

			for (Role role : roles) {
				if (role.getName().equals("ROLE_DIOSESANO")) {
					listadoPilar = pilarDTO.obtenerPilarPorCiudad(usuario.getCiudad().getId());
				} else if (role.getName().equals("ROLE_REGIONAL")) {
					listadoPilar = pilarDTO.obtenerPilarPorRegionPais(usuario.getCiudad().getRegion().getId());
				} else if (role.getName().equals("ROLE_NACIONAL")) {
					listadoPilar = pilarDTO.obtenerPilarPorPais(usuario.getCiudad().getPais().getId());
				} else if (role.getName().equals("ROLE_ZONAL")) {
					listadoPilar = pilarDTO.obtenerPilarPorZona(usuario.getCiudad().getPais().getZona().getId());
				} else if (role.getName().equals("ROLE_LATAM")) {
					listadoPilar = pilarService.getAll();
				}
			}

			if (listadoPilar.isEmpty()) {
				return ResponseEntity.ok(new GeneralResponse<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null, null));
			}

			int numSPA = 0, numFDS = 0, numMV = 0, numCA = 0, numSEC = 0, numMC = 0, numSAC = 0, numRC = 0;
			for (CuartoPilar pilar : listadoPilar) {
				numSPA += pilar.getNumServidoresPostActivos();
				numFDS += pilar.getNumFdsPostPeriodo();
				numMV += pilar.getNumMatrimonioVivieron();
				numCA += pilar.getNumComunidadApoyo();
				numSEC += pilar.getNumServiciosComunidad();
				numMC += pilar.getNumMatrimoiosComunidad();
				numSAC += pilar.getNumSacerdotesComunidad();
				numRC += pilar.getNumReligiososComunidad();
			}
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numServidoresPostActivos"), numSPA));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numFdsPostPeriodo"), numFDS));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numMatrimonioVivieron"), numMV));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numComunidadApoyo"), numCA));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numServiciosComunidad"), numSEC));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numMatrimoiosComunidad"), numMC));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numSacerdotesComunidad"), numSAC));
			Listotal.add(new GeneralResponseTotal(message.getMessage("cuartoPilar.numReligiososComunidad"), numRC));

			return ResponseEntity
					.ok(new GeneralResponse<>(Mensaje.CODE_OK, "Lista de pilares", listadoPilar, Listotal));

		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			GeneralResponse<List<CuartoPilar>> body = new GeneralResponse<>(Mensaje.CODE_INTERNAL_SERVER,
					e.getMessage(), null, null);
			return ResponseEntity.internalServerError().body(body);
		}
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
