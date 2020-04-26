package it.angelic.growlroom.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.result.DeleteResult;

import it.angelic.growlroom.model.SensorLog;
import it.angelic.growlroom.model.repositories.HourValuePair;
import it.angelic.growlroom.model.repositories.MongoSensorLogRepository;

@Service
public class MongoLogService {

	private final MongoTemplate mongoTemplate;

	@Autowired
	private MongoSensorLogRepository repository;
	
	@Autowired
	private MongoSequenceService sequenceGenerator;

	@Autowired
	public MongoLogService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
 


	public void logSensor(SensorLog in) {
		in.setLogId(sequenceGenerator.generateSequence(SensorLog.SEQUENCE_NAME));
		repository.save(in);
	}

	public List<HourValuePair> getHourChartData(int sensorId, Date tholdDate) {
		return repository.mapReduceToHourChart(sensorId, tholdDate);
	}

	public List<SensorLog> getLogBySensorId(int sensorId) {
		return repository.findByIdSensore(sensorId);
	}

	public Long deleteOldLog() {
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("timeStamp").lt(new Date()));
		DeleteResult res = mongoTemplate.remove(query1, SensorLog.class);
		return res.getDeletedCount();
	}

	/*public List<HourValuePair> getGroupedLogFromDate(int sensorId, Date dtIn) {
		Map<String, HourValuePair> hourAverage = new HashMap<String, HourValuePair>();
		Calendar c = Calendar.getInstance();
		List<SensorLog> nit = repository.findFromDate(sensorId, dtIn);

		for (SensorLog sensorLog : nit) {
			c.setTime(sensorLog.getTimeStamp());
			// int hourK = c.get(Calendar.HOUR_OF_DAY);
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
	}*/
	
	public List<HourValuePair> getGroupedLogFromDate2(int sensorId, Date dtIn) {
		Map<String, HourValuePair> hourAverage = new HashMap<String, HourValuePair>();
		Calendar c = Calendar.getInstance();
		AggregateIterable<Document> nit = repository.getHour24ChartAggregateData(sensorId);
		ArrayList<HourValuePair> ret = new ArrayList<>();
		for (Document document : nit) {
			System.out.println(document);
		}
		 
		return ret;
	}
	
}