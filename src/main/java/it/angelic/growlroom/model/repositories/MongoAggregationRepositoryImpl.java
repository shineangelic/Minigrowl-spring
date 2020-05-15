package it.angelic.growlroom.model.repositories;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.addFields;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.ne;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Field;

import it.angelic.growlroom.model.mongo.ActuatorLog;

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

	public AggregateIterable<Document> getIntervalActuatorsOnMsec(Date from, Date to) {
		AggregateIterable<Document> result = mongoTemplate.getCollection("actuators")
				.aggregate(aggregaTempoAccese(from, to));
		return result;
	}

	/*
	 * PIPELINE
	 * 
	 * [{$match: { timeStamp: { $gte: ISODate("2016-06-01T00:00:00.000Z"), $lt: ISODate("2020-06-03T00:00:00.000Z")},
	 * 
	 * }}, {$lookup: { from: 'actuators', localField: '_id', foreignField: 'nextLogId', as: 'previous' }}, {$addFields:
	 * { prev: {$arrayElemAt: [ '$previous', 0]} }}, {$addFields: { msecAccesa:{ $subtract:
	 * ['$timeStamp','$prev.timeStamp'] } }}, {$match: { prev: {$ne:null}, 'prev.reading':"1" }}, {$group: { _id: '$id',
	 * count: { $sum: '$msecAccesa' } }}]
	 * 
	 */private List<Bson> aggregaTempoAccese(Date in, Date out) {
		return Arrays.asList(match(and(gt("timeStamp", in), lte("timeStamp", out))),
				lookup("actuators", "_id", "nextLogId", "previous"),
				addFields(new Field("prev", new Document("$arrayElemAt", Arrays.asList("$previous", 0L)))),
				addFields(new Field("msecAccesa",
						new Document("$subtract", Arrays.asList("$timeStamp", "$prev.timeStamp")))),
				match(and(ne("prev", new BsonNull()), eq("prev.reading", "1"))),
				group("$id", sum("count", "$msecAccesa")));
	}

	private List<Document> aggrega24H(int sensorId) {
		return Arrays.asList(new Document("$match", new Document("id", sensorId).append("err", false)),
				new Document("$addFields",
						new Document("ora24",
								new Document("$dateToString",
										new Document("date", "$timeStamp").append("format", "%H").append("timezone",
												"Europe/Rome")))),
				// new Document("$addFields", new Document("ora24", new Document("$hour", "$timeStamp"))),
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

		Calendar dtIn = Calendar.getInstance();
		Date dtTo = new Date();
		dtTo.setTime(dtIn.getTime().getTime());
		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-DD");

		dtIn.add(Calendar.DATE, -7);

		MongoCollection<Document> collection = mongoTemplate.getCollection("sensors");

		AggregateIterable<Document> result = collection
				.aggregate(
						Arrays.asList(
								/*new Document(new Document("$match",
										new Document("timeStamp",
												new Document("$gt", dtIn.getTime()).append("$lte", dtTo)))),*/
								new Document("$match", new Document("id", sensorI).append("errorPresent", false)),
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

	public ActuatorLog getLastByActuatorId(Long id) {
		/*
		 * return mongoTemplate.findOne( Query.query(Criteria.where("_id").is(id)), ActuatorLog.class, COLLECTION_NAME
		 * );
		 */
		Query query = Query.query(Criteria.where("id").is(id));
		query.with(new Sort(Sort.Direction.DESC, "timeStamp"));
		return mongoTemplate.findOne(query, ActuatorLog.class);
		// return mongoTemplate.find(Query.query(Criteria.where("_id").is(id)), ActuatorLog.class).sort({ "date_time" :
		// -1 }).limit(1);
	}
}