package it.angelic.growlroom.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorLog;

public interface MongoSensorLogRepository extends MongoRepository<SensorLog, String> {

	@Override
	List<SensorLog> findAll();

	Optional<SensorLog> findById(String name);

	@Query("{typ:'?0'}")
	List<Sensor> findByTyp(String address);

	// Custom Query method returning a Java 8 Stream
	// @Query("{}")
	// Stream<Sensor> findAllByCustomQueryWithStream();
}