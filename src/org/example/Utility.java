package org.example;

import java.util.List;
import java.util.stream.Stream;

public class Utility {
    public static <A, B> Stream<Pair<A, B>> pairify(A a, List<B> b) {
        return b.stream()
                .map(x -> Pair.of(a, x));
    }

}
