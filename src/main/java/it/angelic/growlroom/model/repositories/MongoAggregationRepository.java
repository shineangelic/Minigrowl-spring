package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

public interface MongoAggregationRepository {
 

	List<HourValuePair> mapReduceToHourChart(int sensorId, Date fromDate);

}