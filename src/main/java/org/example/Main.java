package org.example;

import java.util.HashMap;
import java.util.Map;

import static org.example.Main.sizeToFreq;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        maxEntryRunnable maxEntryRunnable = new maxEntryRunnable();
        Thread thread = new Thread(maxEntryRunnable);
        thread.start();

        for (int i = 0; i < 50; i++) {
            numberOfTurnsToTheRightRunnable number = new numberOfTurnsToTheRightRunnable();
            Thread thread1 = new Thread(number);
            thread1.start();
            thread1.join();
        }
        thread.interrupt();
    }
}

class numberOfTurnsToTheRightRunnable implements Runnable {

    @Override
    public void run() {
        String route = Route.generateRoute("RLRFR", 100);
        Integer turnRight = Math.toIntExact(route.chars()
                .filter(c -> c == 'R')
                .count());
        synchronized (sizeToFreq) {
            sizeToFreq.merge(turnRight, 1, Integer::sum);
            sizeToFreq.notify();
        }
    }
}

class maxEntryRunnable implements Runnable {
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (sizeToFreq) {
                try {
                    sizeToFreq.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Map.Entry<Integer, Integer> maxEntry = sizeToFreq.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .orElse(null);
                System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", maxEntry.getKey(), maxEntry.getValue());
            }
        }
    }
}
