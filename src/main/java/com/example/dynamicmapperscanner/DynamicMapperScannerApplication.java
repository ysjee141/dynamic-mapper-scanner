package com.example.dynamicmapperscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@EnableConfigurationProperties
public class DynamicMapperScannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicMapperScannerApplication.class, args);
	}

}
