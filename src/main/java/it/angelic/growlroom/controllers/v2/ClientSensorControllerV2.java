package it.angelic.growlroom.controllers.v2;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.print.PrintException;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.repositories.HourValuePair;
import it.angelic.growlroom.service.ActuatorsService;
import it.angelic.growlroom.service.CommandsService;
import it.angelic.growlroom.service.MongoLogService;
import it.angelic.growlroom.service.SensorsService;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api/minigrowl/v2")
public class ClientSensorControllerV2 {

	@Autowired
	private SensorsService sensorService;

	@Autowired
	private CommandsService commandsService;

	@Autowired
	private ActuatorsService actuatorsService;

	@Autowired
	private MongoLogService mongoLogService;

	@CrossOrigin
	@GetMapping(value = "/sensors/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Sensor>> getSensors(
			@RequestParam(value = "dataInizio", required = false) Date dtIn, @PathVariable String boardId)
			throws FileNotFoundException, PrintException {

		return new ResponseEntity<>(sensorService.getSensors(), HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/actuators/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Actuator>> getActuators() {

		return new ResponseEntity<>(actuatorsService.getActuators(), HttpStatus.OK);
	}

	@CrossOrigin // UNUSED, see supported commands
	@GetMapping(value = "/commands/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Command>> getSupportedCommands() {
		return ResponseEntity.status(HttpStatus.OK).body(commandsService.getSupportedCommands());
	}

	@CrossOrigin
	@PutMapping(value = "/commands/{boardId}/queue/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long sendCommand(@RequestBody Command sensing, @PathVariable String boardId) {
		try {
			int t2 = Integer.valueOf(boardId);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Unparsable boardId: " + boardId);
		}
		return commandsService.sendCommand(sensing);
	}

	@CrossOrigin
	@PutMapping(value = "/commands/{boardId}/fullRefresh", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long sendForceRefresh() {
		return commandsService.sendFullRefreshCommand();
	}

	@CrossOrigin
	@PutMapping(value = "/sensors/{boardId}/log/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long deleteLogs() {

		return mongoLogService.deleteOldSensorLog();
	}

	@CrossOrigin
	@GetMapping(value = "/actuators/{boardId}/uptime", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Document>> getActuatorsUptime(
			@RequestParam(value = "dataInizio", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dtIn,
			@RequestParam(value = "dataFine", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date dtOut,
			@RequestParam(value = "actuatorId", required = true) Integer actuatorId)
			throws FileNotFoundException, IllegalArgumentException {

		// return new ResponseEntity<>(mongoLogService.getLogBySensorId(Integer.valueOf(id)), HttpStatus.OK);
		return new ResponseEntity<>(mongoLogService.getGroupedActuatorUptime(dtIn, dtOut, actuatorId), HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/sensors/{boardId}/{id}/hourChart", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HourValuePair>> getSensorsHourChart(@PathVariable String id,
			@RequestParam(value = "dataInizio", required = false) Date dtIn)
			throws FileNotFoundException, IllegalArgumentException {
		if (!(Integer.valueOf(id).intValue() > 0))
			throw new IllegalArgumentException("INVALID sensor id: " + id);
		// return new ResponseEntity<>(mongoLogService.getLogBySensorId(Integer.valueOf(id)), HttpStatus.OK);
		return new ResponseEntity<>(mongoLogService.getGroupedSensorLogHourChart(Integer.valueOf(id), dtIn),
				HttpStatus.OK);
	}

	@CrossOrigin
	@GetMapping(value = "/sensors/{boardId}/{id}/historyChart", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<HourValuePair>> getSensorsHistoryChart(@PathVariable String id,
			@RequestParam(value = "dataInizio", required = false) Date dtIn)
			throws FileNotFoundException, IllegalArgumentException {
		if (!(Integer.valueOf(id).intValue() > 0))
			throw new IllegalArgumentException("INVALID sensor id: " + id);
		// return new ResponseEntity<>(mongoLogService.getLogBySensorId(Integer.valueOf(id)), HttpStatus.OK);
		return new ResponseEntity<>(mongoLogService.getGroupedSensorLogHistory(Integer.valueOf(id), dtIn),
				HttpStatus.OK);
	}
}
