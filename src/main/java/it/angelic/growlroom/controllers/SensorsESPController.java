package it.angelic.growlroom.controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.PrintException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandEnum;
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
public class SensorsESPController {

	@Autowired
	private CommandsRepository articleRepository;

	@Autowired
	private SensorsRepository sensorRepository;
	/*
	 * @CrossOrigin
	 * 
	 * @RequestMapping("/sensor/list")
	 * 
	 * @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE) public List<Sensor>
	 * readSensori(@RequestParam(value = "dataInizio", required = false) Date dtIn) throws FileNotFoundException,
	 * PrintException {
	 * 
	 * List<Sensor> res = new ArrayList<Sensor>(); res.add(new Sensor());
	 * 
	 * return res; }
	 */

	@CrossOrigin
	@RequestMapping(value = "/command/download", method = RequestMethod.GET)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Command> downloadComandi(@RequestParam(value = "dataInizio", required = false) Date dtIn)
			throws FileNotFoundException, PrintException {

		List<Command> res = new ArrayList<Command>();
		Command ree = new Command();
		ree.setActuatorId("1");
		ree.setCmdType(CommandEnum.DECREASE);
		ree.setParameter("s");
		res.add(ree);

		return res;
	}

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
	public int putSensor(@RequestBody Sensor sensing) {
		sensing.setTimeStamp(new Date());
		sensorRepository.save(sensing);
		return sensing.getId();
	}

}
