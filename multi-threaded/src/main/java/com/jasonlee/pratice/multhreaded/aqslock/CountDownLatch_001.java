package com.jasonlee.pratice.multhreaded.aqslock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/1/21 4:10 PM
 */
@Slf4j
public class CountDownLatch_001 {

    /**
     * CountDownLatch
     * > 使用减法的
     *
     * <p>
     * use scene :
     * 1. 子线程发送信号后并不阻塞的场景
     * 2. 不能重复使用; 否则就要重新new
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //processEvent();
        cycUsed();
    }

    private static void processEvent() throws InterruptedException {
        int countDownNum = 4;
        CountDownLatch count = new CountDownLatch(countDownNum);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < countDownNum; i++) {
            service.submit(new CountDownRunnable(count));
        }
        service.shutdown();

        log.info("event process thread wait~");
        count.await();
        log.info("all thread event process finish~");
        Thread.sleep(15_000);
    }

    private static void cycUsed() throws InterruptedException {
        int countDownNum = 2;
        CountDownLatch count = new CountDownLatch(countDownNum);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0; i < countDownNum; i++) {
            service.submit(new CountDownRunnable(count));
        }
        service.shutdown();

        log.info("event process thread wait~");
        count.await();
        log.info("all thread event process finish~");
        Thread.sleep(5_000);

        log.info("cyc used~");
        service = Executors.newCachedThreadPool();
        service.submit(new CountDownRunnable(count));
        service.submit(new CountDownRunnable(count));
        service.shutdown();
        // 重复使用不再生效
        count.await();
        log.info("应该不会被拦截");
    }

    static class CountDownRunnable implements Runnable {
        Random random = new Random();
        CountDownLatch count;

        public CountDownRunnable(CountDownLatch count) {
            this.count = count;
        }

        @SneakyThrows
        @Override
        public void run() {
            // do business
            int times = random.nextInt(10);
            log.info("处理业务耗时:{}", times);
            Thread.sleep(times * 1000);

            // 不阻塞
            count.countDown();
            log.info("核心业务处理完成,process another business~");

            int num = (times % 4) + 1;
            for (int i = 0; i < num; i++) {
                log.info("process businessNo{}~~", num);
            }
        }
    }
}
