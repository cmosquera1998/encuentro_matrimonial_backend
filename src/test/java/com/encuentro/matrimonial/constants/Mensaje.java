package com.encuentro.matrimonial.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mensaje {

	public static final String NOT_FOUND = "No se ha encontrado información";
	public static final String BAD_REQUEST = "información incompleta";
	public static final String INTERNAL_SERVER = "Se ha generado un error interno";
	public static final String CREATE_OK = "Creacion exitosa!";
	public static final String UPDATE_OK = "Actualizacion exitosa";
	public static final String DELETE_OK = "Registro eliminado exitosamente!";
	public static final String CREDENCIALES_INCORRECTAS ="Credenciales incorrectas";

	public static final int CODE_OK = 200;
	public static final int CODE_NOT_FOUND = 404;
	public static final int CODE_BAD_REQUEST = 400;
	public static final int CODE_UNAUTHORIZED = 401;
	public static final int CODE_INTERNAL_SERVER = 500;

}
