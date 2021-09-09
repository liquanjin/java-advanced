package com.jasonlee.practice.multhreaded.threadpool;

import com.jasonlee.practice.multhreaded.threadpool.monitor.ThreadPoolMonitor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/13 7:52 下午
 */
@Slf4j
public class ThreadPoolManualBuildWay_002 {

    /**
     * construction method
     * <p>
     * int corePoolSize, 默认工作的线程数.除非设置允许核心线程超时,否则这是最小的线程数.
     * int maximumPoolSize, 工作线程数最大值. 当核心线程不够时不会立刻构建新的线程 ,
     * 而是先往Queue 中存放,直到Queue 也无法存放后才逐步使用maximumPoolSize的名额继续创建工作线程
     * long keepAliveTime, 线程闲置后收回时间.
     * TimeUnit unit, 闲置收回时间的单位
     * BlockingQueue<Runnable> workQueue 当core线程不够用之后,剩下的task 都往 queue 里面保存
     * ThreadFactory threadFactory, 线程构建工厂对象
     * RejectedExecutionHandler handler 拒绝策略
     *
     * @param args args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        /**
         * ArrayBlockingQueue feature:
         * 1. 有序队列
         * 2. 有限队列
         * 3. 可定义是否公平
         * 4. task 数量超过 maximum + capacity 就会报错
         */
        //int corePoolSize = 2, maximumPoolSize = 8, capacityOfQueue = 20, taskCount = 22;
        //limitedArrayQueue(corePoolSize, maximumPoolSize, capacityOfQueue, taskCount, false);

        /**
         * LinkedBlockingQueue feature:
         * 1. 有界队列 (超过就会报错)
         * 2. 有序
         */
        //int corePoolSize = 2, maximumPoolSize = 10, capacityOfQueue = 20, taskCount = 15;
        //testTPELinkedBlockingQueue(corePoolSize, maximumPoolSize, capacityOfQueue, taskCount);

        /**
         * LinkedBlockingDeque
         * 1. 有界
         * 2. 双向
         * 3.
         *
         * ano:
         * 1. 和 LinkedBlockingQueue 的区别
         * 2.
         */
        //int corePoolSize = 2, maximumPoolSize = 8, capacityOfQueue = 15, taskCount = 11;
        //testTPELinkedBlockingDeque(corePoolSize, maximumPoolSize, capacityOfQueue, taskCount);

        /**
         * 可实现自定义规则的线程池. 严格按照顺序执行
         * 1. 无界限.
         * 2. 要自定义优先级.
         *
         **/
        //int corePoolSize = 1, maximumPoolSize = 5, taskCount = 30;
        //testTPEPriority(corePoolSize, maximumPoolSize, taskCount);

        /**
         * delayQueue : 延时队列
         * 1. 需要实现 Delayed 接口,内部既要排序又要实现延时时间的规则
         * 2. 无界限
         * 3. 自定义排序,自定义延时规则
         *
         */
        //int corePoolSize = 1, maximumPoolSize = 5, taskCount = 20;
        //testTPEDelay(corePoolSize, maximumPoolSize, taskCount);


        /**
         * LinkedTransferQueue
         * 1. 有序.
         * 2. 无容量上限
         *
         */
        //int corePoolSize = 3, maximumPoolSize = 5, taskCount = 20;
        //testTPELinkedTransfer(corePoolSize, maximumPoolSize, taskCount);


        /**
         * SynchronousQueue
         * 1. 有上限, poolSize 就是上限
         * 2. 没有cache ,直接转交给 thread
         *
         */
        //int corePoolSize = 1, maximumPoolSize = 4, taskCount = 10;
        //testTPSSynchronousQueue(corePoolSize, maximumPoolSize, taskCount);

    }

    private static void testTPELinkedTransfer(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize
                , 60, TimeUnit.SECONDS, new LinkedTransferQueue<>());
        testTPEBase(pool, taskCount);
    }

    private static void testTPEDelay(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize
                , 60, TimeUnit.SECONDS, new DelayQueue());
        testTP4Delayed(pool, taskCount);
    }

    private static void testTPEPriority(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize
                , 60, TimeUnit.SECONDS,
                new PriorityBlockingQueue<>());
        testTPPriority(pool, taskCount);
    }

    private static void limitedArrayQueue(int corePoolSize, int maximumPoolSize,
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
        Thread monitorThread = new Thread(new ThreadPoolMonitor(500, pool));
        monitorThread.start();
        for (int i = 1; i <= taskCount; i++) {
            pool.submit(new SimpleRunnable(i));
        }
        pool.shutdown();

        log.info("main thread content");
    }

    private static void testTPPriority(ThreadPoolExecutor pool, int taskCount) {
        Thread monitorThread = new Thread(new ThreadPoolMonitor(500, pool));
        monitorThread.start();
        for (int i = 1; i <= taskCount; i++) {
            pool.execute(new MyFutureTask(new SimpleRunnable(i), new Object(), i));
        }
        pool.shutdown();

        log.info("main thread content");
    }

    private static void testTP4Delayed(ThreadPoolExecutor pool, int taskCount) {
        Thread monitorThread = new Thread(new ThreadPoolMonitor(500, pool));
        monitorThread.start();
        for (int i = 1; i <= taskCount; i++) {
            pool.execute(new MyDelayed(new SimpleRunnable(i), new Object(), i, 6_000));
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
    private static void testTPSSynchronousQueue(int corePoolSize, int maximumPoolSize, int taskCount) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                60, TimeUnit.SECONDS, new SynchronousQueue<>());
        testTPEBase(pool, taskCount);
    }


    @Slf4j
    static class SimpleRunnable implements Runnable, Comparable<SimpleRunnable> {
        protected Integer no;

        public SimpleRunnable() {
        }

        public SimpleRunnable(int no) {
            this.no = no;
        }

        @Override
        public void run() {
            log.info("no:{} running ", no);
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
        public int compareTo(SimpleRunnable o) {
            return this.no.compareTo(o.no);
        }
    }

    static class MyFutureTask extends FutureTask implements Comparable<MyFutureTask> {
        private Integer no;

        public MyFutureTask(Runnable runnable, Object result, Integer no) {
            super(runnable, result);
            this.no = no;
        }

        @Override
        public int compareTo(MyFutureTask o) {
            return this.no.compareTo(o.no);
        }
    }

    static class MyDelayed extends FutureTask implements Delayed {
        private Integer no;
        private Integer milliSecond;

        public MyDelayed(Runnable runnable, Object result, Integer no, Integer milliSecond) {
            super(runnable, result);
            this.no = no;
            this.milliSecond = milliSecond;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            switch (unit) {
                case MICROSECONDS:
                    return milliSecond;
                case SECONDS:
                    return milliSecond / 1000;
                case MINUTES:
                    return milliSecond / 1000 / 60;
                case HOURS:
                    return milliSecond / 1000 / 60 / 60;
                case DAYS:
                    return milliSecond / 1000 / 60 / 60 / 24;
                default:
                    return 0;
            }
        }

        @Override
        public int compareTo(Delayed o) {
            MyDelayed delayed = (MyDelayed) o;
            if (delayed.no > this.no) {
                return -1;
            } else if (delayed.no.equals(this.no)) {
                return 0;
            }
            return 1;
        }
    }


}
