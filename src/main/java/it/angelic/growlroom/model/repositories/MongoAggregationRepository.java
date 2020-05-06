package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;

import it.angelic.growlroom.model.mongo.ActuatorLog;

public interface MongoAggregationRepository {

	// non va su atlas e benve ha detto che e` vecchia
	List<HourValuePair> mapReduceToHourChart(int sensorId, Date fromDate);

	// aggregate pipeline
	public AggregateIterable<Document> getHour24ChartAggregateData(int sensorId);
	

	// aggregate pipeline V2
	public AggregateIterable<Document> aggregaStoriaV2(int sensorI);
	
	public ActuatorLog getLastByActuatorId(Long id);
	
	//per ogni attuatore ottiene i millisec di uptime
	public AggregateIterable<Document> getIntervalActuatorsOnMsec(Date from, Date to);

}