package com.jasonlee.practice.multhreaded.aqslock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/9/21 11:24 AM
 */
@Slf4j
public class ReentrantLockBase_001 {
    public static void main(String[] args) {

        //base_001();

        base_002();

    }


    /**
     *
     */
    private static void base_002() {
        ExecutorService service = Executors.newCachedThreadPool();
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Condition condition2 = lock.newCondition();
        log.info("condition == condition2 ? {}", (condition == condition2));

        service.submit(() -> {
            lock.lock();
            try {
                log.info("task01 start~");
                Thread.sleep(1_000);
                log.info("task01 进入等待状态");

                // 类似 synchronized 加锁后, Object的 wait 方法();
                condition.await();
                Thread.sleep(2_000);
                log.info("task01 finish~");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });


        service.submit(() -> {
            lock.lock();
            try {
                log.info("task02 start~");
                Thread.sleep(3_000);
                log.info("task02 finish~");

                // 类似 synchronized 加锁后, Object的 notify 方法();
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });


        service.shutdown();
    }

    /**
     * lock()
     * unlock()
     * tryLock()
     * tryLock(1000,TimeUnit)
     */
    private static void base_001() {
        ReentrantLock lock = new ReentrantLock();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            lock.lock();

            // 必须要手动 finally unlock
            try {
                log.info("task 01 process business~");
                Thread.sleep(10_000);
                log.info("task 01 finish~");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        });

        service.submit(() -> {
            log.info("当前lock状态: {}", lock.isLocked());
            log.info("当前是否可以抢占成功: {}", lock.tryLock());

            try {
                Boolean result = lock.tryLock(11, TimeUnit.SECONDS);
                if (result) {
                    log.info("tryLock success~");
                } else {
                    log.info("tryLock fail~");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();

    }
}
