package it.angelic.growlroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.components.SensorsService;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.Sensor;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/esp/v1/sensors")
public class ESPSensorsController {

	@Autowired
	private CommandsRepository commandsRepository;

	@Autowired
	private SensorsService sensorRepository;
	 

	@RequestMapping(value = "/command/add", method = RequestMethod.PUT)
	public boolean testAddCommand(Command sensing) {
		commandsRepository.save(sensing);
		return true;
	}

	@PutMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseStatus(HttpStatus.OK)
	public int putSensor(@PathVariable String id, @RequestBody Sensor sensing) {
		
		return sensorRepository.createOrUpdateSensor(sensing,id).getId();
	}

}
