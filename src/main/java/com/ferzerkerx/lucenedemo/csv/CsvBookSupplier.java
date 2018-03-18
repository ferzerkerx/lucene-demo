package com.ferzerkerx.lucenedemo.csv;

import com.ferzerkerx.lucenedemo.model.Book;
import com.opencsv.CSVReader;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.closeQuietly;
import static java.util.Objects.requireNonNull;

public class CsvBookSupplier implements Supplier<Book>, AutoCloseable {

    private final FileReader fileReader;

    private final CSVReader csvReader;
    public CsvBookSupplier(Path pathToFile) {
        requireNonNull(pathToFile);
        try {
            fileReader = new FileReader(pathToFile.toFile());
        } catch (FileNotFoundException e) {
            throw new UncheckedIOException(e);
        }
        csvReader = new CSVReader(fileReader);
    }

    public Stream<Book> stream() {
        return StreamSupport.stream(new NullTerminatedSpliterator<>(this), false);
    }

    @Override
    @Nullable
    public Book get() {
        return readNextLine()
                .map(CsvBookSupplier::toBook)
                .orElse(null);
    }

    @Override
    public void close() {
        closeQuietly(csvReader);
        closeQuietly(fileReader);
    }

    private static Book toBook(String[] line) {
        requireNonNull(line);
        String title = line[Columns.TITLE.getIndex()];
        String author = line[Columns.AUTHOR.getIndex()];
        return Book.builder()
                .withTitle(title)
                .withAuthor(author)
                .createBook();
    }

    private Optional<String[]> readNextLine() {
        try {
            return Optional.ofNullable(csvReader.readNext());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private enum Columns {
        TITLE(0), AUTHOR(1);
        private final int index;

        Columns(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }
}

