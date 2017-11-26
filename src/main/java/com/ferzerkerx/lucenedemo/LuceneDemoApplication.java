package com.ferzerkerx.lucenedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(CreateIndexConfiguration.class)
public class LuceneDemoApplication {

    public LuceneDemoApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(LuceneDemoApplication.class, args);
    }
}
