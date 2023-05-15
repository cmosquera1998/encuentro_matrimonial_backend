package com.encuentro.matrimonial.service.implementation;

import com.encuentro.matrimonial.modelo.CuartoPilar;
import com.encuentro.matrimonial.repository.ICuartoPilarRepository;
import com.encuentro.matrimonial.service.ICuartoPilarService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuartoPilarService implements ICuartoPilarService {

	@Autowired
	ICuartoPilarRepository pilarDTO;

	@Override
	public void create(CuartoPilar pilar) {
		pilarDTO.save(pilar);
	}

	@Override
	public List<CuartoPilar> getAll() {
		return (List<CuartoPilar>) pilarDTO.findAll();
	}

	@Override
	public CuartoPilar findByCuartoPilar(Long id) {
		return pilarDTO.findByCuartoPilar(id);
	}

	@Override
	public CuartoPilar update(CuartoPilar pilar) {
		return pilarDTO.save(pilar);
	}

	@Override
	public void delete(Long id) {
		pilarDTO.deleteById(id);
	}

}
