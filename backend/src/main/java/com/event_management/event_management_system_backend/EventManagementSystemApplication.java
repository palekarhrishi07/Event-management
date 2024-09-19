package com.event_management.event_management_system_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.event_management.event_management_system_backend")
public class EventManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventManagementSystemApplication.class, args);
		@ComponentScan(basePackages = "com.event_management.event_management_system_backend.mapper")
		class AppConfig{
		}
		}
	}


