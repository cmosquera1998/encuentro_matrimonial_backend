package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro.matrimonial.constants.Mensaje;
import com.encuentro.matrimonial.constants.ResourceMapping;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.IUserRepository;
import com.encuentro.matrimonial.security.BCryptPasswordEncoder;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(ResourceMapping.USER)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
public class UsuarioController {

	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserRepository userDao;

	ObjectMapper mapper = new ObjectMapper();

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// servicio que trae un usuario
	@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<?> get(@RequestParam Long id) {
		log.debug("Id a consultar:-" + id);
		try {
			Usuario user = userService.findBy(id);
			ErrorMessage<?> error = user == null ? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
					: new ErrorMessage<>(Mensaje.CODE_OK, "Usuario", user);
			return ResponseEntity.ok().body(error);
		} catch (Exception e) {
			log.error("Error:-" + e.getMessage());
			ErrorMessage2 body = new ErrorMessage2(Mensaje.CODE_INTERNAL_SERVER, e.getMessage());
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio que trae el listado de usuarios
	@RequestMapping(value = "/getUsuarios", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Usuario>>> getUser(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (us.isPresent()) {
				Usuario usuario = us.get();
				List<Role> roles = (List<Role>) usuario.getRoles();
				List<Usuario> listadoUser = new ArrayList<Usuario>();
				if (!roles.isEmpty()) {
					Role primerRol = roles.get(0);
					if (primerRol.getName().equals("ROLE_NACIONAL")) {
						listadoUser = userDao.obtenerUsuariosPorPais(usuario.getCiudad().getPais().getId());
					} else if (primerRol.getName().equals("ROLE_LATAM")) {
						listadoUser = userService.getUsuarios();
					} else {
						listadoUser = userDao.obtenerUsuariosPorCiudad(usuario.getCiudad().getId());
					}
				}
				ErrorMessage<List<Usuario>> lista = listadoUser.isEmpty()
						? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
						: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de Usuarios", listadoUser);
				return ResponseEntity.ok(lista);
			} else {
				ErrorMessage<List<Usuario>> error = new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null);
				return ResponseEntity.ok(error);
			}
		} catch (Exception e) {
			log.error("Error: " + e.getMessage());
			ErrorMessage<List<Usuario>> body = new ErrorMessage<>(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}

	// servicio para crear un usuario
	@RequestMapping(value = "/createUsuario", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage2> createUser(@Valid @RequestBody Usuario user) throws JsonProcessingException {
		String request = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
		log.info("Request createUsuario {}", request);
		Usuario user1 = userService.findByUser(user.getUsername());
		Usuario user2 = userService.findByDocumento(user.getDocument());
		if (user1 != null || user2 != null) {
			return ResponseEntity.ok(new ErrorMessage2(1, "El usuario ya se encuentra registrado"));
		}
		user.setState(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setCreationDate(new Date());
		try {
			userService.createUsuario(user);
			return ResponseEntity.ok(new ErrorMessage2(0, "Usuario creado con éxito!"));
		} catch (DataAccessException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ErrorMessage2(3, "Ocurrió un error al crear el usuario"));
		}
	}

	// servicio para actualizar un usuario
	@RequestMapping(value = "/updateUsuario", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> updateUsuario(@Valid @RequestBody Usuario user) {
		Optional<Usuario> us = userService.findByIdUsuario(user.getId());
		if (!us.isPresent()) {
			return new ResponseEntity(new ErrorMessage2(1, "No sea encontrado el usuario"), HttpStatus.OK);
		}
		if (!us.get().getPassword().equals(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		userService.updateUsuario(user);
		return new ResponseEntity(new ErrorMessage2(0, "Usuario actualizado con exito!"), HttpStatus.OK);
	}

	// servicio para desactivar un usuario
	@RequestMapping(value = "/desactivarUsuario", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> desactivarUsuario(@RequestParam Long id) {
		Usuario us = userService.findBy(id);
		System.out.println(us);
		if (us == null) {
			return new ResponseEntity(new ErrorMessage2(1, "No sea encontrado el usuario"), HttpStatus.OK);
		}
		if (us.getState() == true) {
			us.setState(false);
		}
		userService.updateUsuario(us);
		return new ResponseEntity(new ErrorMessage2(0, "Usuario desactivado con exito!"), HttpStatus.OK);
	}

	// servicio para eliminar un usuario
	@RequestMapping(value = "/deleteUsuario", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> deleteUsuario(@RequestParam Long id) {
		Usuario us = userService.findBy(id);
		System.out.println(us);
		if (us == null) {
			return new ResponseEntity(new ErrorMessage2(1, "No sea encontrado el usuario"), HttpStatus.OK);
		}
		userService.deleteUsuario(id);
		return new ResponseEntity(new ErrorMessage2(0, "Usuario eliminado con exito!"), HttpStatus.OK);
	}

}
