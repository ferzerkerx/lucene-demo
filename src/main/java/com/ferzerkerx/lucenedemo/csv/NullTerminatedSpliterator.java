package com.ferzerkerx.lucenedemo.csv;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class NullTerminatedSpliterator<T> implements Spliterator<T> {
    private final Supplier<T> tSupplier;

    NullTerminatedSpliterator(Supplier<T> tSupplier) {
        requireNonNull(tSupplier);
        this.tSupplier = tSupplier;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        requireNonNull(tSupplier);
        T value = tSupplier.get();
        if (value != null) {
            action.accept(value);
            return true;
        }
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {
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
}
