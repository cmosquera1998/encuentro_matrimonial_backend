package com.encuentro.matrimonial.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ciudad")
public class Ciudad {

	@Id
	@Column
	private Long id;

	@Column
	private String name;

	@ManyToOne
	@JoinColumn(name = "pais_id")
	private Pais pais;

}