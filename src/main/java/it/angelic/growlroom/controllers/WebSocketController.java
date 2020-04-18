package it.angelic.growlroom.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@Controller
public class WebSocketController {

	
	@CrossOrigin
	@MessageMapping("/actuators")
	@SendTo("/topic/actuators")
	public void subscribeActuatorsSocket() throws Exception {
		return;
	}
 

	@CrossOrigin
	@MessageMapping("/sensors")
	@SendTo("/topic/sensors")
	public void getSensorsSocket() throws Exception {
		return;
	}
}
