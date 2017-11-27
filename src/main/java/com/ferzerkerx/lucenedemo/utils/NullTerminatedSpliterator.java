package com.ferzerkerx.lucenedemo.utils;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NullTerminatedSpliterator<T> implements Spliterator<T> {
    private final Supplier<T> tSupplier;

    public NullTerminatedSpliterator(Supplier<T> tSupplier) {
        this.tSupplier = tSupplier;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
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
