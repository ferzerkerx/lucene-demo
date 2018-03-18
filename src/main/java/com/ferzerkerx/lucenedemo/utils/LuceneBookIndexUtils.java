package com.ferzerkerx.lucenedemo.utils;

import com.ferzerkerx.lucenedemo.csv.CsvBookSupplier;
import com.ferzerkerx.lucenedemo.model.Book;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public final class LuceneBookIndexUtils {

    private static final Logger LOG = LoggerFactory.getLogger(LuceneBookIndexUtils.class);

    private LuceneBookIndexUtils() {
        throw new AssertionError();
    }

    public static void createIndex(Directory directory, CsvBookSupplier csvBookSupplier) {
        requireNonNull(directory);
        requireNonNull(csvBookSupplier);
        try (Stream<Book> bookStream = csvBookSupplier.stream()) {
            LOG.info("Started writing to index.");
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());

            try (IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig)) {
                bookStream
                        .map(LuceneBookIndexUtils::toDocument)
                        .forEach(document -> writeToIndex(indexWriter, document));
            }

            LOG.info("Finished writing to index.");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void writeToIndex(IndexWriter indexWriter, Document document) {
        try {
            indexWriter.addDocument(document);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Document toDocument(Book book) {
        StringField titleField = new StringField("title", book.getTitle(), Field.Store.YES);
        StringField authorField = new StringField("author", book.getAuthor(), Field.Store.YES);
        Document document = new Document();
        document.add(titleField);
        document.add(authorField);
        return document;
    }

    public static PrefixQuery createPrefixQuery(String fieldName, String termValue) {
        requireNonNull(fieldName);
        requireNonNull(termValue);
        Term term = new Term(fieldName, termValue);
        return new PrefixQuery(term);
    }

    public static Book toBook(Document document) {
        requireNonNull(document);
        return Book.builder()
                .withTitle(document.get("title"))
                .withAuthor(document.get("author"))
                .createBook();
    }
}
