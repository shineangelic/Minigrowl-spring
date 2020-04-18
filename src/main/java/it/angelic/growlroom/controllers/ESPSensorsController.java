package it.angelic.growlroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PatchMapping;
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
	private final SimpMessagingTemplate simpMessagingTemplate;

	public ESPSensorsController(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	@Autowired
	private SensorsService sensorService;

	@Autowired
	private MongoSensorController mongoSensorController;

	@PatchMapping("/heavyresource/{id}")
	public ResponseEntity<?> partialUpdateName(@RequestBody Sensor partialUpdate, @PathVariable("id") String id) {
		// TODO partial update
		sensorService.createOrUpdateSensor(partialUpdate, id);
		return ResponseEntity.ok("sensor updated");
	}

	@PutMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseStatus(HttpStatus.OK)
	public int putSensor(@PathVariable String id, @RequestBody Sensor sensing) {
		
		//Sensor pasS = sensorService.find
		Sensor updated = sensorService.createOrUpdateSensor(sensing, id);
		
	/*	mongoSensorController.logSensor(new SensorLog(updated));
		// avvisa i sottoscrittori dei sensori
		this.simpMessagingTemplate.convertAndSend("/topic/sensors", sensorService.getSensors());*/

		return updated.getId();
	}

}
