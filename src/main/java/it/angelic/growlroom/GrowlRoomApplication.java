package it.angelic.growlroom;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GrowlRoomApplication {

	@PostConstruct
	public void init() {
	}

	public static void main(String[] args) {
		SpringApplication.run(GrowlRoomApplication.class, args);
	}
}
