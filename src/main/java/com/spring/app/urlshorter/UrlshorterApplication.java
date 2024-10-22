package com.spring.app.urlshorter;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UrlshorterApplication {

	public static void main(String[] args) {
		if (args.length > 0 && "migration".equalsIgnoreCase(args[0])) {
			runMigrations();
		} else {
			SpringApplication.run(UrlshorterApplication.class, args);
		}
	}

	private static void runMigrations() {
		// Create and configure Flyway
		Flyway flyway = Flyway.configure()
				.dataSource("jdbc:postgresql://localhost:5432/url_shortener", "postgres", "postgres")
				.load();

		// Start the migration
		flyway.migrate();

		System.out.println("Flyway migrations completed.");
	}
}
