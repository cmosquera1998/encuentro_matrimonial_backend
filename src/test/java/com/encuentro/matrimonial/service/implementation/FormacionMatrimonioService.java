package com.encuentro.matrimonial.service.implementation;

import com.encuentro.matrimonial.modelo.FormacionMatrimonio;
import com.encuentro.matrimonial.repository.IFormacionMatrimonioRepository;
import com.encuentro.matrimonial.service.IFormacionMatrimonioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormacionMatrimonioService implements IFormacionMatrimonioService {

	@Autowired
	IFormacionMatrimonioRepository formacionDTO;

	@Override
	public void create(FormacionMatrimonio pilar) {
		formacionDTO.save(pilar);
	}

	@Override
	public List<FormacionMatrimonio> getAll() {
		return (List<FormacionMatrimonio>) formacionDTO.findAll();
	}

	@Override
	public FormacionMatrimonio findByFormacionMatrimonio(Long id) {
		return formacionDTO.findByFormacionMatrimonio(id);
	}

	@Override
	public List<FormacionMatrimonio> findByFiltroFormacionMatrimonio(String fecha) {
		return (List<FormacionMatrimonio>) formacionDTO.findByFiltroFormacionMatrimonio(fecha);
	}

	@Override
	public FormacionMatrimonio update(FormacionMatrimonio pilar) {
		return formacionDTO.save(pilar);
	}

	@Override
	public void delete(Long id) {
		formacionDTO.deleteById(id);
	}

}
