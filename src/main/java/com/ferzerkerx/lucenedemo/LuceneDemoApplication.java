package com.ferzerkerx.lucenedemo;

import com.ferzerkerx.lucenedemo.config.AppConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(AppConfiguration.class)
public class LuceneDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuceneDemoApplication.class, args);
    }
}
