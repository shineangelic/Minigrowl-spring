package it.angelic.growlroom.model.repositories.mongo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import it.angelic.growlroom.model.mongo.ActuatorLog;

public interface MongoActuatorLogRepository extends MongoRepository<ActuatorLog, String> {

	@Override
	List<ActuatorLog> findAll();

	@Query("{'actuatorId': ?0}")
	List<ActuatorLog> findByActuatorId(long actId);
	
	@Query("{'actuatorId': ?0}")
	ActuatorLog findLastByActuatorId(int actId);

	@Query("{'actuatorId': ?0},{'timeStamp': $gt: ?1}")
	List<ActuatorLog> findFromDate(int sensorId, Date tHold);
	 
 
}