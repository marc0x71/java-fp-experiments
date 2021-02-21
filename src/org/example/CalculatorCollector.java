package org.example;

import java.util.HashMap;
import java.util.Map;

class CalculatorCollector {
    private final Map<String, Double> result = new HashMap<>();

    public Map<String, Double> getResult() {
        return result;
    }

    public void add(Pair<Map.Entry<String, Double>, Double> entry) {
        String key = entry.getKey().getKey();
        double value = result.containsKey(key) ? result.get(key) : entry.getKey().getValue();
        if (value <= 0F) return;
        double amount = entry.getValue();
        result.put(key, value - amount);
    }

    public void merge(@SuppressWarnings("unused") CalculatorCollector other) {
        throw new UnsupportedOperationException();
    }


}
