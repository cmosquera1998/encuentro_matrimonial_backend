package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
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
import com.encuentro.matrimonial.modelo.FormacionMatrimonio;
import com.encuentro.matrimonial.modelo.GeneralResponseTotal;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.IFormacionMatrimonioRepository;
import com.encuentro.matrimonial.service.IFormacionMatrimonioService;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;
import com.encuentro.matrimonial.util.GeneralResponse;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(ResourceMapping.FORMACION_MATRIMONIO)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
@Log4j2
public class FormacionMatrimonioController {

	@Autowired
	private IFormacionMatrimonioService formacionService;

	@Autowired
	IFormacionMatrimonioRepository formacionDTO;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private MessageSourceAccessor message;

	// servicio que trae una formacion de matrimonio
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			FormacionMatrimonio formacion = formacionService.findByFormacionMatrimonio(id);
			ErrorMessage<?> error = formacion == null
					? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Formacion de matrimonio", formacion);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio que trae el listado de formacion de matrimonios
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<GeneralResponse<?>> getAll(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (!us.isPresent()) {
				GeneralResponse<List<FormacionMatrimonio>> error = new GeneralResponse<>(Mensaje.CODE_NOT_FOUND,
						Mensaje.NOT_FOUND, null, null);
				return ResponseEntity.ok(error);
			}

			Usuario usuario = us.get();
			List<Role> roles = (List<Role>) usuario.getRoles();
			List<FormacionMatrimonio> listadoFormacion = new ArrayList<>();
			List<GeneralResponseTotal> Listotal = new ArrayList<>();

			for (Role role : roles) {
				if (role.getName().equals("ROLE_DIOSESANO")) {
					listadoFormacion = formacionDTO.obtenerFormacionPorCiudad(usuario.getCiudad().getId());
				} else if (role.getName().equals("ROLE_REGIONAL")) {
					listadoFormacion = formacionDTO.obtenerFormacionPorRegionPais(usuario.getCiudad().getRegion().getId());
				} else if (role.getName().equals("ROLE_NACIONAL")) {
					listadoFormacion = formacionDTO.obtenerFormacionPorPais(usuario.getCiudad().getPais().getId());
				} else if (role.getName().equals("ROLE_ZONAL")) {
					listadoFormacion = formacionDTO.obtenerFormacionPorZona(usuario.getCiudad().getPais().getZona().getId());
				} else if (role.getName().equals("ROLE_LATAM")) {
					listadoFormacion = formacionService.getAll();
				}
			}

			if (listadoFormacion.isEmpty()) {
				return ResponseEntity.ok(new GeneralResponse<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null, null));
			}

			int JD = 0, RE = 0, LA = 0, GT = 0, S = 0, DNS = 0, DNV = 0, PC = 0, DP = 0, SP = 0, FA = 0, PN = 0, TN = 0;
			for (FormacionMatrimonio formacion : listadoFormacion) {
				JD += formacion.getJornadaDialogo();
				RE += formacion.getRetornoEspiritual();
				LA += formacion.getLenguajeAmor();
				GT += formacion.getGuiaDeRelacion();
				S += formacion.getSacramento();
				DNS += formacion.getDiosEnSacramento();
				DNV += formacion.getDiosEnVida();
				PC += formacion.getPatronesComportamiento();
				DP += formacion.getDialogoProfundo();
				SP += formacion.getServidoresPostEncuentro();
				FA += formacion.getFormacionAcompanantes();
				PN += formacion.getPadreNuestro();
				TN += formacion.getTransmisionNacional();
			}
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.jornadaDialogo"), JD));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.retornoEspiritual"), RE));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.lenguajeAmor"), LA));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.guiaDeRelacion"), GT));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.sacramento"), S));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.diosEnSacramento"), DNS));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.diosEnVida"), DNV));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.patronesComportamiento"), PC));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.dialogoProfundo"), DP));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.servidoresPostEncuentro"), SP));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.formacionAcompanantes"), FA));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.padreNuestro"), PN));
			Listotal.add(new GeneralResponseTotal(message.getMessage("formacion.transmisionNacional"), TN));

			return ResponseEntity
					.ok(new GeneralResponse<>(Mensaje.CODE_OK, "Lista de Formacion de matrimonios", listadoFormacion, Listotal));

		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			GeneralResponse<List<FormacionMatrimonio>> body = new GeneralResponse<>(Mensaje.CODE_INTERNAL_SERVER,
					e.getMessage(), null, null);
			return ResponseEntity.internalServerError().body(body);
		}
	}


	// servicio para crear una formacion de matrimonio
	@RequestMapping(value = "/create", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> create(@RequestBody FormacionMatrimonio formacion) {
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

	// servicio para actualizar una formacion de matrimonio
	@RequestMapping(value = "/update", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> update(@RequestBody FormacionMatrimonio formacion) {
		log.info("DataBody:-" + formacion);
		try {
			Optional<FormacionMatrimonio> pl = Optional
					.ofNullable(formacionService.findByFormacionMatrimonio(formacion.getId()));
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

	// servicio para eliminar una formacion de matrimonio
	@RequestMapping(value = "/delete", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> delete(@RequestParam Long id) {
		log.debug("Id:-" + id);
		try {
			Optional<FormacionMatrimonio> fs = Optional.ofNullable(formacionService.findByFormacionMatrimonio(id));
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
