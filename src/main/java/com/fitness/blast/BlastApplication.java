package com.fitness.blast;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BlastApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlastApplication.class, args);
        System.out.print("It works!");
	}
}
