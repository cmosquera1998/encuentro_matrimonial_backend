package com.encuentro.matrimonial.controller;

import com.encuentro.matrimonial.constants.Mensaje;
import com.encuentro.matrimonial.constants.ResourceMapping;
import com.encuentro.matrimonial.modelo.AuthenticationRequest;
import com.encuentro.matrimonial.modelo.AuthenticationResponse;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.repository.IUserRepository;
import com.encuentro.matrimonial.security.UserDetailService;
import com.encuentro.matrimonial.util.ErrorMessage2;
import com.encuentro.matrimonial.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
public class AuthenticationController {


	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@PostMapping(ResourceMapping.LOGIN)
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest usuario) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword()));

		} catch (Exception e) {
			return ResponseEntity.ok(new ErrorMessage2(Mensaje.CODE_UNAUTHORIZED, Mensaje.CREDENCIALES_INCORRECTAS));
		}

		final UserDetails userDetails = userDetailService.loadUserByUsername(usuario.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);

		Usuario user = userRepository.findByUser(usuario.getUsername());
		String role = (user.getRoles()).iterator().next().getName();
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt, role , user));
	}

}
