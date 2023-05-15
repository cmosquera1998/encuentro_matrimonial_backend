package com.encuentro.matrimonial.service;

import java.util.List;

import com.encuentro.matrimonial.modelo.FormacionMatrimonio;

public interface IFormacionMatrimonioService {

	void create(FormacionMatrimonio pilar);

	List<FormacionMatrimonio> getAll();

	FormacionMatrimonio findByFormacionMatrimonio(Long id);

	FormacionMatrimonio update(FormacionMatrimonio pilar);

	void delete(Long id);

}
