package it.angelic.growlroom.model.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

//Impl postfix of the name on it compared to the core repository interface
public class MongoAggregationRepositoryImpl implements MongoAggregationRepository {

	private final MongoTemplate mongoTemplate;

	@Autowired
	public MongoAggregationRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}


	/**
	 * brutti stronzi di atlas, 40 minuti per scriverla e poi si deve pagare
	 * 
	 * NOT TESTED
	 * 
	 */
	@Override
	public List<HourValuePair> mapReduceToHourChart(int sensorId, Date fromDate) {
		final String map = "function(){ emit(this.timestamp , 1 ); } ";
		final String reduce = "function(key,values){ return Array.avg(values);}";

		MapReduceResults<HourValuePair> results = mongoTemplate.mapReduce("sensors", map, reduce, HourValuePair.class);

		final List<HourValuePair> sortedList = new ArrayList<>();
		for (HourValuePair result : results) {
			sortedList.add(result);
		}

		Collections.sort(sortedList);
		return sortedList;
		/*
		 * List<BarChartStatisticsResult> res = new ArrayList<>(projectCount); for (HourValuePair kv : sortedList) {
		 * ProjectEntity project = mongoTemplate.findOne(new Query(Criteria.where("id").is(kv.getId())),
		 * ProjectEntity.class);
		 * 
		 * if (project != null) { res.add(new BarChartStatisticsResult(kv.getId(), project.getTitle(), kv.getValue()));
		 * } if (res.size() >= projectCount) { break; } }
		 */
	}

}