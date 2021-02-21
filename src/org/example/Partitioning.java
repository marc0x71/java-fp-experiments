package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Partitioning {

    public static void main(String[] args) {
        Map<Boolean, Long> result = List.of(4, 13, 10, 21, 20).stream()
                .collect(Collectors.partitioningBy(e -> e % 2 == 0, Collectors.counting()));

        System.out.println("result = " + result); // result = {false=2, true=3}

    }
}
