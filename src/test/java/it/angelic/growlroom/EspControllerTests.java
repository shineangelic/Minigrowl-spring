/*
 * Copyright 2016 the original author or authors.
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorEnum;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.repositories.ActuatorsRepository;
import it.angelic.growlroom.model.repositories.SensorsRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class EspControllerTests {

	@Autowired
	private SensorsRepository repository;
	@Autowired
	private ActuatorsRepository actrepository;
	
	@Autowired
	private CommandsRepository comrepository;

	@Test
	public void testRepository() {
		Sensor emp = new Sensor();
		emp.setId(22);
		repository.save(emp);
		Assert.assertNotNull(emp.getId());

		Assert.assertTrue(repository.count() > 0);
	}
	
	@Test
	public void testActRepository() {
		Actuator emp = new Actuator();
		emp.setId(22l);
		emp.setTyp(ActuatorEnum.HUMIDIFIER);
		actrepository.save(emp);
		Assert.assertNotNull(emp.getId());

		Assert.assertTrue(actrepository.count() > 0);
	}
	@Test
	public void testComRepository() {
		Command emp = new Command();
		emp.setName("df");
		emp.setTargetActuator(new Actuator());
		emp.setParameter("1");
		emp = comrepository.save(emp);
		Assert.assertNotNull(emp.getParameter());

		Assert.assertTrue(comrepository.count() > 0);
	}
}
