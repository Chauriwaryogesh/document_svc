package com.example;

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
@ComponentScan("com.example")
@EntityScan(value="com.example.entity")
@EnableJpaRepositories(basePackages ="com.example.repo")
@EnableAutoConfiguration
public class DocumentServiceApplication  extends SpringBootServletInitializer{
	
	public SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return  application.sources(DocumentServiceApplication.class);
	}
	
	
    public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
    }
}
