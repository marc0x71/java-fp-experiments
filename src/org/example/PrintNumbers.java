package org.example;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PrintNumbers {

    public static void main(String[] args) {
        List<Integer> numbers = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        printNumbersLimited(numbers, 80);
    }

    private static void printNumbersLimited(List<Integer> numbers, int width) {
        System.out.println(numbers.stream()
                .map(number -> String.format("%d ", number))
                .collect(() -> new NumberFormatter(width), NumberFormatter::append, NumberFormatter::merge).complete());
    }

    private static class NumberFormatter {
        private final int width;
        private int length;
        private final StringBuilder builder = new StringBuilder();

        public NumberFormatter(int width) {
            this.width = width;
        }

        public String complete() {
            return (builder.toString());
        }

        public void append(String s) {
            if (length + s.length() > width) {
                length = 0;
                builder.append('\n');
            }
            builder.append(s).append(" ");
            length += s.length();
        }

        public void merge(NumberFormatter other) {
            throw new UnsupportedOperationException();
        }
    }
}
