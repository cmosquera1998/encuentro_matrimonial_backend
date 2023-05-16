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
	private IZonaRepository zonaRepository;
	
	@Autowired
	private IRegionRepository regionRepository;
	
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

	
		createRoleIfNotFound(1L,"ROLE_DIOSESANO","ROL DIOSESANO", diosesanoPrivileges);
		createRoleIfNotFound(2L,"ROLE_REGIONAL","ROL REGIONAL", Arrays.asList(writePrivilege));
		createRoleIfNotFound(3L,"ROLE_NACIONAL","ROL NACIONAL", adminPrivileges);
		createRoleIfNotFound(4L,"ROLE_ZONAL","ROL ZONAL", Arrays.asList(readPrivilege));
		createRoleIfNotFound(5L,"ROLE_LATAM","ROL LATAM", adminPrivileges);

		Role nacionalRole = roleRepository.findByName("ROLE_NACIONAL");
		Role diosesanoRole = roleRepository.findByName("ROLE_DIOSESANO");
		Role regionalRole = roleRepository.findByName("ROLE_REGIONAL");
		Role zonalRole = roleRepository.findByName("ROLE_ZONAL");
		Role latamRole = roleRepository.findByName("ROLE_LATAM");
		
		createZonaIfNotFound(1L,"ZONA CENTRO");
		createZonaIfNotFound(2L,"ZONA SUR");
		createZonaIfNotFound(3L,"ZONA NORTE");
		
		Zona zonaCentro = zonaRepository.findByID(1L);
		Zona zonaSur = zonaRepository.findByID(2L);
		Zona zonaNorte = zonaRepository.findByID(3L);
		
		createRegionIfNotFound(1L, "REGION SUR");
		createRegionIfNotFound(2L, "REGION CENTRO");
		createRegionIfNotFound(3L, "REGION NORTE");

		Region regionSur = regionRepository.findByID(1L);
		Region regionCentro = regionRepository.findByID(2L);
		Region regionNorte = regionRepository.findByID(3L);
		
		
		
		createPaisIfNotFound(1L, "Colombia", zonaCentro);
		createPaisIfNotFound(2L, "Peru", zonaCentro);
		createPaisIfNotFound(3L, "Bolivia", zonaCentro);
		createPaisIfNotFound(4L, "Ecuador", zonaCentro);
		createPaisIfNotFound(5L, "Vanezuela", zonaCentro);
				
		Pais paisColombia = paisRepository.findByName("Colombia");
		Pais paisPeru = paisRepository.findByName("Peru");
		Pais paisBolivia = paisRepository.findByName("Bolivia");
		Pais paisEcuador = paisRepository.findByName("Ecuador");
		Pais paisVanezuela = paisRepository.findByName("Vanezuela");
		
		createCiudadIfNotFound(1L, "ARMENIA", paisColombia,regionSur);
		createCiudadIfNotFound(2L, "PEREIRA", paisColombia,regionSur);
		createCiudadIfNotFound(3L, "MANIZALES", paisColombia,regionSur);
		createCiudadIfNotFound(4L, "MEDELLIN", paisColombia,regionSur);
		createCiudadIfNotFound(5L, "CALI", paisColombia,regionSur);
		
		
		createCiudadIfNotFound(6L, "BOGOTA", paisColombia,regionCentro);
		createCiudadIfNotFound(7L, "ZIPAQUIRA", paisColombia,regionCentro);
		createCiudadIfNotFound(8L, "BUCARAMANGA", paisColombia,regionCentro);
		createCiudadIfNotFound(9L, "IBAGUE", paisColombia,regionCentro);
		createCiudadIfNotFound(10L, "NEIVA", paisColombia,regionCentro);
		createCiudadIfNotFound(11L, "PITALITO", paisColombia,regionCentro);
		createCiudadIfNotFound(12L, "FLORENCIA", paisColombia,regionCentro);
		createCiudadIfNotFound(13L, "GRANADA", paisColombia,regionCentro);
		createCiudadIfNotFound(14L, "VILLAVICENCIO", paisColombia,regionCentro);
																						

		createCiudadIfNotFound(15L, "BARRANQUILLA", paisColombia, regionNorte);
		createCiudadIfNotFound(16L, "CARTAGENA", paisColombia, regionNorte);
		createCiudadIfNotFound(17L, "SANTA MARTA", paisColombia, regionNorte);
		createCiudadIfNotFound(18L, "SAN ANDRES", paisColombia, regionNorte);

		createCiudadIfNotFound(19L, "LIMA", paisPeru, regionNorte);
		createCiudadIfNotFound(20L, "Cusco", paisPeru,regionNorte);
		createCiudadIfNotFound(21L, "Arequipa", paisPeru,regionNorte);
		
		
													
													
		Ciudad ciudadBogota = ciudadRepository.findByName("BOGOTA");
		Ciudad ciudadMedellin = ciudadRepository.findByName("MEDELLIN");
		Ciudad ciudadLima = ciudadRepository.findByName("LIMA");

		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadBogota);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadBogota);
		createPrimerPilarIfNotFound(0L, new Date(), 10, 10, 10, 10, 10, ciudadMedellin);

		
		if (userRepository.findByUser("Cmosquera") == null) {
			Usuario user = new Usuario();
	        user.setId(0L);
	        user.setName("Super Admin");
			user.setLastname("Administrador");
			user.setUsername("Cmosquera");
			user.setPassword(passwordEncoder.encode("1007064254"));
			user.setDocument("1007064254");
			user.setEmail("cmosquerara@gmail.com");
			user.setTelefono("+57 3213293921");
			user.setCiudad(ciudadBogota);
			user.setCreationDate(new Date());
			user.setState(true);
			
			ArrayList<Role> roles = new ArrayList<Role>();
			roles.add(nacionalRole);
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
	private Region createRegionIfNotFound(Long id, String name) {
		Region region = new Region();
		region.setId(id);
		region.setName(name);
		//region.setPais(pais);
		regionRepository.save(region);
		return region;
	}
	
	@Transactional
	private Pais createPaisIfNotFound(Long id, String name , Zona zona) {
		Pais pais = new Pais();
		pais.setId(id);
		pais.setName(name);
		pais.setZona(zona);
		paisRepository.save(pais);
		return pais;
	}
	
	@Transactional
	private Zona createZonaIfNotFound(Long id, String name) {
		Zona zona = new Zona();
		zona.setId(id);
		zona.setName(name);
		zonaRepository.save(zona);
		return zona;
	}
	
	@Transactional
	private Ciudad createCiudadIfNotFound(Long id, String name , Pais pais, Region region) {
		Ciudad ciudad = new Ciudad();
		ciudad.setId(id);
		ciudad.setName(name);
		ciudad.setPais(pais);
		ciudad.setRegion(region);
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
	private Role createRoleIfNotFound(Long id,String name, String detalle, Collection<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(id, name, detalle, null, privileges);
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
