package com.encuentro.matrimonial.service.implementation;

import com.encuentro.matrimonial.modelo.SegundoPilar;
import com.encuentro.matrimonial.repository.ISegundoPilarRepository;
import com.encuentro.matrimonial.service.ISegundoPilarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SegundoPilarService implements ISegundoPilarService {

	@Autowired
	ISegundoPilarRepository pilarDTO;

	@Override
	public void create(SegundoPilar pilar) {
		pilarDTO.save(pilar);
	}

	@Override
	public List<SegundoPilar> getAll() {
		return (List<SegundoPilar>) pilarDTO.findAll();
	}

	@Override
	public SegundoPilar findBySegundoPilar(Long id) {
		return pilarDTO.findBySegundoPilar(id);
	}

	@Override
	public List<SegundoPilar> findByFiltroSegundoPilar(String fecha) {
		return (List<SegundoPilar>) pilarDTO.findByFiltroSegundoPilar(fecha);
	}

	@Override
	public SegundoPilar update(SegundoPilar pilar) {
		return pilarDTO.save(pilar);
	}

	@Override
	public void delete(Long id) {
		pilarDTO.deleteById(id);
	}

}
