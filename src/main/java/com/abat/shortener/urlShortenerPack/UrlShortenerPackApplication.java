package com.abat.shortener.urlShortenerPack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlShortenerPackApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerPackApplication.class, args);
	}

}
