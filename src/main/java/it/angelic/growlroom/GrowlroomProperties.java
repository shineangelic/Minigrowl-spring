package it.angelic.growlroom;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class GrowlroomProperties {
	 
/*	@Value("${server.address}")
	private String boundAddress;

	public String getBoundAddress() {
		return boundAddress;
	}
*/

}