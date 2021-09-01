package com.jasonlee.pratice.multhreaded.thread;

import com.jasonlee.pratice.multhreaded.threadpool.MyThreadMonitor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/13 7:52 下午
 */
@Slf4j
public class TestThreadPool {

    public static void main(String[] args) throws Exception {
        /***
         * TPE:ThreadPoolExecutor
         * 有界:
         * ArrayBlockingQueue \ LinkedBlockingQueue \ LinkedBlockingDeque
         *
         * 无界:
         * PriorityBlockingQueue \ DelayQueue \ LinkedTransferQueue
         *
         * 不存储:
         * SynchronousQueue
         *
         */
        int capacityOfQueue = 2;
        // 总线程数,而不是补充线程数.
        int maximumPoolSize = 2;
        int corePoolSize = 2;
        int taskCount = 1;
        boolean fair = true;

        // demo1. 有界.
        //corePoolSize = 2;
        //maximumPoolSize = 8;
        //capacityOfQueue = 20;
        //taskCount = 28;
        //fair = true;
        //testTPEArray(corePoolSize, maximumPoolSize, capacityOfQueue, taskCount, fair);

        //demo2
        //corePoolSize = 1;
        //maximumPoolSize = 1;
        //capacityOfQueue = 4;
        //taskCount = 10;
        //testTPELinkedBlockingQueue(corePoolSize, maximumPoolSize, capacityOfQueue, taskCount);

        //demo3
        //corePoolSize = 1;
        //maximumPoolSize = 1;
        //capacityOfQueue = 4;
        //taskCount = 3;
        //testTPELinkedBlockingDeque(corePoolSize, maximumPoolSize, capacityOfQueue, taskCount);

        /**
         *  无界 队列. 较少使用
         *  1. 优先往队列中放,所以最大线程基本上无效.
         *  2.
         */

        //demo 4 .需要实现 Comparable 比较接口
        //corePoolSize = 1;
        //maximumPoolSize = 5;
        //taskCount = 300;
        //testTPEPriority(corePoolSize, maximumPoolSize, taskCount);

        //demo 5. 延时实现
        //corePoolSize = 1;
        //maximumPoolSize = 5;
        //taskCount = 30;
        //testTPEDelay(corePoolSize, maximumPoolSize, taskCount);


        //demo6
        corePoolSize = 1;
        maximumPoolSize = 5;
        taskCount = 30;
        testTPELinkedTransfer(corePoolSize, maximumPoolSize, taskCount);

        Executors.newCachedThreadPool();

        //ThreadPoolExecutor.AbortPolicy;

        // demo7. 无法存储队列. 特殊.
        maximumPoolSize = 4;
        corePoolSize = 1;
        testTPSSynchronousQueue(corePoolSize, maximumPoolSize);


        //1. 固定大小
        //executorsFixed1();

        //2. 延期执行的线程池
        //executorsPoolDelay();
    }

    private static void testTPELinkedTransfer(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize
                , 60, TimeUnit.SECONDS, new LinkedTransferQueue<>());
        testTPEBase(pool, taskCount);
    }

    private static void testTPEDelay(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize
                , 60, TimeUnit.SECONDS,
                new DelayQueue());
        testTPEBase(pool, taskCount);
    }

    private static void testTPEPriority(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize
                , 60, TimeUnit.SECONDS,
                new PriorityBlockingQueue<>());
        testTPEBase(pool, taskCount);
    }

    private static void testTPEArray(int corePoolSize, int maximumPoolSize,
                                     int capacityOfQueue, int taskCount, boolean fair) {
        ThreadPoolExecutor pool =
                new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                        60, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(capacityOfQueue, fair));

        testTPEBase(pool, taskCount);
    }

    private static void testTPELinkedBlockingQueue(int corePoolSize, int maximumPoolSize,
                                                   int capacityOfQueue, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(capacityOfQueue));

        testTPEBase(pool, taskCount);
    }

    private static void testTPELinkedBlockingDeque(int corePoolSize, int maximumPoolSize,
                                                   int capacityOfQueue, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60, TimeUnit.SECONDS, new LinkedBlockingDeque<>(capacityOfQueue));

        testTPEBase(pool, taskCount);
    }

    private static void testTPEBase(ThreadPoolExecutor pool, int taskCount) {

        Thread monitorThread = new Thread(new MyThreadMonitor(1000, pool));
        monitorThread.start();
        for (int i = 1; i <= taskCount; i++) {
            pool.execute(new SimpleRunnable(i));
        }

        pool.shutdown();

        log.info("main thread content");
    }


    /**
     * SynchronousQueue 没有存储队列! 有工作线程就会消费,如果没有就不能继续添加.
     *
     * @param corePoolSize
     * @param maximumPoolSize
     */
    private static void testTPSSynchronousQueue(int corePoolSize, int maximumPoolSize) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60, TimeUnit.SECONDS
                , new SynchronousQueue<>());

        testTPEBase(pool, 4);
    }


    @Slf4j
    static class SimpleRunnable implements Runnable, Comparable {
        protected Integer no;

        public SimpleRunnable() {
        }

        public SimpleRunnable(int no) {
            this.no = no;
        }

        @Override
        public void run() {
            log.info("no:{} SimpleRunnable running start~", no);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //log.info("no:{} SimpleRunnable running finish~", no);
        }

        /**
         *
         */
        @Override
        public int compareTo(Object o) {
            return this.no.compareTo(((SimpleRunnable) o).no);
        }
    }

    private static void executorsPoolDelay() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        service.schedule(() -> log.info("{} 正在延期执行job.", getLogPrefix()), 10, TimeUnit.SECONDS);
        service.shutdown();
        log.info("{} 主线程内容", getLogPrefix());
    }

    private static void executorsFixed1()
            throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(() -> {
            String prefix = getLogPrefix();
            log.info("{} : 正在执行.预计耗时3秒", prefix);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //无法并行
        executorService.submit(() -> {
            String prefix = getLogPrefix();
            log.info("{} : 无法并行.", prefix);
        });

        executorService.shutdown();
        log.info(" {} 主线程内容", getLogPrefix());
    }

    private static String getLogPrefix() {
        return " ";
    }

}
