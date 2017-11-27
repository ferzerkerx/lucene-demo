package com.ferzerkerx.lucenedemo.config;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateIndexConfiguration {

    @Bean
    public Directory directory() {
        return new RAMDirectory();
    }
}
