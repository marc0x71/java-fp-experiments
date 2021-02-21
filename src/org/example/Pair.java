package org.example;

public class Pair<K, T> {
    private final K key;
    private final T value;

    private Pair(K key, T value) {
        this.key = key;
        this.value = value;
    }

    public static <K, T> Pair<K, T> of(K key, T value) {
        return new Pair<>(key, value);
    }

    public K getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "" + key +
                "," + value +
                '}';
    }
}
