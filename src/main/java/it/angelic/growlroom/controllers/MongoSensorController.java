package it.angelic.growlroom.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import it.angelic.growlroom.model.SensorLog;
import it.angelic.growlroom.model.repositories.HourValuePair;
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

	public List<HourValuePair> getHourChartData(int sensorId, Date tholdDate) {
		return repository.mapReduceToHourChart(sensorId, tholdDate);

	}

	public Map<Integer, Float> getGroupedLogFromDate(int sensorId, Date dtIn) {
		Map<Integer, Float> hourAverage = new HashMap<Integer, Float>();
		Calendar c = Calendar.getInstance();
		List<SensorLog> nit = repository.findFromDate(sensorId, dtIn);
		for (SensorLog sensorLog : nit) {
			c.setTime(sensorLog.getTimeStamp());
			int hourK = c.get(Calendar.HOUR_OF_DAY);
			if (hourAverage.containsKey(hourK)) {
				Float aver = hourAverage.get(hourK);
				hourAverage.put(hourK, (aver + Float.valueOf(sensorLog.getVal())) / 2f);
			} else {
				hourAverage.put(hourK, (Float.valueOf(sensorLog.getVal())));
			}

		}
		return hourAverage;
	}

	public List<SensorLog> getLogBySensorId(int sensorId) {

		return repository.findByIdSensore(sensorId);
	}

}
