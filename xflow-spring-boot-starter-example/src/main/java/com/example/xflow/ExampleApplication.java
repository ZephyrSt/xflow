package com.example.xflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
//        SpringApplication.run(SystemApplication.class, args);
        SpringApplication app = new SpringApplication(ExampleApplication.class);
        ConfigurableApplicationContext application=app.run(args);
        Environment env = application.getEnvironment();
    }

}
