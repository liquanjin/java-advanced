package com.jasonlee.practice.multhreaded.aba;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * ABA的问题如何解决?
 *
 * @author : liquanjin
 */
@Slf4j
public class ABAQuestion_001 {
    public static void main(String[] args) throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(100);
        AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 0);


        Thread t1 = new Thread(() -> {
            atomicInteger.compareAndSet(100, 101);
            atomicInteger.compareAndSet(101, 100);
            log.info("t1 CAS finish~");
        });

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean tag = atomicInteger.compareAndSet(100, 101);
            log.info("t2 CAS result: " + tag);
            log.info("");
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        Thread t3 = new Thread(() -> {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            stampedReference.compareAndSet(100, 101,
                    stampedReference.getStamp(), stampedReference.getStamp() + 1);
            stampedReference.compareAndSet(101, 100,
                    stampedReference.getStamp(), stampedReference.getStamp() + 1);
            log.info("t3 CAS finish~");
        });

        Thread t4 = new Thread(() -> {
            int beforeStamp = stampedReference.getStamp();
            log.info("t4 CAS before stamp: " + beforeStamp);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.info("t4 CAS sleep after stamp: " + beforeStamp);
            boolean tag = stampedReference.compareAndSet(100, 101,
                    beforeStamp, beforeStamp + 1);
            log.info("t4 CAS result: " + tag);
        });

        t3.start();
        t4.start();

    }
}
