package com.encuentro.matrimonial.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.encuentro.matrimonial.modelo.Role;
import com.encuentro.matrimonial.repository.RoleRepository;
import com.encuentro.matrimonial.service.IRoleService;

@Service
public class RoleService implements IRoleService {

	@Autowired
	private RoleRepository roleDao;

	@Override
	public List<Role> getRoles() {
		return roleDao.findAll();
	}

}
