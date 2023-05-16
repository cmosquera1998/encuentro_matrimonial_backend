package com.encuentro.matrimonial.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<E> {

	private int code;
	private String mensaje;
	private E response;
	private E TotalResponse;

}
