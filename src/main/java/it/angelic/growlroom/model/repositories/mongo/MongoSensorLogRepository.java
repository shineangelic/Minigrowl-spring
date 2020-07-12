package it.angelic.growlroom.model.repositories.mongo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.angelic.growlroom.model.mongo.SensorLog;

public interface MongoSensorLogRepository extends MongoRepository<SensorLog, String>,MongoAggregationRepository {

	@Override
	List<SensorLog> findAll();

	@Query("{'sensorId': ?0}")
	List<SensorLog> findByIdSensore(int sensorId);

	@Query("{'sensorId': ?0},{'timeStamp': $gt: ?1}")
	List<SensorLog> findFromDate(int sensorId, Date tHold);
 
}