package com.ferzerkerx.lucenedemo;

import com.ferzerkerx.lucenedemo.config.CreateIndexConfiguration;
import com.ferzerkerx.lucenedemo.csv.CsvBookSupplier;
import com.ferzerkerx.lucenedemo.service.BookIndexCreator;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.nio.file.Path;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.resolvePath;

@SpringBootApplication
@Import({CreateIndexConfiguration.class})
public class LuceneDemoApplication {

    private static final String BOOKS_CSV = "books.csv";

    @Autowired
    public LuceneDemoApplication(Directory directory) throws IOException {
        Path pathToFile = resolvePath(LuceneDemoApplication.class, BOOKS_CSV);
        try (CsvBookSupplier csvBookSupplier = new CsvBookSupplier(pathToFile)) {
            BookIndexCreator.create(directory, csvBookSupplier);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(LuceneDemoApplication.class, args);
    }
}
