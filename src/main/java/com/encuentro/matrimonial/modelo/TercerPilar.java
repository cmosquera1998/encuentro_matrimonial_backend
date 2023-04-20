package com.encuentro.matrimonial.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "tercer_pilar")
public class TercerPilar {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private Date fechaCreacion;

	@Column
	private int numRegiones;

	@Column
	private int numDiocesisEstablecidas;

	@Column
	private int numDiocesisContacto;

	@Column
	private int numDiocesisExpansion;
	
	@Column
	private int numDiocesisEclisiastica;

}
