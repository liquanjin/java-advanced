package com.jasonlee.practice.multhreaded.aqslock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/7/21 4:45 PM
 */
@Slf4j
public class ReentrantLockDebug_001 {

    /**
     * 测试基于AQS的ReentrantLock使用效果.
     * 1.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //lockByOrder();

        lockByNonfair();
    }

    /**
     * 测试 nonfair 模式, 有线程直接争抢
     */
    @SneakyThrows
    private static void lockByNonfair() {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService service = Executors.newCachedThreadPool();
        CyclicBarrier barrier = new CyclicBarrier(2);


        // 01. 直接上锁
        service.submit(() -> {
            lock.lock();
            log.info("do business finish~");

            lock.unlock();

            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 排队.
        service.submit(() -> {
            lock.lock();
            log.info("do business 2 finish~");
            lock.unlock();
        });


        barrier.await();
        // 准备争抢线程
        service.submit(() -> {
            lock.lock();
            log.info("do business 3 finish~");
            lock.unlock();
        });

        while (true) {
            int i = 1;
        }
    }

    /**
     * nonfair 模式下,多线程依次争抢资源
     * @throws Exception
     */
    private static void lockByOrder() throws Exception {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService service = Executors.newCachedThreadPool();


        /**
         * 1. 只关注unlock 方法
         */
        service.submit(() -> {
            lock.lock();
            try {
                //barrier1.await();
                log.info("business process finish ~~");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                log.info("unlock success~");
            }

        });


        /**
         * 1. debug: 第二线程的 lock 方法. 快调试完了,触发3线程
         * 2.
         */
        service.submit(() -> {
            lock.lock();
            try {
                log.info("business process finish ~~");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                log.info("unlock success~");
            }

        });

        service.submit(() -> {
            lock.lock();

            try {
                log.info("business process finish ~~");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                log.info("unlock success~");
            }

        });


        service.shutdown();
        while (true) {
            int i = 1;
        }
    }
}
