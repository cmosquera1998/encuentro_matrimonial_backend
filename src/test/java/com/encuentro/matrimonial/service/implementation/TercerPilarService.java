package com.encuentro.matrimonial.service.implementation;

import com.encuentro.matrimonial.modelo.TercerPilar;
import com.encuentro.matrimonial.repository.ITercerPilarRepository;
import com.encuentro.matrimonial.service.ITercerPilarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TercerPilarService implements ITercerPilarService {

	@Autowired
	ITercerPilarRepository pilarDTO;

	@Override
	public void create(TercerPilar pilar) {
		pilarDTO.save(pilar);
	}

	@Override
	public List<TercerPilar> getAll() {
		return (List<TercerPilar>) pilarDTO.findAll();
	}

	@Override
	public TercerPilar findByTercerPilar(Long id) {
		return pilarDTO.findByTercerPilar(id);
	}

	@Override
	public List<TercerPilar> findByFiltroTercerPilar(String fecha) {
		return (List<TercerPilar>) pilarDTO.findByFiltroTercerPilar(fecha);
	}

	@Override
	public TercerPilar update(TercerPilar pilar) {
		return pilarDTO.save(pilar);
	}

	@Override
	public void delete(Long id) {
		pilarDTO.deleteById(id);
	}

}
