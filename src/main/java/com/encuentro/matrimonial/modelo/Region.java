package com.encuentro.matrimonial.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "region")
public class Region {

	@Id
	@Column
	private Long id;

	@Column
	private String name;

	/*@ManyToOne
	@JoinColumn(name = "pais_id")
	private Pais pais;*/

	/*@OneToMany
	@JoinColumn(name = "ciudad_id")
	private List<Ciudad> ciudad;*/
}