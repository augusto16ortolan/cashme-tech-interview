package com.cashme.tech_project;

import com.cashme.tech_project.domain.Generated;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Generated
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		final var app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

}
