package it.angelic.growlroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.service.ActuatorsService;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/esp/v2/actuators")
public class ESPActuatorsController {

	@Autowired
	private ActuatorsService actuatorsService;

	private final SimpMessagingTemplate simpMessagingTemplate;

	public ESPActuatorsController(SimpMessagingTemplate simpMessagingTemplate) {
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	/**
	 * Put degli attuatori, ovvero dispositivi reali quali ventilatori, HVAC e luci.
	 * 
	 * @param id
	 *            PIN della board fisica
	 * @param dispositivo
	 * @return
	 */
	@PutMapping(value = "/{boardId}/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public long putActuator(@PathVariable String id, @PathVariable String boardId, @RequestBody Actuator dispositivo) {
		try {
			int t2 = Integer.valueOf(id);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Unparsable ID: " + id);
		}

		Actuator updated = actuatorsService.createOrUpdateBoardActuator(dispositivo, boardId, id);


		// avvisa i sottoscrittori degli attuatori
		this.simpMessagingTemplate.convertAndSend("/topic/actuators", updated);

		return dispositivo.getPid();
	}

}
