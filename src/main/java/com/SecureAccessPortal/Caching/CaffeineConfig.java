package com.SecureAccessPortal.Caching;

import java.time.Duration;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configurable
@EnableCaching
public class CaffeineConfig {
	
	@Bean
	public Caffeine<Object , Object> caffeineConfig(){
		return Caffeine.newBuilder().maximumSize(10)
				.expireAfterWrite(Duration.ofMinutes(10))
				.recordStats();
		
	}
	
	@Bean
	public CacheManagerCustomizers myCustomizer() {
	    return new CacheManagerCustomizers(Collections.emptyList());
	}


}
