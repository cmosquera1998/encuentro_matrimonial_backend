package com.encuentro.matrimonial.service;

import java.util.List;

import com.encuentro.matrimonial.modelo.TercerPilar;

public interface ITercerPilarService {

	void create(TercerPilar pilar);

	List<TercerPilar> getAll();

	TercerPilar findByTercerPilar(Long id);

	List<TercerPilar> findByFiltroTercerPilar(String fecha);

	TercerPilar update(TercerPilar pilar);

	void delete(Long id);

}
