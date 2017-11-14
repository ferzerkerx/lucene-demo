package com.ferzerkerx.lucenedemo.repository;

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
import java.util.Arrays;
import java.util.List;

class FakeIndexCreator {

    private FakeIndexCreator() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(FakeIndexCreator.class);

    static void writeToIndex(Directory directory) {
        LOG.info("Started writing to index.");
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new StandardAnalyzer());
        try (IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig)) {

            for (Document document : createFakeDocuments()) {
                indexWriter.addDocument(document);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        LOG.info("Finished writing to index.");
    }


    private static List<Document> createFakeDocuments() {
        return Arrays.asList(
                createDocument("Game of thrones"),
                createDocument("Norwegian wood"),
                createDocument("1Q84")
        );
    }


    private static Document createDocument(String bookName) {
        StringField stringField = new StringField("name", bookName, Field.Store.YES);
        Document document = new Document();
        document.add(stringField);
        return document;
    }


}
