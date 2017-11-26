package com.ferzerkerx.lucenedemo.csv;

import com.ferzerkerx.lucenedemo.model.Book;
import com.opencsv.CSVReader;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static com.ferzerkerx.lucenedemo.Utils.closeQuietly;
import static com.ferzerkerx.lucenedemo.Utils.resolvePath;

public class CsvBookSupplier implements Supplier<Book>, AutoCloseable {
    private static final int TITLE_COLUMN_INDEX = 0;
    private static final int AUTHOR_COLUMN_INDEX = 1;
    private static final String BOOKS_CSV = "books.csv";

    private final FileReader fileReader;
    private final CSVReader csvReader;

    public CsvBookSupplier() throws IOException {
        Path pathToFile = resolvePath(CsvBookSupplier.class, BOOKS_CSV);
        try {
            fileReader = new FileReader(pathToFile.toFile());
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        csvReader = new CSVReader(fileReader);
    }

    private static Book toBook(String[] line) {
        Objects.requireNonNull(line);
        String title = line[TITLE_COLUMN_INDEX];
        String author = line[AUTHOR_COLUMN_INDEX];
        return Book.builder()
                .withTitle(title)
                .withAuthor(author)
                .createBook();
    }

    @Override
    @Nullable
    public Book get() {
        return readNextLine()
                .map(CsvBookSupplier::toBook)
                .orElse(null);
    }

    private Optional<String[]> readNextLine() {
        try {
            return Optional.ofNullable(csvReader.readNext());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
        public void close() throws Exception {
            closeQuietly(csvReader);
            closeQuietly(fileReader);
        }

}
