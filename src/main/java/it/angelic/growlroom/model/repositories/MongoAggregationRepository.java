package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;

public interface MongoAggregationRepository {
 

	List<HourValuePair> mapReduceToHourChart(int sensorId, Date fromDate);
	 
	public AggregateIterable<Document> getHour24ChartAggregateData(int sensorId);

}