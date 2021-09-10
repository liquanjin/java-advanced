package com.jasonlee.practice.multhreaded.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonlee.practice.multhreaded.async.event.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : jason Lee
 */
@Slf4j
@ComponentScan(value = "com.jasonlee.practice.multhreaded")
@SpringBootTest(classes = SyncConvertAsync_001.class)
@RunWith(SpringRunner.class)
public class SyncConvertAsync_001 {

    /***
     *  同步转异步的几种方式
     * 1. Runnable
     * 2. Callable
     * 3. parallel Stream
     * 4. CompletableFuture.supplyAsync 方式
     * 5. @Async 关键字
     * 6. 通过event完成
     *
     */
    public static void main(String[] args) throws Exception {

        //asyncRunByRunnable();

        //callableRun();

        //parallelRun();

        //completableFutureRun();
    }

    /**
     * 通过event 调用: springEvent, redis event, MQ等
     */
    @Test
    public void springEvent() throws InterruptedException {
        bookingService.createOrderByEvent("1770211111");
        log.info("主线程调用完成");
        Thread.sleep(5000);
    }

    @Autowired
    private BookingService bookingService;

    /**
     * @Async 关键字开启异步调用
     */
    @Test
    public void asyncSyntax() throws InterruptedException {
        bookingService.createOrderWithAno("1770211111");
        log.info("主线程调用完成");
        Thread.sleep(5000);
    }

    /**
     * CompletableFuture
     */
    private static void completableFutureRun() throws Exception {
        List<CompletableFuture<String>> futures =
                IntStream.range(1, 12)
                        .boxed()
                        .map(id -> CompletableFuture.supplyAsync(() -> queryUser(id)))
                        .collect(Collectors.toList());

        List<String> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        log.info(" testDemo4 finish。" + new ObjectMapper().writeValueAsString(result));
    }

    /**
     * parallelStream 并行
     */
    @Test
    public void parallelRun() throws JsonProcessingException {
        List<String> names = IntStream.range(1, 12)
                .boxed()
                .collect(Collectors.toList())
                .parallelStream()
                .map(SyncConvertAsync_001::queryUser)
                .collect(Collectors.toList());
        log.info("parallelRun end. " + new ObjectMapper().writeValueAsString(names));
    }

    private static String queryUser(Integer id) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("queryUser " + id + " finish~");
        return "name-" + id;
    }


    /**
     * runnable 方式
     */
    private static void asyncRunByRunnable() {
        Thread thread = new Thread(() -> {
            int tag = 1;
            while (tag <= 5) {
                log.info("当前时间");
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tag++;
            }
        });
        thread.start();
        log.info("当前时间");
    }

    /**
     * Callable
     * 1. 有返回值.
     * 2.
     */
    private static void callableRun() throws Exception {
        ExecutorService service = Executors.newCachedThreadPool();
        Future<Integer> future = service.submit(() -> {
            int tag = 1;
            while (tag <= 5) {
                log.info("当前时间");
                try {
                    Thread.sleep(2_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tag++;
            }
            return tag;
        });

        log.info("主线程开始获取结果~");

        // get 方法会阻塞
        // 同时这也是 异步转同步的方式
        log.info("执行结果: {}", future.get());
    }


}
