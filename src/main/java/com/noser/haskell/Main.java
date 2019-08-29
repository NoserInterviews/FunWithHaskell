package com.noser.haskell;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

class Main {

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler("important-stuff");

        Function1<Task,Unit> printer = (task) -> {
            System.out.println(task + " added");
            return Unit.INSTANCE;
        };

        scheduler.addTask("task1", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task1: de do do do...");
            return Unit.INSTANCE;
        }).forEach(printer);
        scheduler.addTask("task2", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task2: de da da da...");
            return Unit.INSTANCE;
        }).forEach(printer);
        scheduler.addTask("task3", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task3: is all I want...");
            return Unit.INSTANCE;
        }).forEach(printer);
        scheduler.addTask("task4", 3L, () -> {
            System.out.print(System.currentTimeMillis() + "-task4: to say to you...");
            return Unit.INSTANCE;
        }).forEach(printer);

        try {
            Thread.sleep(16_000);
        } catch (InterruptedException e) {
            // bad
        }
    }
}