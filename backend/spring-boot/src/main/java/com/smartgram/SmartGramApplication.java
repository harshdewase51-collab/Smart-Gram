package com.smartgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartGramApplication {
    public static void main(String[] args) {
        System.out.println("🌾 Starting SmartGram Spring Boot Application on port 5002...");
        SpringApplication.run(SmartGramApplication.class, args);
    }
}
