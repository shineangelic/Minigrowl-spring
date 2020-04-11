package it.angelic.growlroom.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.service.ActuatorsService;
import it.angelic.growlroom.service.CommandsService;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/esp/v1/commands")
public class ESPCommandsController {

	@Autowired
	private ActuatorsService actuatorsService;

	@Autowired
	private CommandsService commandsService;

	@CrossOrigin
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Command> downloadComandi() {
		return commandsService.getUnexecutedCommands();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/id/{queueCommandid}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean deleteExecutedCommand(@PathVariable String queueCommandid, Command executed) {
		try {
			return commandsService.removeExecutedCommand(Long.valueOf(queueCommandid), executed);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException();
		}
	}

}
