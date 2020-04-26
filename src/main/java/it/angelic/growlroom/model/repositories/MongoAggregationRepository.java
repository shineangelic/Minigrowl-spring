package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;

public interface MongoAggregationRepository {

	// non va su atlas e benve ha detto che e` vecchia
	List<HourValuePair> mapReduceToHourChart(int sensorId, Date fromDate);

	// aggregate pipeline
	public AggregateIterable<Document> getHour24ChartAggregateData(int sensorId);
	
	// aggregate pipeline
	public AggregateIterable<Document> getHourHistoryChartAggregateData(int sensorId, Date limit);

}