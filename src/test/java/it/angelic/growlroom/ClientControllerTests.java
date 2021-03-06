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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.angelic.growlroom.model.Sensor;
import it.angelic.growlroom.model.SensorEnum;
import it.angelic.growlroom.model.repositories.SensorsRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class ClientControllerTests {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private SensorsRepository mockRepository;

	@Test
	public void testGetBoards() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/boards")).andExpect(status().isOk());
	}

	@Test
	public void testGetSensors() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/sensors/1")).andExpect(status().isOk());
	}

	@Test
	public void testPutSensor() throws Exception {

		Sensor fakeSensor = new Sensor();
		fakeSensor.setSensorId(1l);
		fakeSensor.setPid(2);
		fakeSensor.setTyp(SensorEnum.TEMPERATURE);
		when(mockRepository.save(any(Sensor.class))).thenReturn(fakeSensor);

		mockMvc.perform(put("/api/esp/v2/sensors/1/2").content(om.writeValueAsString(fakeSensor))
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				/* .andDo(print())*/
				.andExpect(status().isOk());

		verify(mockRepository, times(1)).save(any(Sensor.class));

	}

	@Test(expected = Exception.class)
	public void testErrGetSensors() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/sensors/ihsvbdd")).andExpect(status().isNotFound());
	}

	@Test
	public void testGetActuators() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/actuators/1")).andExpect(status().isOk());
	}

	@Test
	public void testGetHistory() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/sensors/1/historyChart")).andExpect(status().isOk());
	}

	@Test
	public void testGetHourChart() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/sensors/1/hourChart")).andExpect(status().isOk());
	}

	@Test
	public void testGetUptime() throws Exception {
		this.mockMvc.perform(get(
				"/api/minigrowl/v2/actuators/uptime?dataInizio=2020-05-02 00:02:23&dataFine=2020-05-21 00:02:23&actuatorId=13"))
				.andExpect(status().isOk());
	}

	/*
	 * @Test public void paramGreetingShouldReturnTailoredMessage() throws Exception {
	 * 
	 * this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
	 * .andDo(print()).andExpect(status().isOk()) .andExpect(jsonPath("$.content").value("Hello, Spring Community!")); }
	 */

}
