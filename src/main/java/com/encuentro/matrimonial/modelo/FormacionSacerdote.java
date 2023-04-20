package com.encuentro.matrimonial.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name = "formacion_sacerdote")
public class FormacionSacerdote {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private Date fechaCreacion;

	@Column
	private int jornadaDialogo;

	@Column
	private int retornoEspiritual;

	@Column
	private int lenguajeAmor;

	@Column
	private int guiaDeRelacion;

	@Column
	private int sacramento;

	@Column
	private int diosEnSacramento;

	@Column
	private int diosEnVida;

	@Column
	private int patronesComportamiento;

	@Column
	private int dialogoProfundo;

	@Column
	private int servidoresPostEncuentro;

	@Column
	private int formacionAcompanantes;

	@Column
	private int padreNuestro;

	@Column
	private int transmisionNacional;

}
