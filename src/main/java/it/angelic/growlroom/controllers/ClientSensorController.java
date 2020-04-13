package it.angelic.growlroom.controllers;

import java.io.FileNotFoundException;
import java.util.Date;

import javax.print.PrintException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.service.ActuatorsService;
import it.angelic.growlroom.service.CommandsService;
import it.angelic.growlroom.service.SensorsService;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/minigrowl/v1")
public class ClientSensorController {

	@Autowired
	private SensorsService sensorService;

	@Autowired
	private CommandsService commandsService;

	@Autowired
	private ActuatorsService actuatorsService;
	


	@CrossOrigin
	@GetMapping(value = "/sensors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSensors(@RequestParam(value = "dataInizio", required = false) Date dtIn)
			throws FileNotFoundException, PrintException {

		return new ResponseEntity<>(sensorService.getSensors(), HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/actuators", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSensors() {

		return new ResponseEntity<>(actuatorsService.getActuators(), HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/commands", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getSupportedCommands() {
		return new ResponseEntity<>(commandsService.getSupportedCommands(), HttpStatus.OK);
	}

	@CrossOrigin
	@PutMapping(value = "/commands/queue/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseStatus(HttpStatus.OK)
	public Long sendCommand(@RequestBody Command sensing) {

		return commandsService.sendCommand(sensing);
	}
 
}
