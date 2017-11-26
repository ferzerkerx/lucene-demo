package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.csv.CsvBookSupplier;
import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.ferzerkerx.lucenedemo.Utils.closeQuietly;


@Repository
public class LuceneBookRepository implements BookRepository, AutoCloseable {

    private static final int MAX_NUM_RESULTS = 30;

    private IndexSearcher searcher;
    private IndexReader indexReader;
    private Directory directory;
    private boolean isReady;

    @PostConstruct
    public void init() throws Exception {
        directory = new RAMDirectory();

        try (CsvBookSupplier bookSupplier = new CsvBookSupplier()) {
            try (Stream<Book> bookStream = generate(bookSupplier)) {
                BookIndexCreator.create(directory, bookStream);
            }
        }

        prepareIndexReader();
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

    private void prepareIndexReader() {
        try {
            indexReader = DirectoryReader.open(directory);
        } catch (IOException e) {
            closeQuietly(directory);
        }
        if (indexReader != null) {
            try {
                searcher = new IndexSearcher(indexReader);
                isReady = true;
            } catch (Exception e) {
                closeQuietly(indexReader);
                closeQuietly(directory);
                indexReader = null;
                searcher = null;
            }
        }
    }


    @Override
    public Stream<? extends Book> findBy(BookQuery bookQuery) {
        if (!isReady) {
            throw new IllegalStateException("Lucene is not ready.");
        }

        Query query = createLuceneQuery(bookQuery);

        return findDocuments(query)
                .map(this::toBook);

    }

    private Query createLuceneQuery(BookQuery bookQuery) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        if (bookQuery.getAuthor().isPresent()) {
            builder.add(createBooleanClause("author", bookQuery.getAuthor().get()));
        }
        if (bookQuery.getTitle().isPresent()) {
            builder.add(createBooleanClause("title", bookQuery.getTitle().get()));
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
        closeQuietly(directory);
        indexReader = null;
        directory =  null;
    }
}
