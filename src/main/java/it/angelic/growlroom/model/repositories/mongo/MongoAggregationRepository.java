package it.angelic.growlroom.model.repositories.mongo;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;

import it.angelic.growlroom.model.HourValuePair;
import it.angelic.growlroom.model.mongo.ActuatorLog;
import it.angelic.growlroom.model.mongo.SensorLog;

public interface MongoAggregationRepository {

	// non va su atlas e benve ha detto che e` vecchia
	List<HourValuePair> mapReduceToHourChart(int sensorId, Date fromDate);

	// aggregate pipeline
	public AggregateIterable<Document> getHour24ChartAggregateData(int sensorId);
	
	public AggregateIterable<Document> aggregaStoriaUltimaSettimana(Long sensorId);
	
	public ActuatorLog getLastByActuatorId(Long id);
	
	//per check log mismatch
	public SensorLog getLastBySensorId(Long id);
	
	//per ogni attuatore ottiene i millisec di uptime
	public AggregateIterable<Document> getIntervalActuatorsOnMsec(Date from, Date to, Integer actuatorId);

}