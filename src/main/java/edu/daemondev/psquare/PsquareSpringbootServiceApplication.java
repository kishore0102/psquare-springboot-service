package edu.daemondev.psquare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PsquareSpringbootServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PsquareSpringbootServiceApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
		AuthFilter authFilter = new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/notes/*");
		registrationBean.addUrlPatterns("/api/calendar/*");
		registrationBean.addUrlPatterns("/api/user/changePassword");
		return registrationBean;
	}

}
