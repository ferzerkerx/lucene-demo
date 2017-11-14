package com.ferzerkerx.lucenedemo.repository;

import com.ferzerkerx.lucenedemo.model.Book;
import com.ferzerkerx.lucenedemo.model.BookQuery;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
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

        FakeIndexCreator.writeToIndex(directory);

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
            finally {
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
        Term term = new Term("name", bookQuery.getName());
        builder.add(new BooleanClause(new PrefixQuery(term), BooleanClause.Occur.SHOULD));
        return builder.build();
    }


    private Book toBook(Document document) {
        return Book.builder()
                .withName(document.get("name"))
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
