package org.example;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.Utility.pairify;

public class WorkWithMaps {
    public static void main(String[] args) {
        System.out.println("imperative = " + imperative());
        System.out.println("functionalWithCollector = " + functionalWithCollector());
        System.out.println("functionalRecursive = " + functionalRecursive());
        System.out.println("functionalWithReduce = " + functionalWithReduce());
    }


    private static Map<String, Double> functionalWithReduce() {
        // https://stackoverflow.com/questions/66302292/changing-value-in-a-map-the-right-way/66302586?noredirect=1#comment117222541_66302586
        Map<String, Double> input = prepareInput();
        Map<String, List<Function<Double, Double>>> rules = prepareFunctions();

        return input.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> rules.getOrDefault(e.getKey(), Collections.emptyList())
                                .stream()
                                .reduce(
                                        e.getValue(), // initial value from input
                                        (res, fun) -> res > 0 ? res - fun.apply(res) : res, // accumulate
                                        (res1, res2) -> res2 // combine
                                )
                ));
    }

    private static Map<String, Double> functionalWithCollector() {
        Map<String, Double> input = prepareInput();
        Map<String, List<Function<Double, Double>>> rules = prepareFunctions();

        return input.entrySet().stream()
                .flatMap(e -> pairify(e, rules.get(e.getKey())))
                .map(e -> Pair.of(e.getKey(), e.getValue().apply(e.getKey().getValue())))
                .collect(CalculatorCollector::new, CalculatorCollector::add, CalculatorCollector::merge).getResult();
    }

    private static Map<String, Double> functionalRecursive() {
        Map<String, Double> input = prepareInput();
        Map<String, List<Function<Double, Double>>> rules = prepareFunctions();

        Map<String, List<Function<Double, Double>>> functions = rules.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return input.entrySet().stream()
                .map(e -> calculator(e, functions.get(e.getKey())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static Pair<String, Double> calculator(Map.Entry<String, Double> input, List<Function<Double, Double>> entries) {
        double value = input.getValue();
        double result = calculator(value, entries.iterator());
        return Pair.of(input.getKey(), result);
    }

    private static double calculator(double value, Iterator<Function<Double, Double>> iterator) {
        if (!iterator.hasNext() || value <= 0F) return value;
        Function<Double, Double> function = iterator.next();
        double cost = function.apply(value);
        return calculator(value - cost, iterator);
    }

    private static Map<String, Double> imperative() {
        Map<String, Double> input = prepareInput();
        Map<String, List<Function<Double, Double>>> rules = prepareFunctions();

        for (Map.Entry<String, List<Function<Double, Double>>> entry : rules.entrySet()) {
            String inName = entry.getKey();
            Double inValue = input.get(inName);
            if (inValue == null) continue;
            for (Function<Double, Double> function : entry.getValue()) {
                inValue -= function.apply(inValue);
                if (inValue <= 0F) {
                    break;
                }
            }
            input.put(inName, inValue);
        }

        return input;
    }

    private static Map<String, List<Function<Double, Double>>> prepareFunctions() {
        return Map.of(
                "IN1", List.of(value -> value / 10.0, value -> 90.0, value -> 10.0),
                "IN2", List.of(value -> value / 2.0),
                "IN3", List.of(value -> value / 5.0));
    }

    private static Map<String, Double> prepareInput() {
        return new HashMap<>() {{
            put("IN1", 100.0);
            put("IN2", 10.0);
        }};
    }


}
