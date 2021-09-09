package com.jasonlee.practice.multhreaded.aqslock;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自己实现一个生产者\消费者阻塞队列
 *
 * @author : jason Lee
 * @version :
 * @createAt : 9/9/21 8:04 PM
 */
@Slf4j
public class ProviderAndConsumer<T> {

    private int length;
    private Queue<T> queue;
    private ReentrantLock lock = new ReentrantLock();
    private Condition providerCon = lock.newCondition();
    private Condition consumerCon = lock.newCondition();

    public ProviderAndConsumer(int length) {
        this.length = length;
        queue = new LinkedList<>();
    }


    public void provider(T object) {
        lock.lock();
        try {
            while (queue.size() > length) {
                log.info("队列中已有足够多的元素,provider线开始暂停~");
                providerCon.await();
            }
            log.info("正在生产元素: {}", object.toString());
            queue.add(object);

            consumerCon.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public T consumer() {
        T result = null;
        lock.lock();

        try {
            while (queue.isEmpty()) {
                log.info("队列没有任何元素,customer线开始暂停~");
                consumerCon.await();
            }

            result = queue.remove();
            log.info("正在消费元素: {}", result.toString());
            providerCon.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * 基于ReentrantLock 和condition 实现生产者,消费者阻塞队列
     *
     * @param args
     */
    public static void main(String[] args) {

        //moreProvider();

        moreConsumer();

    }

    /**
     * 更多消费者的模式下
     */
    private static void moreConsumer() {
        ExecutorService service = Executors.newCachedThreadPool();
        ProviderAndConsumer<String> queue = new ProviderAndConsumer<>(10);

        /**
         * 2 生产者,3 消费者
         */
        service.submit(() -> {
            for (; ; ) {
                queue.provider(UUID.randomUUID().toString());
                Thread.sleep(2_500);
            }
        });
        service.submit(() -> {
            for (; ; ) {
                queue.provider(UUID.randomUUID().toString());
                Thread.sleep(1_500);
            }
        });

        service.submit(() -> {
            for (; ; ) {
                queue.consumer();
                Thread.sleep(1_000);
            }
        });
        service.submit(() -> {
            for (; ; ) {
                queue.consumer();
                Thread.sleep(1_500);
            }
        });
        service.submit(() -> {
            for (; ; ) {
                queue.consumer();
                Thread.sleep(3_000);
            }
        });
    }


    /**
     * 更多生产者的模式下
     */
    private static void moreProvider() {
        ExecutorService service = Executors.newCachedThreadPool();
        ProviderAndConsumer<String> queue = new ProviderAndConsumer<>(10);

        /**
         * 3 生产者,2 消费者
         */
        service.submit(() -> {
            for (; ; ) {
                queue.provider(UUID.randomUUID().toString());
                Thread.sleep(1_000);
            }
        });
        service.submit(() -> {
            for (; ; ) {
                queue.provider(UUID.randomUUID().toString());
                Thread.sleep(2_000);
            }
        });
        service.submit(() -> {
            for (; ; ) {
                queue.provider(UUID.randomUUID().toString());
                Thread.sleep(1_500);
            }
        });

        service.submit(() -> {
            for (; ; ) {
                queue.consumer();
                Thread.sleep(1_000);
            }
        });
        service.submit(() -> {
            for (; ; ) {
                queue.consumer();
                Thread.sleep(3_000);
            }
        });
    }

}
