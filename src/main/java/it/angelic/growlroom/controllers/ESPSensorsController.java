package it.angelic.growlroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.service.SensorsService;

/**
 * L'esp contiene la definizione dei sensori che rispecchia la entity
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/esp/v1/sensors")
public class ESPSensorsController {

	@Autowired
	private SensorsService sensorService;

	@PutMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public int putSensor(@PathVariable String id, @RequestBody Sensor sensing) {

		Sensor updated = sensorService.createOrUpdateSensor(sensing, id);
		return updated.getPid();
	}

}
