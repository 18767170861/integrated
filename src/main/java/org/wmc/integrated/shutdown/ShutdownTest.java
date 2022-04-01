package org.wmc.integrated.shutdown;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ShutdownTest {

    static void shutdownTest1() {
        int count = 10;
        ThreadPoolExecutor tp = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(count));
        for (int i = 0; i < count; i++) {
            //try {
            tp.execute(() -> {
                for (;;){
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("111111111111111111111111");
                }
            });
        }

        try {
            boolean b = tp.awaitTermination(4, TimeUnit.SECONDS);
            System.out.println("b:" + b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------");
        System.out.println("all tests finished");
    }

    static void shutdownTest() {
        int count = 10;
        ThreadPoolExecutor tp = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(count));
        for (int i = 0; i < count; i++) {
            tp.execute(new Task(i));
        }
        try {
            tp.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------");
        System.out.println("all tests finished");
    }

    static class Task implements Runnable {
        String name = "";

        public Task(int i) {
            name = "task-" + i;
        }

        public String getName() {
            return name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("sleep completed, " + getName());
            } catch (InterruptedException e) {
                System.out.println("interrupted, " + getName());
            }
            System.out.println(getName() + " finished");
        }
    }

    public static void main(String[] args) {
        shutdownTest1();
    }
}
