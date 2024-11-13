package com.spring.app.urlshorter.unit;

import net.minidev.json.JSONUtil;
import org.apache.groovy.util.ObjectHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

@ExtendWith(MockitoExtension.class)
public class ThreadTests {
    private final Object obj = new Object();
    private final Object obj2 = new Object();

    @Test
    @DisplayName("thread test")
    public void testSyncronized() {
        var threads = IntStream.rangeClosed(1, 100).mapToObj((i) -> {
            return new Thread(() -> {
//                printI(i);
//                printWithLock(i);
                printWithLock2(i);
            });
        }).map((thread) -> {
            thread.start();
            return thread;
        }).toList();

        threads.forEach((thread) -> {
            try {
                thread.join();
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }

    @Test
    @DisplayName("thread test2")
    public void testSyncronized2() {
        var futures = IntStream.rangeClosed(1, 100).mapToObj((i) -> {
            return CompletableFuture.runAsync(() -> {
                printI(i);
            });
        }).toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
    }

    @Test
    @DisplayName("thread test3")
    public void testSyncronized3() {
        var futures = IntStream.rangeClosed(1, 100).mapToObj((i) -> {
            return CompletableFuture.supplyAsync(() -> {
                return printI2(i);
            });
        }).toList();

        futures.forEach(integerCompletableFuture -> {
            try {
                System.out.println(integerCompletableFuture.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    public synchronized void printI(int i) {
        try {
            System.out.println(i);
            Thread.sleep((long) (Math.random() * 100));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public synchronized int printI2(int i) {
        try {
            Thread.sleep((long) (Math.random() * 100));
            return i;
        } catch (Exception e) {
            System.out.println(e);
        }
        return i;
    }

    public void printWithLock(int i) {
        synchronized (obj) {
            try {
                System.out.println("lock 1 -> " + i);
                Thread.sleep((long) (Math.random() * 500));
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        synchronized (obj2) {
            try {
                System.out.println("lock 2 -> " + i);
                Thread.sleep((long) (Math.random() * 500));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    ReentrantLock lock = new ReentrantLock();

    public void printWithLock2(int i) {
        lock.lock();
        try {
            System.out.println("lock 2 -> " + i);
//            Thread.sleep((long) (Math.random() * 10));
        } catch (Exception e) {
            System.out.println(e);
        }

        lock.unlock();
    }
}
