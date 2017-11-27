package com.ferzerkerx.lucenedemo.service;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.utils.NullTerminatedSpliterator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BookIndexCreator {

    private static final Logger LOG = LoggerFactory.getLogger(BookIndexCreator.class);

    private BookIndexCreator() {
    }

    public static void create(Directory directory, Supplier<Book> bookSupplier) {
        try (Stream<Book> bookStream = generate(bookSupplier)) {
            LOG.info("Started writing to index.");
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());

            try (IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig)) {
                bookStream
                        .map(BookIndexCreator::toDocument)
                        .forEach(document -> writeToIndex(indexWriter, document));
            }

            LOG.info("Finished writing to index.");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static Stream<Book> generate(Supplier<Book> bookSupplier) {
        return StreamSupport.stream(new NullTerminatedSpliterator<>(bookSupplier), false);
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
}
