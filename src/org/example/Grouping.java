package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Grouping {
    public static void main(String[] args) {
        List<String> stringList = List.of("ooo122ppp", "aaa122b333", "zzz122bmmm", "ccc9o9i333", "mmm9o9i111", "qqqQmQm888", "777QmQmlll", "vvvjjj1sss");
        Map<String, List<String>> collection = stringList.stream().collect(Collectors.groupingBy(ex -> ex.substring(3, 7)));
        for (Map.Entry<String, List<String>> entry : collection.entrySet()) {
            System.out.println("group <" + entry.getKey() + "> strings " + entry.getValue());
        }

    }
}
