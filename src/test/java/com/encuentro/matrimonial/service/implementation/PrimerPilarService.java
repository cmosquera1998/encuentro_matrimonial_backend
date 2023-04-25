package com.encuentro.matrimonial.service.implementation;

import com.encuentro.matrimonial.modelo.PrimerPilar;
import com.encuentro.matrimonial.repository.IPrimerPilarRepository;
import com.encuentro.matrimonial.service.IPrimerPilarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrimerPilarService implements IPrimerPilarService {

	@Autowired
	IPrimerPilarRepository pilarDTO;

	@Override
	public void create(PrimerPilar pilar) {
		pilarDTO.save(pilar);
	}

	@Override
	public List<PrimerPilar> getAll() {
		return (List<PrimerPilar>) pilarDTO.findAll();
	}

	@Override
	public PrimerPilar findByPrimerPilar(Long id) {
		return pilarDTO.findByPrimerPilar(id);
	}

	@Override
	public List<PrimerPilar> findByFiltroPrimerPilar(String fecha) {
		return (List<PrimerPilar>) pilarDTO.findByFiltroPrimerPilar(fecha);
	}

	@Override
	public PrimerPilar update(PrimerPilar pilar) {
		return pilarDTO.save(pilar);
	}

	@Override
	public void delete(Long id) {
		pilarDTO.deleteById(id);
	}

}
