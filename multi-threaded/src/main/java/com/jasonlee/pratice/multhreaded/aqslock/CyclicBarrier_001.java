package com.jasonlee.pratice.multhreaded.aqslock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 回旋栅栏. async
 *
 * @author : jason Lee
 * @version :
 * @createAt : 9/1/21 3:36 PM
 */
@Slf4j
public class CyclicBarrier_001 {

    /**
     * 回旋栅栏
     * > 基于AQS实现的 栅栏锁,当所有线程达到指定的计数次数后,所有线程阻塞接触,继续运行.
     * <p>
     * feature:
     * 1. 加法
     * 2. await 会阻塞
     * 3. 可重复使用
     * 4. 线程间可多次使用的计数栅栏.
     *
     * @param args
     */
    public static void main(String[] args) {
        base();
    }

    private static void base() {
        int tasks = 3;
        CyclicBarrier barrier = new CyclicBarrier(tasks);
        ExecutorService service = Executors.newCachedThreadPool();
        // 是否循环使用. 1 不循环
        int cycCount = 2;
        for (int i = 0; i < tasks * cycCount; i++) {
            service.submit(new CyclicRunnable(barrier));
        }
        service.shutdown();
    }

    static class CyclicRunnable implements Runnable {
        private CyclicBarrier barrier;
        Random random = new Random();

        public CyclicRunnable(CyclicBarrier barrier) {
            this.barrier = barrier;
        }


        @SneakyThrows
        @Override
        public void run() {
            // do business
            int processTimes = random.nextInt(10);
            log.info("准备工作需要花 {} 秒", processTimes);
            Thread.sleep(processTimes * 1000);
            log.info("准备工作做完,等待其他线程");

            // wait another thread.
            // 线程进入等待状态.
            barrier.await();
            log.info("一批Thread就绪,统一处理~");

        }
    }


}
