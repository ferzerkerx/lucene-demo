package com.ferzerkerx.lucenedemo;

import com.ferzerkerx.lucenedemo.config.CreateIndexConfig;
import com.ferzerkerx.lucenedemo.csv.CsvBookSupplier;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.nio.file.Path;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.resolvePath;
import static com.ferzerkerx.lucenedemo.utils.LuceneBookIndexUtils.createIndex;

@SpringBootApplication
@Import({CreateIndexConfig.class})
public class LuceneDemoApplication {

    private static final String BOOKS_CSV = "books.csv";

    @Autowired
    public LuceneDemoApplication(Directory directory) {
        Path pathToFile = resolvePath(LuceneDemoApplication.class, BOOKS_CSV);
        try (CsvBookSupplier csvBookSupplier = new CsvBookSupplier(pathToFile)) {
            createIndex(directory, csvBookSupplier);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(LuceneDemoApplication.class, args);
    }
}
