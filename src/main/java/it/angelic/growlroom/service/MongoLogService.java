package it.angelic.growlroom.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.result.DeleteResult;

import it.angelic.growlroom.model.mongo.ActuatorLog;
import it.angelic.growlroom.model.mongo.SensorLog;
import it.angelic.growlroom.model.repositories.HourValuePair;
import it.angelic.growlroom.model.repositories.MongoActuatorLogRepository;
import it.angelic.growlroom.model.repositories.MongoSensorLogRepository;

@Service
public class MongoLogService {

	private final MongoTemplate mongoTemplate;

	@Autowired
	private MongoSensorLogRepository mongoSensorLogRepository;
	
	@Autowired
	private MongoActuatorLogRepository mongoActuatorLogRepository;

	@Autowired
	private MongoSequenceService sequenceGenerator;

	@Autowired
	public MongoLogService(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public void logSensor(SensorLog in) {
		in.setLogId(sequenceGenerator.generateSequence(SensorLog.SEQUENCE_NAME));
		mongoSensorLogRepository.save(in);
	}
	
	public void logActuator(ActuatorLog in) {
		in.setLogId(sequenceGenerator.generateSequence(ActuatorLog.SEQUENCE_NAME_ACTUATORS));
		
		
		ActuatorLog last = mongoActuatorLogRepository.findLastByActuatorId(in.getId());
		if (last != null) {
			last.setNextLogId(in.getLogId());
			mongoActuatorLogRepository.save(last);
		}
		
		mongoActuatorLogRepository.save(in);
	}

	public List<HourValuePair> getHourChartSensorData(int sensorId, Date tholdDate) {
		return mongoSensorLogRepository.mapReduceToHourChart(sensorId, tholdDate);
	}

	public List<SensorLog> getLogBySensorId(int sensorId) {
		return mongoSensorLogRepository.findByIdSensore(sensorId);
	}

	public Long deleteOldSensorLog() {
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("timeStamp").lt(new Date()));
		DeleteResult res = mongoTemplate.remove(query1, SensorLog.class);
		return res.getDeletedCount();
	}

	public List<HourValuePair> getGroupedSensorLogFromDate(int sensorId, Date dtIn) {
		final DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		AggregateIterable<Document> nit = mongoSensorLogRepository.getHour24ChartAggregateData(sensorId);
		ArrayList<HourValuePair> ret = new ArrayList<>();
		for (Document document : nit) {
			HourValuePair ha = new HourValuePair(document.get("_id").toString(),
					df.format(Double.valueOf(document.get("avg").toString())));
			ha.setMax(df.format(Double.valueOf(document.get("max").toString())));
			ha.setMin(df.format(Double.valueOf(document.get("min").toString())));
			ret.add(ha);
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * WARN unused Date
	 * 
	 * @param sensorId
	 * @param dtIn
	 * @return
	 */
	public List<HourValuePair> getGroupedSensorLogHistory(int sensorId, Date dtIn) {
		final DecimalFormat df = new DecimalFormat();
		df.setGroupingUsed(false);
		df.setMaximumFractionDigits(2);
		Calendar c = Calendar.getInstance();
		AggregateIterable<Document> nit = mongoSensorLogRepository.aggregaStoriaV2(sensorId);
		ArrayList<HourValuePair> ret = new ArrayList<>();
		for (Document document : nit) {
			// schifo perche` mongo torna le ore a 1 cifra
			String tCode = document.get("_id").toString();
			String[] dPart = tCode.split(":");

			if (dPart[1].length() == 1)
				dPart[1] = "0" + dPart[1];

			HourValuePair ha = new HourValuePair(dPart[0] + dPart[1],
					df.format(Double.valueOf(document.get("houravg").toString())));
			ha.setMax(df.format(Double.valueOf(document.get("hourmax").toString())));
			ha.setMin(df.format(Double.valueOf(document.get("hourmin").toString())));
			ret.add(ha);
		}
		Collections.sort(ret);
		return ret;
	}

}
