package com.jasonlee.practice.multhreaded.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/4/21 2:59 PM
 */
@Slf4j
public class ThreadStates_001 implements Runnable {

    /**
     * 分析 Thread.State 各个状态
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        ThreadStates_001 obj = new ThreadStates_001();
        //obj.threadState_001();
        obj.threadState_002();
    }


    private void threadState_001() throws InterruptedException {
        new Thread(() -> {
            synchronized (ThreadStates_001.class) {
                try {
                    Thread.sleep(5_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread thread = new Thread(() -> {
            log.info("thread running ~~~~");

            // 调用 synchronized 方法: 进入BLOCKED
            syncMethod();

            // TIMED_WAITING case
            try {
                Thread.sleep(3_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //WAITING case

            log.info("running finish~~~~");
        });

        //NEW
        monitorForThreadState(thread);

        //RUNNABLE
        thread.start();

        // BLOCKED

        //TIMED_WAITING

        thread.join();
        //terminal
    }

    private void threadState_002() throws InterruptedException {
        ThreadStates_001 runnable = new ThreadStates_001();
        Thread thread = new Thread(runnable);
        monitorForThreadState(thread);
        //NEW
        //RUNNABLE
        thread.start();

        Thread.sleep(2_000);

        //BLOCKED
        synchronized (runnable) {
            runnable.notify();
        }

        // BLOCKED
        thread.join();
    }

    @Override
    public void run() {
        log.info("thread running ~~~~");

        //进入BLOCKED 的第二种方式
        synchronized (this) {
            try {
                this.wait();
                for (int i = 0; i < 5000_0000; i++) {
                    System.out.print("");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("running finish~~~~");
    }

    private static void monitorForThreadState(Thread thread) {
        new Thread(() -> {
            Thread.State oldState = null;
            do {
                Thread.State newState = thread.getState();
                if (!newState.equals(oldState)) {
                    log.info("{}线程状态变化, {} -> {}", thread.getName(),
                            oldState == null ? "null" : oldState.toString(), newState.toString());
                    oldState = newState;
                    if (Thread.State.TERMINATED.equals(newState)) {
                        break;
                    }
                }
            } while (true);
        }).start();
    }

    private static synchronized void syncMethod() {
        log.info("syncMethod running~");
    }

    private synchronized void syncMethod02() throws InterruptedException {
        log.info("syncMethod02 synchronized method running~");
    }

}
