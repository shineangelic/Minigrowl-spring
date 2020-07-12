/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.angelic.growlroom;

import java.util.Calendar;
import java.util.Date;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.client.AggregateIterable;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorEnum;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.mongo.ActuatorLog;
import it.angelic.growlroom.model.mongo.SensorLog;
import it.angelic.growlroom.model.repositories.mongo.MongoActuatorLogRepository;
import it.angelic.growlroom.model.repositories.mongo.MongoSensorLogRepository;

/**
 * Integration test for {@link PersonRepository}.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class MongoSensorRepositoryIntegrationTest {

	@Autowired
	MongoSensorLogRepository mongoSensorLogRepository;
	@Autowired
	MongoActuatorLogRepository mongoActuatorLogRepository;
	@Autowired
	MongoOperations operations;

	SensorLog sensorLogT1;
	private ActuatorLog actuatorLog;
	 
	private Board fakeB;
	private Sensor fakeSensor1;

	@Before
	public void setUp() {
		mongoSensorLogRepository.deleteAll();
		mongoActuatorLogRepository.deleteAll();
		fakeSensor1 = new Sensor();
		fakeSensor1.setSensorId(66l);
		fakeB = new Board();
		fakeSensor1.setBoard(fakeB);
		SensorLog sl = new SensorLog(fakeSensor1);
		sl.setLogId(2l);
		sensorLogT1 = mongoSensorLogRepository.save(sl);
		
		
		ActuatorLog l2 = new ActuatorLog();
		l2.setLogId(33l);
		actuatorLog = mongoActuatorLogRepository.save(l2);

	}

	/**
	 * Note that the all object conversions are preformed before the results are printed to the console.
	 */
	@Test
	public void testFindAllSensors() {
		Assert.assertTrue(mongoSensorLogRepository.findAll().size() > 0);
	}
	
	@Test
	public void testActuatorLogSensors() {
		Actuator a = new Actuator();
		a.setBoard(fakeB);
		a.setActuatorId(5432l);
		a.setTimeStamp(new Date());
		a.setErrorPresent(false);
		a.setTyp(ActuatorEnum.FAN);
		ActuatorLog l2 = new ActuatorLog(a);
		l2.setLogId(234111l);
		mongoActuatorLogRepository.save(l2);
		
		Assert.assertNotNull(mongoActuatorLogRepository.findByActuatorId(5432l));
	}
	
	@Test
	public void testFindAllctuators() {
		Assert.assertTrue(mongoActuatorLogRepository.findAll().size() > 0);
	}

	/**
	 * Note that the object conversions are preformed during stream processing as one can see from the
	 * {@link LoggingEventListener} output that is printed to the console.
	 */
	@Test
	public void testStoriaUltimaSettimana() {
		Calendar timeStamp = Calendar.getInstance();
		timeStamp.add(Calendar.DATE, -3);
		SensorLog sl = new SensorLog(fakeSensor1);
		sl.setLogId(2l);
		sl.setTimeStamp(timeStamp.getTime());
		sensorLogT1 = mongoSensorLogRepository.save(sl);
		
		AggregateIterable<Document> ret = mongoSensorLogRepository.aggregaStoriaUltimaSettimana(66l);
		Assert.assertNotNull(ret);
		Assert.assertTrue(!ret.first().isEmpty());
	}
}