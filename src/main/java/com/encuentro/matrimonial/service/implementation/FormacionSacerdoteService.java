package com.encuentro.matrimonial.service.implementation;

import com.encuentro.matrimonial.modelo.FormacionSacerdote;
import com.encuentro.matrimonial.repository.IFormacionSacerdoteRepository;
import com.encuentro.matrimonial.service.IFormacionSacerdoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormacionSacerdoteService implements IFormacionSacerdoteService {

	@Autowired
	IFormacionSacerdoteRepository formacionDTO;

	@Override
	public void create(FormacionSacerdote pilar) {
		formacionDTO.save(pilar);
	}

	@Override
	public List<FormacionSacerdote> getAll() {
		return (List<FormacionSacerdote>) formacionDTO.findAll();
	}

	@Override
	public FormacionSacerdote findByFormacionSacerdote(Long id) {
		return formacionDTO.findByFormacionSacerdote(id);
	}

	@Override
	public List<FormacionSacerdote> findByFiltroFormacionSacerdote(String fecha) {
		return (List<FormacionSacerdote>) formacionDTO.findByFiltroFormacionSacerdote(fecha);
	}

	@Override
	public FormacionSacerdote update(FormacionSacerdote pilar) {
		return formacionDTO.save(pilar);
	}

	@Override
	public void delete(Long id) {
		formacionDTO.deleteById(id);
	}

}
