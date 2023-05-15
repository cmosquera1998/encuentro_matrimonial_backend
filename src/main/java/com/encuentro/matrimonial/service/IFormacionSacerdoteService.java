package com.encuentro.matrimonial.service;

import java.util.List;

import com.encuentro.matrimonial.modelo.FormacionSacerdote;

public interface IFormacionSacerdoteService {

	void create(FormacionSacerdote pilar);

	List<FormacionSacerdote> getAll();

	FormacionSacerdote findByFormacionSacerdote(Long id);

	FormacionSacerdote update(FormacionSacerdote pilar);

	void delete(Long id);

}
