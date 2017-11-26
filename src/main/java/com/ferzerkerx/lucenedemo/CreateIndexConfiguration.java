package com.ferzerkerx.lucenedemo;

import com.ferzerkerx.lucenedemo.csv.CsvBookSupplier;
import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.repository.BookIndexCreator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Configuration
public class CreateIndexConfiguration {

    @Bean
    public Directory directory() throws Exception {
        RAMDirectory directory = new RAMDirectory();
        try (CsvBookSupplier bookSupplier = new CsvBookSupplier()) {
            try (Stream<Book> bookStream = generate(bookSupplier)) {
                BookIndexCreator.create(directory, bookStream);
            }
        }
        return directory;
    }

    private Stream<Book> generate(Supplier<Book> bookSupplier) {
        return StreamSupport.stream(new Spliterator<Book>() {
            @Override
            public boolean tryAdvance(Consumer<? super Book> action) {
                Book book = bookSupplier.get();
                if (book != null) {
                    action.accept(book);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<Book> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        }, false);
    }

}
