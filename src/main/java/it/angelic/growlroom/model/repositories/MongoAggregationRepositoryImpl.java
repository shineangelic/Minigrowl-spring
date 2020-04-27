package it.angelic.growlroom.model.repositories;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.max;
import static com.mongodb.client.model.Accumulators.min;
import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Field;

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

	}

	public AggregateIterable<Document> getHour24ChartAggregateData(int sensorId) {

		AggregateIterable<Document> result = mongoTemplate.getCollection("sensors").aggregate(aggrega24H(sensorId));
		return result;

	}

	@Override
	public AggregateIterable<Document> getHourHistoryChartAggregateData(int sensorId, Date limit) {
		AggregateIterable<Document> result = mongoTemplate.getCollection("sensors").aggregate(aggregaStoria(sensorId));
		return result;
	}

	private List<Document> aggrega24H(int sensorId) {
		return Arrays.asList(new Document("$match", new Document("id", sensorId).append("err", false)),
				new Document("$addFields", new Document("ora24", new Document("$hour", "$timeStamp"))),
				new Document("$group", new Document("_id", "$ora24").append("avg", new Document("$avg", "$val"))
						.append("max", new Document("$max", "$val")).append("min", new Document("$min", "$val"))));
	}

	private List<Document> aggregaStoria(int sensorI) {
		return Arrays.asList(new Document("$match", new Document("id", sensorI).append("err", false)),
				new Document("$addFields",
						new Document("year", new Document("$year", "$timeStamp"))
								.append("mon", new Document("$month", "$timeStamp"))
								.append("GG", new Document("$dayOfMonth", "$timeStamp"))
								.append("ora24", new Document("$hour", "$timeStamp"))),
				new Document("$addFields",
						new Document("GGts",
								new Document("$concat",
										Arrays.asList(new Document("$substr", Arrays.asList("$year", 0L, -1L)), "-",
												new Document("$substr", Arrays.asList("$mon", 0L, -1L)), "-",
												new Document("$substr", Arrays.asList("$GG", 0L, -1L)), ":",
												new Document("$substr", Arrays.asList("$ora24", 0L, -1L)))))),
				new Document("$group", new Document("_id", "$GGts").append("avg", new Document("$avg", "$val"))
						.append("max", new Document("$max", "$val")).append("min", new Document("$min", "$val"))));

	}

	/*
	 * [{$match: { "id" : 33, "err" : false }}, {$addFields: { 'groupStamp' : { $dateToString: { date: '$timeStamp',
	 * format: "%Y-%m-%d:%H", timezone: "Europe/Rome" } } }}, {$group: { _id: '$groupStamp', 'houravg': { '$avg': '$val'
	 * }, 'hourmin': { '$min': '$val' }, 'hourmax': { '$max': '$val' }, }}, {}]
	 */
	@Override
	public AggregateIterable<Document> aggregaStoriaV2(int sensorI) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("");

		AggregateIterable<Document> result = collection
				.aggregate(Arrays.asList(match(and(eq("id", 33L), eq("err", false))),
						addFields(new Field<>("groupStamp",
								new Document("$dateToString",
										new Document("date", "$timeStamp").append("format", "%Y-%m-%d:%H")
												.append("timezone", "Europe/Rome")))),
						group("$groupStamp", avg("houravg", "$val"), min("hourmin", "$val"), max("hourmax", "$val")),
						new Document()));
		
		return result;

	}
}