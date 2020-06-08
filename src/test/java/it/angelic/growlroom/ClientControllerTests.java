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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
public class ClientControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testGetBoards() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/boards")).andExpect(status().isOk());
	}
	
	@Test
	public void testGetSensors() throws Exception {
		this.mockMvc.perform(get("/api/minigrowl/v2/sensors/1")).andExpect(status().isOk());
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
		this.mockMvc.perform(get("/api/minigrowl/v2/actuators/uptime?dataInizio=2020-05-02+00:02:23&dataFine=2020-05-21+00:02:23&actuatorId=13")).andExpect(status().isOk());
	}

	
	/*
	 * @Test public void paramGreetingShouldReturnTailoredMessage() throws Exception {
	 * 
	 * this.mockMvc.perform(get("/greeting").param("name", "Spring Community"))
	 * .andDo(print()).andExpect(status().isOk()) .andExpect(jsonPath("$.content").value("Hello, Spring Community!")); }
	 */

}
