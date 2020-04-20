package it.angelic.growlroom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class GrowlroomProperties {
	
	@Value("${server.listen.filter}")
	private String listenFilter;

	public String getListenFilter() {
		return listenFilter;
	}
	 

}