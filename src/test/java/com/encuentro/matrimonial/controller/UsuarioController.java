package com.encuentro.matrimonial.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.IUserRepository;
import com.encuentro.matrimonial.security.BCryptPasswordEncoder;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;

@RestController
@RequestMapping(ResourceMapping.USER)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
public class UsuarioController {
 /*
  * Tener encuenta la logica del rol principal , para realizar la gestion de usuarios
  * Evaluar rol principal
  * pais , departamento , zona  y lo que se requiera 
  * 
  * */
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IUserRepository userDao;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// servicio que trae un usuario 
		@RequestMapping(value = "/get", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<?> get(@RequestParam Long id) {
			log.debug("Id:-" + id);
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
	public ResponseEntity<ErrorMessage<List<Usuario>>> getUser() {
		List<Usuario> listado = userService.getUsuarios();
		ErrorMessage<List<Usuario>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de Usuarios", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}
	
	    //servicio que trae el listado de usuarios por pais 
		@RequestMapping(value = "/getUsuariosPais", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<Usuario>>> getUserPais(@RequestParam Long idPais) {
			List<Usuario> listado = userDao.obtenerUsuariosPorPais(idPais);
			ErrorMessage<List<Usuario>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de Usuarios", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}
		
		//servicio que trae el listado de usuarios por ciudad 
		@RequestMapping(value = "/getUsuariosCiudad", method = RequestMethod.GET, headers = "Accept=application/json")
		public ResponseEntity<ErrorMessage<List<Usuario>>> getUserCiudad(@RequestParam Long idCiudad) {
			List<Usuario> listado = userDao.obtenerUsuariosPorCiudad(idCiudad);
			ErrorMessage<List<Usuario>> error = listado.isEmpty()
					? new ErrorMessage<>(1, "No se ha encontrado información", null)
					: new ErrorMessage<>(0, "Lista de Usuarios", listado);
			return new ResponseEntity<>(error, HttpStatus.OK);
		}

	// servicio para crear un usuario
	@RequestMapping(value = "/createUsuario", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> createUser(@RequestBody Usuario user) {
		Usuario user2 = userService.findByUser(user.getUsername());
		Usuario user3 = userService.findByDocumento(user.getDocument());
		if (user2 != null || user3 != null) {
			return new ResponseEntity(new ErrorMessage2(1, "El usuario ya se encuentra registrado"), HttpStatus.OK);
		}

		if (user.getName().isEmpty() || user.getLastname().isEmpty()) {
			return new ResponseEntity(new ErrorMessage2(2, "información incompleta"), HttpStatus.OK);
		}
		user.setState(true);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setCreationDate(new Date());
		userService.createUsuario(user);
		return new ResponseEntity(new ErrorMessage2(0, "Usuario creado con exito!"), HttpStatus.OK);
	}

	// servicio para actualizar un usuario
	@RequestMapping(value = "/updateUsuario", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> updateUsuario(@RequestBody Usuario user) {
		Optional<Usuario> us = userService.findByIdUsuario(user.getId());
		if (!us.isPresent()) {
			return new ResponseEntity(new ErrorMessage2(1, "No sea encontrado el usuario"), HttpStatus.OK);
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
