package com.encuentro.matrimonial.service;

import java.util.List;

import com.encuentro.matrimonial.modelo.SegundoPilar;

public interface ISegundoPilarService {

	void create(SegundoPilar pilar);

	List<SegundoPilar> getAll();

	SegundoPilar findBySegundoPilar(Long id);

	SegundoPilar update(SegundoPilar pilar);

	void delete(Long id);

}
