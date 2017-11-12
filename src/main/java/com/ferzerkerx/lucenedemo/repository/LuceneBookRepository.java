package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.ferzerkerx.lucenedemo.Utils.closeQuietly;


@Repository
public class LuceneBookRepository implements BookRepository, AutoCloseable {

    private static final int MAX_NUM_RESULTS = 30;

    private IndexSearcher searcher;
    private IndexReader indexReader;
    private Directory directory;
    private boolean isReady;

    @PostConstruct
    public void init() {
        directory = new RAMDirectory();

        IndexCreator.writeToIndex(directory);

        prepareIndexReader();
        isReady = true;
    }

    private void prepareIndexReader() {
        try {
            indexReader = DirectoryReader.open(directory);
        } catch (IOException e) {
            closeQuietly(directory);
        }
        if (indexReader != null) {
            try {
                searcher = new IndexSearcher(indexReader);
            } catch (Exception e) {
                closeQuietly(indexReader);
                closeQuietly(directory);
            }
        }
    }

    @Nonnull
    @Override
    public Stream<? extends Book> findBy(@Nonnull BookQuery bookQuery) {
        if (!isReady) {
            throw new IllegalStateException("Lucene is not ready.");
        }

        Query query = createLuceneQuery(bookQuery);

        return findDocuments(query)
                .map(this::toBook);

    }

    @Nonnull
    private Query createLuceneQuery(@Nonnull BookQuery bookQuery) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        Term term = new Term("name", bookQuery.getName());
        builder.add(new BooleanClause(new PrefixQuery(term), BooleanClause.Occur.SHOULD));
        return builder.build();
    }

    @Nonnull
    private Book toBook(@Nonnull Document document) {
        return Book.builder()
                .withName(document.get("name"))
                .createBook();
    }

    @Nonnull
    private Stream<Document> findDocuments(@Nonnull Query query) {
        try {
            TopDocs topDocs = searcher.search(query, MAX_NUM_RESULTS);
            return Arrays.stream(topDocs.scoreDocs)
                    .map(topDoc -> {
                        try {
                            return searcher.doc(topDoc.doc);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() throws Exception {
        closeQuietly(indexReader);
        closeQuietly(directory);
    }

    static class IndexCreator {

        private final static Logger LOG = LoggerFactory.getLogger(IndexCreator.class);

        private static void writeToIndex(@Nonnull Directory directory) {
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

        @Nonnull
        private static List<Document> createFakeDocuments() {
            return Arrays.asList(
                    createDocument("Game of thrones"),
                    createDocument("Norwegian wood"),
                    createDocument("1Q84")
            );
        }

        @Nonnull
        private static Document createDocument(@Nonnull String bookName) {
            StringField stringField = new StringField("name", bookName, Field.Store.YES);
            Document document = new Document();
            document.add(stringField);
            return document;
        }


    }
}
