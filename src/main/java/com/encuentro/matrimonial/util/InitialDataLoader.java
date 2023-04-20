package com.encuentro.matrimonial.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.encuentro.matrimonial.modelo.*;
import com.encuentro.matrimonial.repository.*;
import com.encuentro.matrimonial.security.BCryptPasswordEncoder;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IPrimerPilarRepository primerPilarRepository;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup)
			return;

		Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound("ADMIN_PRIVILEGE");
		Privilege createUserPrivilege = createPrivilegeIfNotFound("CREATE_USER_PRIVILEGE");
		Privilege editInfoPrivilege = createPrivilegeIfNotFound("EDIT_INFO_PRIVILEGE");

		List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, createUserPrivilege,
				editInfoPrivilege);
		List<Privilege> diosesanoPrivileges = Arrays.asList(readPrivilege, writePrivilege);

		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10);

		createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound("ROLE_USUARIO", Arrays.asList(readPrivilege));
		createRoleIfNotFound("ROLE_DIOSESANO", diosesanoPrivileges);
		createRoleIfNotFound("ROLE_REGIONAL", Arrays.asList(writePrivilege));
		createRoleIfNotFound("ROLE_ZONAL", Arrays.asList(readPrivilege));
		createRoleIfNotFound("ROLE_LATAM", adminPrivileges);

		Role adminRole = roleRepository.findByName("ROLE_ADMIN");
		Role usuarioRole = roleRepository.findByName("ROLE_USUARIO");
		Role diosesanoRole = roleRepository.findByName("ROLE_DIOSESANO");
		Role regionalRole = roleRepository.findByName("ROLE_REGIONAL");
		Role zonalRole = roleRepository.findByName("ROLE_ZONAL");
		Role latamRole = roleRepository.findByName("ROLE_LATAM");
		if (userRepository.findByUser("admin") == null) {
			Usuario user = new Usuario(0L, "Super admin", "Mosquera", "admin", passwordEncoder.encode("admin"), "001",
					new Date(), true, null);
			ArrayList<Role> roles = new ArrayList<Role>();
			roles.add(adminRole);
			roles.add(usuarioRole);
			roles.add(diosesanoRole);
			roles.add(regionalRole);
			roles.add(zonalRole);
			roles.add(latamRole);
			user.setRoles(roles);
			userRepository.save(user);
		}
		alreadySetup = true;
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(null, name, null);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(null, name, null, name, privileges);
			roleRepository.save(role);
		}
		return role;
	}

	@Transactional
	private PrimerPilar createPrimerPilarIfNotFound(Long id, Date fecha, int numFDS, int numMatrinoniosVivieron,
			int numSacerdotesVivieron, int numReligiososVivieron) {
		PrimerPilar pilar = new PrimerPilar(id, fecha, numFDS, numMatrinoniosVivieron, numSacerdotesVivieron,
				numReligiososVivieron);
		primerPilarRepository.save(pilar);
		return pilar;
	}

}
