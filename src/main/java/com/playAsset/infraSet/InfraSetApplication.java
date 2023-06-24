package com.playAsset.infraSet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {
    "classpath:/static/property/deploy-dev.properties"
}, ignoreResourceNotFound = true)
public class InfraSetApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfraSetApplication.class, args);
	}
}