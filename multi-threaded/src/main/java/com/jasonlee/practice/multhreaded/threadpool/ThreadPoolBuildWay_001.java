package com.jasonlee.practice.multhreaded.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/6/21 11:28 AM
 */
@Slf4j
public class ThreadPoolBuildWay_001 {
    public static void main(String[] args) {

        //buildByExecutors();

        buildByThreadPoolExecutors();
    }

    /**
     * recommend usage for ThreadPoolExecutor
     */
    private static void buildByThreadPoolExecutors() {
        ExecutorService service = new ThreadPoolExecutor(4, 6, 5,
                TimeUnit.MINUTES, new LinkedBlockingDeque<>());
        service.submit(() -> log.info("synchronized method run~~"));
        service.submit(() -> log.info("synchronized method run~~"));
        service.submit(() -> log.info("synchronized method run~~"));
        service.submit(() -> log.info("synchronized method run~~"));
        service.submit(() -> log.info("synchronized method run~~"));
        service.submit(() -> log.info("synchronized method run~~"));
        service.shutdown();

    }

    /**
     * 不推荐的方式: 生产禁止使用
     * 原因:
     * 1. 屏蔽了线程构建的核心参数,使用时可能有偏差
     * 2. 构建方式不灵活,可选性较少.
     * <p>
     * 提供线程池方法弊端
     * 1) FixedThreadPool 和 SingleThreadPool:
     * 允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
     * 2) CachedThreadPool:
     * 允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。
     */
    private static void buildByExecutors() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        //ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(() -> {
            try {
                Thread.sleep(5_00_00_00_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            service.submit(() -> {
                log.info("task {}", System.currentTimeMillis());
            });
        }
        log.info("task add finish~");
        service.shutdown();
    }
}
