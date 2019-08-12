package com.noser.haskell;

import kotlin.Unit;

class Main {

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler("important-stuff");

        scheduler.addTask("task1", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task1: de do do do...");
            return Unit.INSTANCE;
        });
        scheduler.addTask("task2", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task2: de da da da...");
            return Unit.INSTANCE;
        });
        scheduler.addTask("task3", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task3: is all I want...");
            return Unit.INSTANCE;
        });
        scheduler.addTask("task4", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task4: to say to you...");
            return Unit.INSTANCE;
        });

        try {
            Thread.sleep(16_000);
        } catch (InterruptedException e) {
            // bad
        }
    }
}