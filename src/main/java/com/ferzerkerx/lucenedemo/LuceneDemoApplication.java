package com.ferzerkerx.lucenedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LuceneDemoApplication {

    private LuceneDemoApplication() {
    }

    public static void main(String[] args) {
        SpringApplication.run(LuceneDemoApplication.class, args);
    }
}
