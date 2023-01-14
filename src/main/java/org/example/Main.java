package org.example;

import java.util.HashMap;
import java.util.Map;

import static org.example.Main.sizeToFreq;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            myRunnable mr = new myRunnable();
            new Thread(mr).start();
        }

        Map.Entry<Integer, Integer> maxEntry = sizeToFreq
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        sizeToFreq.remove(maxEntry.getKey());
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxEntry.getKey(), maxEntry.getValue());
        System.out.println("другие размеры:");
        sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .forEach(s -> System.out.printf("- %d (%d раз)\n", s.getKey(), s.getValue()));
    }
}

class myRunnable implements Runnable {

    @Override
    public void run() {
        String route = Route.generateRoute("RLRFR", 100);
        Integer turnRight = Math.toIntExact(route.chars()
                .filter(c -> c == 'R')
                .count());
        synchronized (sizeToFreq) {
            sizeToFreq.merge(turnRight, 1, Integer::sum);
        }
    }
}