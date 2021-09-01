package com.jasonlee.pratice.multhreaded.aqslock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/14 5:56 下午
 */
@Slf4j
public class AQSDeepTest {

    static class TestLockDemo implements Runnable {
        private ReentrantLock lock;

        public TestLockDemo(ReentrantLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            log.info("lock success");
            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Test
    public void testLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService service = Executors.newFixedThreadPool(5);
        service.submit(new TestLockDemo(lock));
        service.shutdown();

        Thread.sleep(1000);
        lock.lock();
    }
}
