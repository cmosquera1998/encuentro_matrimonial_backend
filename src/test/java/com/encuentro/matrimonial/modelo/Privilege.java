package com.encuentro.matrimonial.modelo;

import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
@Table(name = "privilege")
public class Privilege {

	@Id
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;

}
