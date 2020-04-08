package it.angelic.growlroom.controllers;

import java.io.FileNotFoundException;
import java.util.Date;

import javax.print.PrintException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * L'esp periodicamente contatta il cloud, invia lo stato dei sensori e degli attuatori. Legge una list per scaricare i
 * comandi da eseguire. E` stupido
 * 
 * @author Ale
 *
 */
@RestController
@RequestMapping(value = "/api/esp/v1/")
public class ESPTimeController {

	@CrossOrigin
	@RequestMapping(value = "/getTime", method = RequestMethod.GET)
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public Date getTime() throws FileNotFoundException, PrintException {
		// will return UTC
		return new Date();
	}

}
