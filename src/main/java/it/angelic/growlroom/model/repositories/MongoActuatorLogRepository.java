package it.angelic.growlroom.model.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.angelic.growlroom.model.mongo.ActuatorLog;

public interface MongoActuatorLogRepository extends MongoRepository<ActuatorLog, String> {

	@Override
	List<ActuatorLog> findAll();

	@Query("{'id': ?0}")
	List<ActuatorLog> findByActuatorId(int actId);

	@Query("{'id': ?0},{'timeStamp': $gt: ?1}")
	List<ActuatorLog> findFromDate(int sensorId, Date tHold);
 
}