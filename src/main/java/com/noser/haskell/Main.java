package com.noser.haskell;

import kotlin.Unit;

class Main {

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler("important-stuff");

        scheduler.addTask("task1", 3L, () -> {
            System.out.print("dududu...");
            return Unit.INSTANCE;
        });
        scheduler.addTask("task2", 3L, () -> {
            System.out.print("dadada...");
            return Unit.INSTANCE;
        });

        try {
            Thread.sleep(16_000);
        } catch (InterruptedException e) {
            // bad
        }
    }
}