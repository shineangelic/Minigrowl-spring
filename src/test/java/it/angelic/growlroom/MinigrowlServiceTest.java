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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorEnum;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorEnum;
import it.angelic.growlroom.model.repositories.BoardsRepository;
import it.angelic.growlroom.service.ActuatorsService;
import it.angelic.growlroom.service.SensorNotFoundException;
import it.angelic.growlroom.service.SensorsService;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = GrowlRoomApplication.class)
@Import(GrowlroomProperties.class)
public class MinigrowlServiceTest {
 
	@Autowired
	private SensorsService sensorsService;
	@Autowired
	private ActuatorsService actuatorsService;
	@Autowired
	private BoardsRepository boardsRepository;
	 
	private Board createFakeBoard(Long id) {
		Board ret = new Board();
		ret.setBoardId(id);

		return boardsRepository.saveAndFlush(ret);
	}

	@Test(expected = SensorNotFoundException.class)
	public void testSensorNotFoundException() throws SensorNotFoundException {
		Sensor got = sensorsService.getSensorByBoardIdAndPid(666L, -3);
	}
	
	
	@Test 
	public void testUpdateCreateSensor() throws SensorNotFoundException {
		Sensor emp = new Sensor();
		emp.setPid(22);
		emp.setTyp(SensorEnum.HUMIDITY);
		emp.setBoard(createFakeBoard(456l));
		Sensor t2 = sensorsService.createOrUpdateBoardSensor(emp, "4", "22");
		Assert.assertNotNull(t2.getPid());
		
		Sensor t3 =sensorsService.createOrUpdateBoardSensor(t2, "4", "22");
		Assert.assertEquals(t2, t3);
	}
	
	@Test 
	public void testGetSensorByBoardIdAndPid() throws SensorNotFoundException {
		Sensor emp = new Sensor();
		emp.setPid(22);
		emp.setTyp(SensorEnum.HUMIDITY);
		emp.setBoard(createFakeBoard(567l));
		Sensor t2 = sensorsService.createOrUpdateBoardSensor(emp, "4", "22");
		Assert.assertNotNull(t2.getPid());
		
		Sensor t3 =sensorsService.getSensorByBoardIdAndPid(4L, 22);
		Assert.assertEquals(t2, t3);
	}
 
	@Test 
	public void testUpdateCreateActuator() throws SensorNotFoundException {
		Actuator emp = new Actuator();
		emp.setPid(22);
		emp.setTyp(ActuatorEnum.LIGHT);
		emp.setBoard(createFakeBoard(678l));
		Actuator t2 = actuatorsService.createOrUpdateBoardActuator(emp, "4", "22");
		Assert.assertNotNull(t2.getPid());
		
		Actuator t3 =actuatorsService.createOrUpdateBoardActuator(t2, "4", "22");
		Assert.assertEquals(t2, t3);
	}
	
}
