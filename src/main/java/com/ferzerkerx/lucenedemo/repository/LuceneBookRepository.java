package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import com.ferzerkerx.lucenedemo.utils.LuceneBookIndexUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.closeQuietly;
import static com.ferzerkerx.lucenedemo.utils.LuceneBookIndexUtils.createPrefixQuery;
import static java.util.Objects.requireNonNull;


@Repository
public class LuceneBookRepository implements BookRepository, AutoCloseable {

    private static final int MAX_NUM_RESULTS = 30;

    private IndexSearcher searcher;
    private IndexReader indexReader;
    private final boolean isReady;

    public LuceneBookRepository(Directory directory) throws IOException {
        requireNonNull(directory);
        indexReader = DirectoryReader.open(directory);
        try {
            searcher = new IndexSearcher(indexReader);
        } catch (Exception e) {
            closeQuietly(indexReader);
            indexReader = null;
            searcher = null;
            throw e;
        }
        isReady = true;
    }

    @Override
    public Stream<Book> findBy(BookQuery bookQuery) {
        requireNonNull(bookQuery);
        if (!isReady) {
            throw new IllegalStateException("Lucene is not ready.");
        }

        Query query = createLuceneQuery(bookQuery);

        return findDocuments(query)
                .map(LuceneBookIndexUtils::toBook);
    }

    @Override
    public void close() {
        closeQuietly(indexReader);
        indexReader = null;
        searcher = null;
    }

    private Query createLuceneQuery(BookQuery bookQuery) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        bookQuery.getAuthor()
                .ifPresent(author -> builder.add(createBooleanClause("author", author)));

        bookQuery.getTitle()
                .ifPresent(title -> builder.add(createBooleanClause("title", title)));

        return builder.build();
    }

    private static BooleanClause createBooleanClause(String fieldName, String fieldValue) {
        PrefixQuery prefixQuery = createPrefixQuery(fieldName, fieldValue);
        return new BooleanClause(prefixQuery, BooleanClause.Occur.SHOULD);
    }

    private Stream<Document> findDocuments(Query query) {
        try {
            TopDocs topDocs = searcher.search(query, MAX_NUM_RESULTS);
            return Arrays.stream(topDocs.scoreDocs)
                    .map(this::toDocument);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Document toDocument(ScoreDoc topDoc) {
        try {
            return searcher.doc(topDoc.doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
