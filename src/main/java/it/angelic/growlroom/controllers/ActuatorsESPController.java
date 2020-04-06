package it.angelic.growlroom.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorsRepository;
import it.angelic.growlroom.model.Command;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/esp/v1/actuators")
public class ActuatorsESPController {

	@Autowired
	private ActuatorsRepository actuatorRepository;

	/**
	 * Put degli attuatori, ovvero dispositivi reali quali ventilatori, HVAC e luci.
	 * 
	 * @param id PIN della board fisica
	 * @param dispositivo
	 * @return
	 */
	@PutMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	// @ResponseStatus(HttpStatus.OK)
	public int putSensor(@PathVariable String id, @RequestBody Actuator dispositivo) {
		try {
			int t2 = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Unparsable ID: " + id);
		}

		if (!Integer.valueOf(id).equals(dispositivo.getId()))
			throw new IllegalArgumentException("PID Mismatch: " + id + " vs" + dispositivo.getId());

		for (Command com : dispositivo.getSupportedCommands()) {
			com.setTargetActuatorId(Integer.valueOf(id));
		}

		dispositivo.setTimeStamp(new Date());
		actuatorRepository.save(dispositivo);
		return dispositivo.getId();
	}

}
