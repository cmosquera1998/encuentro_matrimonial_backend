package com.encuentro.matrimonial.modelo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
@Entity(name = "segundo_pilar")
public class SegundoPilar {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private Date fechaCreacion;

	@Column
	private int numMatrimosServidoresActivos;

	@Column
	private int numSacerdotesServidoresActivos;

	@Column
	private int numMatrimosServidoresProfundoActivos;

	@Column
	private int numSacerdotesServidoresprofundoActivos;

	@Column
	private int numFdsProfundosPeriodo;

	@Column
	private int numMatrimosVivieronProfundo;

	@Column
	private int numSacerdotesVivieronProfundo;

	@Column
	private int numMatrimosDebutaronProfundo;

	@Column
	private int numSacerdotesDebutaronProfundo;
	
	@ManyToOne
	@JoinColumn(name = "ciudad_id")
	private Ciudad ciudad;
}
