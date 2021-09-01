package com.jasonlee.pratice.multhreaded.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/9 9:18 下午
 */
@Slf4j
public class TestThread {

    public static void main(String[] args) throws Exception {
        //testRun();
        //testJoin();

        //testJava8AsyncDemo1();
        //testJava8AsyncDemo2();

        //threadPoolFixed1();

    }

    private static void testJava8AsyncDemo2() throws JsonProcessingException {
        Long startTime = System.currentTimeMillis();

        List<CompletableFuture<String>> completableFutures =
                IntStream.range(1, 12)
                        .boxed()
                        .map(id -> CompletableFuture.supplyAsync(() -> queryUser(id)))
                        .collect(Collectors.toList());

        List<String> result = completableFutures.stream()
                .map(future -> future.join())
                .collect(Collectors.toList());

        Long endTime = System.currentTimeMillis();
        double times = 1.0 * (endTime - startTime) / 1_000;

        System.out.println("执行耗时：" + times);
        System.out.println("执行结果：" + new ObjectMapper().writeValueAsString(result));
    }

    private static void testJava8AsyncDemo1() throws JsonProcessingException {
        Long start = System.currentTimeMillis();

        List<String> userNames = IntStream.range(1, 12).boxed()
                .collect(Collectors.toList())
                // 明显感受到 parallel 执行的效率
                .parallelStream()
                .map(TestThread::queryUser)
                .collect(Collectors.toList());

        Long end = System.currentTimeMillis();

        double result = (end - start) * 1.0 / 1_000;
        System.out.println("执行耗时：" + result);
        System.out.println("执行结果：" + new ObjectMapper().writeValueAsString(userNames));

    }

    private static String queryUser(Integer id) {
        System.out.println(getLogPrefix() + "queryUser " + id + " start~");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(getLogPrefix() + "queryUser " + id + " finish~");
        return "name-" + id;
    }

    private static String getLogPrefix() {
        String name = Thread.currentThread().getName();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        return name + " - " + time + " ";
    }


    private static void testJoin() throws InterruptedException {
        Thread threadA = new Thread(
                () -> {
                    String prefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS").format(new Date());
                    System.out.println(prefix + " threadA running~");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    prefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS").format(new Date());
                    System.out.println(prefix + " threadA finish~");
                }, "threadA");

        Thread threadB = new Thread(() -> {
            String prefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS").format(new Date());
            System.out.println(prefix + " threadB running~");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            prefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS").format(new Date());
            System.out.println(prefix + " threadB finish~");
        }, "threadB");

        threadA.start();
        threadB.start();
        threadA.join();
        threadB.join();

        String prefix = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSS").format(new Date());
        System.out.println(prefix + " 主线程内容～");

    }


    private static void testRun() {

        System.out.println("当前线程:" + Thread.currentThread().getName());
        new Thread(() -> {
            System.out.println("run 子线程信息:" + Thread.currentThread().getName());
        }).run();

        new Thread(() -> {
            System.out.println("start 子线程信息:" + Thread.currentThread().getName());
        }).start();

    }
}
