package it.angelic.growlroom.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorsRepository;

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
	private CommandsRepository articleRepository;

	@Autowired
	private SensorsRepository sensorRepository;
	 
	
	@RequestMapping(value = "/sensor/add", method = RequestMethod.PUT)
	public boolean updateSensors(Sensor sensing) {
		return true;
	}

	@RequestMapping(value = "/command/add", method = RequestMethod.PUT)
	public boolean testAddCommand(Command sensing) {
		articleRepository.save(sensing);
		return true;
	}

	@PutMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseStatus(HttpStatus.OK)
	public int putSensor(@PathVariable String id, @RequestBody Sensor sensing) {
		
		
		if (!Integer.valueOf(id).equals(sensing.getId()))
			throw new IllegalArgumentException("PID Mismatch: "+id+" vs" + sensing.getId());
		
		sensing.setTimeStamp(new Date());
		sensorRepository.save(sensing);
		return sensing.getId();
	}

}
