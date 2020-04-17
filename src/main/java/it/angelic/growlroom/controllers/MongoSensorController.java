package it.angelic.growlroom.controllers;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;

import com.mongodb.client.MongoClients;

import it.angelic.growlroom.model.Sensor;

@Controller
public class MongoSensorController {

	public void logSensor(Sensor in) {
		MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "growLog");
		mongoOps.insert(in);

		//log.info(mongoOps.findOne(new Query(where("name").is("Joe")), Person.class));

		mongoOps.dropCollection("person");
	}

}
