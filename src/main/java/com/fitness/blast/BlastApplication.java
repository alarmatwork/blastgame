package com.fitness.blast;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = { "com.fitness.blast" })
public class BlastApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlastApplication.class, args);
	}


	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() {

		return new RestTemplate(this.clientHttpRequestFactory());
	}


	private ClientHttpRequestFactory clientHttpRequestFactory() {

		log.info("Registering ClientHttpRequestFactory ...");

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(3000);
		factory.setConnectTimeout(1500);

		log.info("ClientHttpRequestFactory initialized ... ");

		return factory;
	}

}
