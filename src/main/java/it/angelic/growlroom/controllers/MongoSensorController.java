package it.angelic.growlroom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.angelic.growlroom.model.SensorLog;
import it.angelic.growlroom.model.repositories.MongoSensorLogRepository;
import it.angelic.growlroom.service.MongoSequenceService;

@Controller
public class MongoSensorController {
	@Autowired
	private MongoSensorLogRepository repository;

	@Autowired
	private MongoSequenceService sequenceGenerator;

	public void logSensor(SensorLog in) {
		in.setLogId(sequenceGenerator.generateSequence(SensorLog.SEQUENCE_NAME));
		repository.save(in);
	}

}
