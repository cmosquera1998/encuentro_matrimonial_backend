package com.encuentro.matrimonial.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.encuentro.matrimonial.constants.ResourceMapping;
import com.encuentro.matrimonial.modelo.CentroSalud;
import com.encuentro.matrimonial.modelo.Paciente;
import com.encuentro.matrimonial.modelo.Usuario;
import com.encuentro.matrimonial.service.ICentroSaludService;
import com.encuentro.matrimonial.service.IPacienteService;
import com.encuentro.matrimonial.util.ErrorMessage;
import com.encuentro.matrimonial.util.ErrorMessage2;

@RestController
@RequestMapping(ResourceMapping.PACIENTE)
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS }, allowedHeaders = "*")
public class PacienteController {

	@Autowired
	private IPacienteService pacienteService;

	@Autowired
	private ICentroSaludService centroService;
	// servicio que trae el listado de pacientes
	@RequestMapping(value = "/getPacientes", method = RequestMethod.GET, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Paciente>>> getPacientes() {
		List<Paciente> listado = pacienteService.getPacientes();
		ErrorMessage<List<Paciente>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de Pacientes", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	// servicio que trae el listado de pacientes dado un centro de salud
	@RequestMapping(value = "/getPacientesXCentro", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<ErrorMessage<List<Paciente>>> getPacientesCentro(@RequestBody Paciente pac) {
		List<Paciente> listado = pacienteService.findByCentro(pac.getCentroSalud().getId());
		ErrorMessage<List<Paciente>> error = listado.isEmpty()
				? new ErrorMessage<>(1, "No se ha encontrado información", null)
				: new ErrorMessage<>(0, "Lista de Pacientes", listado);
		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	// servicio para actualizar el centro de salud a un paciente
	@RequestMapping(value = "/updatedPaciente", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> updatedPaciente(@RequestBody Paciente pac) {
		Paciente pa = pacienteService.findBy(pac.getId());
		if (pa == null) {
			return new ResponseEntity(new ErrorMessage2(1, "No sea encontrado el paciente"), HttpStatus.OK);
		}
		if (pac.getCentroSalud() == null || pac.getCentroSalud().getId() == null) {
			return new ResponseEntity(new ErrorMessage2(1, "Especifique el centro de salud "), HttpStatus.OK);
		}
		CentroSalud cen = centroService.findByC(pac.getCentroSalud().getId());
		if (cen == null) {
			return new ResponseEntity(new ErrorMessage2(1, "No sea encontrado el centro de salud"), HttpStatus.OK);
		}
		pa.setCentroSalud(cen);
		pacienteService.updatePaciente(pa);
		return new ResponseEntity(new ErrorMessage2(0, "Paciente actualizado con exito!"), HttpStatus.OK);
	}
}
