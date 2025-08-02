package com.SecureAccessPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@ComponentScan("com.SecureAccessPortal")
@EntityScan(value = "com.SecureAccessPortal.Entity")
@EnableJpaRepositories(basePackages = "com.SecureAccessPortal.Repo")
@EnableAutoConfiguration
public class SecureAccessPortal extends SpringBootServletInitializer {

	public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SecureAccessPortal.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SecureAccessPortal.class, args);
	}
}
