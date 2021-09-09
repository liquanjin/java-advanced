package com.jasonlee.practice.multhreaded.threadpool.monitor;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/14 10:51 上午
 */
@Slf4j
public class ThreadPoolMonitor implements Runnable {

    private ThreadPoolExecutor pool;
    private int interval;

    public ThreadPoolMonitor(int interval, ThreadPoolExecutor pool) {
        this.pool = pool;
        this.interval = interval;
    }

    @Override
    public void run() {
        do {

            System.out.println(String.format("Thread info. [%d/%d] ActiveCount:%d , " +
                            "Completed:%d ,TaskCount:%d ,ShutDown:%s ,Terminated:%s ",
                    this.pool.getCorePoolSize(),
                    this.pool.getPoolSize(),
                    this.pool.getActiveCount(),
                    pool.getCompletedTaskCount(),
                    pool.getTaskCount(),
                    pool.isShutdown(),
                    pool.isTerminated()
            ));
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
            }
        } while (!pool.isTerminated());

        System.out.println(String.format("Thread info. [%d/%d] ActiveCount:%d , " +
                        "Completed:%d ,TaskCount:%d ,ShutDown:%s ,Terminated:%s ",
                this.pool.getCorePoolSize(),
                this.pool.getPoolSize(),
                this.pool.getActiveCount(),
                pool.getCompletedTaskCount(),
                pool.getTaskCount(),
                pool.isShutdown(),
                pool.isTerminated()
        ));

    }
}
