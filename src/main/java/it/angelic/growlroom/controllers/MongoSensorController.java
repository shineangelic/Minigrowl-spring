package it.angelic.growlroom.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

	public List<HourValuePair> getGroupedLogFromDate(int sensorId, Date dtIn) {
		Map<String, Float> hourAverage = new HashMap<String, Float>();
		Calendar c = Calendar.getInstance();
		List<SensorLog> nit = repository.findFromDate(sensorId, dtIn);
		for (SensorLog sensorLog : nit) {
			c.setTime(sensorLog.getTimeStamp());
			int hourK = c.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat sf = new SimpleDateFormat("HH");
			String hourFormat = sf.format(c.getTime());
			if (hourAverage.containsKey(hourK)) {
				Float aver = hourAverage.get(hourK);
				hourAverage.put(hourFormat, (aver + Float.valueOf(sensorLog.getVal())) / 2f);
			} else {
				hourAverage.put(hourFormat, (Float.valueOf(sensorLog.getVal())));
			}

		}
		ArrayList<HourValuePair> ret = new ArrayList<>();
		for (String k : hourAverage.keySet()) {
			ret.add(new HourValuePair(k, hourAverage.get(k).toString()));
		}

		return ret;
	}

	public List<SensorLog> getLogBySensorId(int sensorId) {

		return repository.findByIdSensore(sensorId);
	}

}
