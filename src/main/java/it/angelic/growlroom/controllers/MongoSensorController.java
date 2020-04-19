package it.angelic.growlroom.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
		Map<String, HourValuePair> hourAverage = new HashMap<String, HourValuePair>();
		Calendar c = Calendar.getInstance();
		List<SensorLog> nit = repository.findFromDate(sensorId, dtIn);
		

		for (SensorLog sensorLog : nit) {
			c.setTime(sensorLog.getTimeStamp());
			//int hourK = c.get(Calendar.HOUR_OF_DAY);
			SimpleDateFormat sf = new SimpleDateFormat("HH");
			String hourFormat = sf.format(c.getTime());
			if (hourAverage.containsKey(hourFormat)) {
				HourValuePair aver = hourAverage.get(hourFormat);
				
				Float curMin = Float.valueOf(aver.getMin());
				Float curMax = Float.valueOf(aver.getMax());
				
				if (Float.valueOf(sensorLog.getVal()) < curMin)
					aver.setMin(sensorLog.getVal());
				if (Float.valueOf(sensorLog.getVal()) > curMax)
					aver.setMax(sensorLog.getVal());
				Float avg = ((Float.valueOf(aver.getValue()) + Float.valueOf(sensorLog.getVal())) / 2f);
				aver.setValue(avg.toString());
				hourAverage.put(hourFormat, aver);
			} else {
				HourValuePair aver = new HourValuePair(hourFormat, Float.valueOf(sensorLog.getVal()).toString());
				hourAverage.put(hourFormat, aver);
			}

		}
		ArrayList<HourValuePair> ret = new ArrayList<>();
		for (String k : hourAverage.keySet()) {
			ret.add(hourAverage.get(k));
		}
		Collections.sort(ret);
		return ret;
	}

	public List<SensorLog> getLogBySensorId(int sensorId) {

		return repository.findByIdSensore(sensorId);
	}

}
