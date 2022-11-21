package com.encuentro.matrimonial.service;

import java.util.List;

import com.encuentro.matrimonial.modelo.PrimerPilar;

public interface IPrimerPilarService {

	void create(PrimerPilar pilar);

	List<PrimerPilar> getAll();

	PrimerPilar findByPrimerPilar(Long id);

	List<PrimerPilar> findByFiltroPrimerPilar(String fecha);

	PrimerPilar update(PrimerPilar pilar);

	void delete(Long id);

}
