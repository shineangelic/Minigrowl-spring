package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.angelic.growlroom.model.SensorLog;

public interface MongoSensorLogRepository extends MongoRepository<SensorLog, String>,MongoAggregationRepository {

	@Override
	List<SensorLog> findAll();

	@Query("{'id': ?0}")
	List<SensorLog> findByIdSensore(int sensorId);

	@Query("{'id': ?0},{'timeStamp': $gt: ?1}")
	List<SensorLog> findFromDate(int sensorId, Date tHold);

	// Custom Query method returning a Java 8 Stream
	// @Query("{}")
	// Stream<Sensor> findAllByCustomQueryWithStream();
}