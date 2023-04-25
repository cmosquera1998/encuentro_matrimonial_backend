package com.encuentro.matrimonial.modelo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthenticationResponse {

	private String jwt;
	private final String role;
	private final Usuario usuario;

}
