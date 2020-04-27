package it.angelic.growlroom.model.repositories;

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

	private List<Document> aggrega24H(int sensorId) {
		return Arrays.asList(new Document("$match", new Document("id", sensorId).append("err", false)),
				new Document("$addFields",
						new Document("ora24", new Document("$dateToString",
								new Document("date", "$timeStamp").append("format", "%H")
										.append("timezone", "Europe/Rome")))),
				//new Document("$addFields", new Document("ora24", new Document("$hour", "$timeStamp"))),
				new Document("$group", new Document("_id", "$ora24").append("avg", new Document("$avg", "$val"))
						.append("max", new Document("$max", "$val")).append("min", new Document("$min", "$val"))));
	}

	/*
	 * [{$match: { "id" : 33, "err" : false }}, {$addFields: { 'groupStamp' : { $dateToString: { date: '$timeStamp',
	 * format: "%Y-%m-%d:%H", timezone: "Europe/Rome" } } }}, {$group: { _id: '$groupStamp', 'houravg': { '$avg': '$val'
	 * }, 'hourmin': { '$min': '$val' }, 'hourmax': { '$max': '$val' }, }}, {}]
	 */
	@Override
	public AggregateIterable<Document> aggregaStoriaV2(int sensorI) {
		MongoCollection<Document> collection = mongoTemplate.getCollection("sensors");

		/*
		 * AggregateIterable<Document> result = collection .aggregate(Arrays.asList(match(and(eq("id", 33L), eq("err",
		 * false))), addFields(new Field<>("groupStamp", new Document("$dateToString", new Document("date",
		 * "$timeStamp").append("format", "%Y-%m-%d:%H") .append("timezone", "Europe/Rome")))), group("$groupStamp",
		 * avg("houravg", "$val"), min("hourmin", "$val"), max("hourmax", "$val")), new Document()));
		 */
		AggregateIterable<Document> result = collection
				.aggregate(
						Arrays.asList(new Document("$match", new Document("id", sensorI).append("err", false)),
								new Document("$addFields",
										new Document("groupStamp", new Document("$dateToString",
												new Document("date", "$timeStamp").append("format", "%Y-%m-%d:%H")
														.append("timezone", "Europe/Rome")))),
								new Document("$group",
										new Document("_id", "$groupStamp")
												.append("houravg", new Document("$avg", "$val"))
												.append("hourmin", new Document("$min", "$val"))
												.append("hourmax", new Document("$max", "$val")))));

		return result;

	}
}