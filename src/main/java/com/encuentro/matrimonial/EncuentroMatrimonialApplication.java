package com.encuentro.matrimonial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableAutoConfiguration
@Configuration
public class EncuentroMatrimonialApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncuentroMatrimonialApplication.class, args);
	}

}
