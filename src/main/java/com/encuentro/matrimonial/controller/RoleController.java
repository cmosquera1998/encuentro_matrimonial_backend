package com.encuentro.matrimonial.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.encuentro.matrimonial.constants.Mensaje;
import com.encuentro.matrimonial.constants.ResourceMapping;
import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.RoleRepository;
import com.encuentro.matrimonial.service.IUserService;
import com.encuentro.matrimonial.util.ErrorMessage;

@RestController
@RequestMapping(ResourceMapping.ROL)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
public class RoleController {

	private static final Logger log = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private IUserService userService;

	@GetMapping(value = "/getRoles", headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Role>>> getRoles(@RequestParam Long id) {
		try {
			Optional<Usuario> us = userService.findByIdUsuario(id);
			if (us.isPresent()) {
				Usuario usuario = us.get();
				List<Role> roles = (List<Role>) usuario.getRoles();
				List<Role> listadoRoles = new ArrayList<>();

				Role diosesanoRole = roleRepository.findByName("ROLE_DIOSESANO");
				Role regionalRole = roleRepository.findByName("ROLE_REGIONAL");
				Role nacionalRole = roleRepository.findByName("ROLE_NACIONAL");

				if (!roles.isEmpty()) {
					Role primerRol = roles.get(0);
					if (primerRol.getName().equals("ROLE_REGIONAL")) {
						listadoRoles.add(diosesanoRole);
					} else if (primerRol.getName().equals("ROLE_NACIONAL")) {
						listadoRoles.add(diosesanoRole);
						listadoRoles.add(regionalRole);
					} else if (primerRol.getName().equals("ROLE_ZONAL")) {
						listadoRoles.add(diosesanoRole);
						listadoRoles.add(regionalRole);
						listadoRoles.add(nacionalRole);
					} else if (primerRol.getName().equals("ROLE_LATAM")) {
						listadoRoles = roleRepository.findAll();
					}
				}
				ErrorMessage<List<Role>> lista = listadoRoles.isEmpty()
						? new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null)
						: new ErrorMessage<>(Mensaje.CODE_OK, "Lista de Roles", listadoRoles);
				return ResponseEntity.ok(lista);
			} else {
				ErrorMessage<List<Role>> error = new ErrorMessage<>(Mensaje.CODE_NOT_FOUND, Mensaje.NOT_FOUND, null);
				return ResponseEntity.ok(error);
			}
		} catch (Exception e) {
			log.info("Error {} ", e.getMessage());
			ErrorMessage<List<Role>> body = new ErrorMessage<>(Mensaje.CODE_INTERNAL_SERVER, e.getMessage(), null);
			return ResponseEntity.internalServerError().body(body);
		}
	}
}
