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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.test.context.junit4.SpringRunner;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.mongo.SensorLog;
import it.angelic.growlroom.model.repositories.MongoSensorLogRepository;

/**
 * Integration test for {@link PersonRepository}.
 *
 * @author Thomas Darimont
 * @author Oliver Gierke
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoSensorRepositoryIntegrationTest {

	@Autowired MongoSensorLogRepository repository;
	@Autowired MongoOperations operations;

	SensorLog dave;

	@Before
	public void setUp() {

		repository.deleteAll();
		dave = repository.save(new SensorLog(new Sensor()));
		 
	}

	/**
	 * Note that the all object conversions are preformed before the results are printed to the console.
	 */
	@Test
	public void shouldPerformConversionBeforeResultProcessing() {
		repository.findAll().forEach(System.out::println);
	}

	/**
	 * Note that the object conversions are preformed during stream processing as one can see from the
	 * {@link LoggingEventListener} output that is printed to the console.
	 */
	@Test
	public void shouldPerformConversionDuringJava8StreamProcessing() {

	//	try (Stream<Sensor> result = repository.findAllByCustomQueryWithStream()) {
	//		result.forEach(System.out::println);
	//	}
	}
}