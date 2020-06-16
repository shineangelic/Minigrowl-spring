package it.angelic.growlroom;

import javax.annotation.PostConstruct;

import org.apache.catalina.filters.RemoteAddrFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GrowlRoomApplication {

	@Autowired(required=false)
	GrowlroomProperties props;
	
	@PostConstruct
	public void init() {
	}

	public static void main(String[] args) {
		SpringApplication.run(GrowlRoomApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean remoteAddressFilter() {

		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		RemoteAddrFilter filter = new RemoteAddrFilter();

		filter.setAllow(props.getListenFilter());
		filter.setDenyStatus(404);

		filterRegistrationBean.setFilter(filter);
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;

	}

}
