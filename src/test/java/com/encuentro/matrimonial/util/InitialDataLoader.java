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
	private PaisRepository paisRepository;
	
	@Autowired
	private CiudadRepository ciudadRepository;

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

		Privilege readPrivilege = createPrivilegeIfNotFound(1L,"READ_PRIVILEGE");
		Privilege writePrivilege = createPrivilegeIfNotFound(2L,"ADMIN_PRIVILEGE");
		Privilege createUserPrivilege = createPrivilegeIfNotFound(3L,"CREATE_USER_PRIVILEGE");
		Privilege editInfoPrivilege = createPrivilegeIfNotFound(4L,"EDIT_INFO_PRIVILEGE");

		List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, createUserPrivilege,
				editInfoPrivilege);
		List<Privilege> diosesanoPrivileges = Arrays.asList(readPrivilege, writePrivilege);

	
		createRoleIfNotFound(1L,"ROLE_ADMIN", adminPrivileges);
		createRoleIfNotFound(2L,"ROLE_USUARIO", Arrays.asList(readPrivilege));
		createRoleIfNotFound(3L,"ROLE_DIOSESANO", diosesanoPrivileges);
		createRoleIfNotFound(4L,"ROLE_REGIONAL", Arrays.asList(writePrivilege));
		createRoleIfNotFound(5L,"ROLE_ZONAL", Arrays.asList(readPrivilege));
		createRoleIfNotFound(6L,"ROLE_LATAM", adminPrivileges);

		Role adminRole = roleRepository.findByName("ROLE_ADMIN");
		Role usuarioRole = roleRepository.findByName("ROLE_USUARIO");
		Role diosesanoRole = roleRepository.findByName("ROLE_DIOSESANO");
		Role regionalRole = roleRepository.findByName("ROLE_REGIONAL");
		Role zonalRole = roleRepository.findByName("ROLE_ZONAL");
		Role latamRole = roleRepository.findByName("ROLE_LATAM");
		
		createPaisIfNotFound(1L, "Colombia");
		createPaisIfNotFound(2L, "Argentina");
		createPaisIfNotFound(3L, "Peru");
		
		Pais paisColombia = paisRepository.findByName("Colombia");
		Pais paisArgentina = paisRepository.findByName("Argentina");
		Pais paisPeru = paisRepository.findByName("Peru");
		
		createCiudadIfNotFound(1L, "Bogota", paisColombia);
		createCiudadIfNotFound(2L, "Medellin", paisColombia);
		createCiudadIfNotFound(3L, "Cali", paisColombia);
		createCiudadIfNotFound(4L, "Pereira", paisColombia);
		
		createCiudadIfNotFound(5L, "Buenos Aires", paisArgentina);
		createCiudadIfNotFound(6L, "Córdoba", paisArgentina);
		createCiudadIfNotFound(7L, "Rosario", paisArgentina);
		
		createCiudadIfNotFound(8L, "Lima", paisPeru);
		createCiudadIfNotFound(9L, "Cusco", paisPeru);
		createCiudadIfNotFound(10L, "Arequipa", paisPeru);
		
		Ciudad ciudadBogota = ciudadRepository.findByName("Bogota");
		Ciudad ciudadMedellin = ciudadRepository.findByName("Medellin");

		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadBogota);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadBogota);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadMedellin);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadMedellin);
		
		
		if (userRepository.findByUser("admin") == null) {
			Usuario user = new Usuario();
	        user.setId(0L);
	        user.setName("Camilo");
			user.setLastname("Mosquera");
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("admin"));
			user.setDocument("10070");
			user.setEmail("cmosquerara@gmail.com");
			user.setTelefono("+57 3213293921");
			user.setCiudad(ciudadBogota);
			user.setCreationDate(new Date());
			user.setState(true);
			
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
	private Pais createPaisIfNotFound(Long id, String name) {
		Pais pais = new Pais();
		pais.setId(id);
		pais.setName(name);
		paisRepository.save(pais);
		return pais;
	}
	
	@Transactional
	private Ciudad createCiudadIfNotFound(Long id, String name , Pais pais) {
		Ciudad ciudad = new Ciudad();
		ciudad.setId(id);
		ciudad.setName(name);
		ciudad.setPais(pais);
		ciudadRepository.save(ciudad);
		return ciudad;
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(Long id,String name) {

		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(id, name, null);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	private Role createRoleIfNotFound(Long id,String name, Collection<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(id, name, null, privileges);
			roleRepository.save(role);
		}
		return role;
	}

	@Transactional
	private PrimerPilar createPrimerPilarIfNotFound(Long id, Date fecha, int numFDS, int numMatrinoniosVivieron,
			int numSacerdotesVivieron, int numReligiososVivieron, int numReligiosasVivieron, Ciudad c) {
		PrimerPilar pilar = new PrimerPilar(id, fecha, numFDS, numMatrinoniosVivieron, numSacerdotesVivieron,
				numReligiososVivieron, c);
		primerPilarRepository.save(pilar);
		return pilar;
	}

}
