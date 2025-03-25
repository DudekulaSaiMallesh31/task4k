package com.example.taskk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.taskk") // Ensure all components are scanned
public class TaskkApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskkApplication.class, args);
    }
}
