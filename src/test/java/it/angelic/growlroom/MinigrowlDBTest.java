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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.angelic.growlroom.model.Actuator;
import it.angelic.growlroom.model.ActuatorEnum;
import it.angelic.growlroom.model.Board;
import it.angelic.growlroom.model.Command;
import it.angelic.growlroom.model.CommandsRepository;
import it.angelic.growlroom.model.QueueCommands;
import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.repositories.ActuatorsRepository;
import it.angelic.growlroom.model.repositories.BoardsRepository;
import it.angelic.growlroom.model.repositories.QueueCommandsRepository;
import it.angelic.growlroom.model.repositories.SensorsRepository;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@Import(GrowlroomProperties.class)
public class MinigrowlDBTest {

	@Autowired
	private BoardsRepository boardsRepository;
	@Autowired
	private SensorsRepository sensorsRepository;
	@Autowired
	private ActuatorsRepository actrepository;
	@Autowired
	private CommandsRepository comrepository;
	@Autowired
	private QueueCommandsRepository queueCommandsRepository;
	
	private Board createFakeBoard() {
		Board ret = new Board();
		ret.setBoardId(1l);
		
		return boardsRepository.saveAndFlush(ret);
	}
	
	@Test
	public void testBRepository() {
		Board ret = createFakeBoard();
		Assert.assertTrue(ret.getBoardActuators() != null);
		Assert.assertTrue(ret.getBoardSensors() != null);
	}

	@Test
	public void testRepository() {
		Sensor emp = new Sensor();
		emp.setPid(22);
		emp.setBoard(createFakeBoard());
		sensorsRepository.save(emp);
		Assert.assertNotNull(emp.getPid());
		Assert.assertTrue(sensorsRepository.count() > 0);
	}
	
	@Test
	public void testActRepository() {
		Actuator emp = new Actuator();
		emp.setPid(22);
		emp.setTyp(ActuatorEnum.HUMIDIFIER);
		emp.setBoard(createFakeBoard());
		List<Command> xlist =new ArrayList<>();
		xlist.add(createFakeCommand(emp));
		emp.setSupportedCommands(xlist);
		
		actrepository.save(emp);
		Assert.assertNotNull(emp.getPid());
		Assert.assertTrue(actrepository.count() > 0);
	}
	 
	@Test(expected=DataIntegrityViolationException.class)
	public void testActClashRepository() {
		Actuator emp = new Actuator();
		emp.setPid(22);
		emp.setTyp(ActuatorEnum.HUMIDIFIER);
		Board b=createFakeBoard();
		emp.setBoard(b);
		 
		Actuator emp2 = new Actuator();
		emp2.setPid(22);
		emp2.setTyp(ActuatorEnum.FAN);
		emp2.setBoard(b);
		 
		actrepository.save(emp);
		Assert.assertTrue(actrepository.count() > 0);
		actrepository.save(emp2);
		Assert.assertTrue(actrepository.count() > 1);
	}
	
	@Test
	public void testCommandsRepository() {
		Actuator emp = new Actuator();
		emp.setPid(22);
		actrepository.save(emp);
		Command ret = createFakeCommand(emp);
		
		Command saved = comrepository.save(ret);
		Assert.assertNotNull(comrepository.findById(saved.getCommandId()));
	}
	 
	private Command createFakeCommand(Actuator a) {
		Command emp = new Command();
		emp.setName("df");
		emp.setIdOnQueue(null);
		emp.setParameter("alfa");
		emp.setTargetActuator(a);
		emp.setParameter("1");
		return emp;
	}
	
	@Test
	public void testQueueRepository() {
		Actuator emp = new Actuator();
		emp.setPid(33);
		actrepository.save(emp);
		Command ret = createFakeCommand(emp);
		Command saved = comrepository.save(ret);
		QueueCommands q1 = new QueueCommands(saved);
		
		QueueCommands s1 = queueCommandsRepository.save(q1);
		Assert.assertNotNull(s1.getIdQueueCommand());
	}
}
