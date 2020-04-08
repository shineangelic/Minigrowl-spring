package it.angelic.growlroom;

import java.util.Date;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrowlRoomApplication {

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+2:00")); // It will set UTC timezone
		System.out.println("Spring boot timezone :" + new Date()); // It will print UTC
																								// timezone
	}

	public static void main(String[] args) {
		SpringApplication.run(GrowlRoomApplication.class, args);
	}
}
