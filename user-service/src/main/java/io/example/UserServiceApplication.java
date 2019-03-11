package io.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
*/

@SpringBootApplication
public class UserServiceApplication {

	/*
	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		  return new HttpComponentsClientHttpRequestFactory();
	}
	
	@Bean(name="myRestTemplate")
	@Primary
	public RestTemplate restTemplate() {
		 return new RestTemplate(httpRequestFactory());
	}
	*/

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
