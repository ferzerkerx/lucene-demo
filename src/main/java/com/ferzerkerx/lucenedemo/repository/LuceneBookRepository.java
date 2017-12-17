package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.Optional;

import static com.ferzerkerx.lucenedemo.utils.FileUtils.closeQuietly;


@Repository
public class LuceneBookRepository implements BookRepository, AutoCloseable {

    private static final int MAX_NUM_RESULTS = 30;

    private IndexSearcher searcher;
    private IndexReader indexReader;
    private final boolean isReady;

    public LuceneBookRepository(Directory directory) throws IOException {
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
        if (!isReady) {
            throw new IllegalStateException("Lucene is not ready.");
        }

        Query query = createLuceneQuery(bookQuery);

        return findDocuments(query)
                .map(this::toBook);

    }

    private Query createLuceneQuery(BookQuery bookQuery) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        Optional<String> optionalAuthor = bookQuery.getAuthor();
        if (optionalAuthor.isPresent()) {
            builder.add(createBooleanClause("author", optionalAuthor.get()));
        }

        Optional<String> optionalTitle = bookQuery.getTitle();
        if (optionalTitle.isPresent()) {
            builder.add(createBooleanClause("title", optionalTitle.get()));
        }

        return builder.build();
    }

    private BooleanClause createBooleanClause(String fieldName, String fieldValue) {
        PrefixQuery authorPrefix = createPrefixQuery(fieldName, fieldValue);
        return new BooleanClause(authorPrefix, BooleanClause.Occur.SHOULD);
    }

    private PrefixQuery createPrefixQuery(String fieldName, String termValue) {
        Term term = new Term(fieldName, termValue);
        return new PrefixQuery(term);
    }


    private Book toBook(Document document) {
        return Book.builder()
                .withTitle(document.get("title"))
                .withAuthor(document.get("author"))
                .createBook();
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

    @Override
    public void close() throws Exception {
        closeQuietly(indexReader);
        indexReader = null;
        searcher = null;
    }
}
