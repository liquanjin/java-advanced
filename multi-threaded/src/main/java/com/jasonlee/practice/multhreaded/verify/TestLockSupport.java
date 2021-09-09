package com.jasonlee.practice.multhreaded.verify;

import com.jasonlee.practice.multhreaded.bo.Address;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/7 5:56 下午
 */
public class TestLockSupport {

    static class ThreadA extends Thread {
        public ThreadA(String name) {
            super(name);
        }

        @SneakyThrows
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            String prefix = "ThreadA " + name + " ";
            System.out.println(prefix + "running~");

            LockSupport.park();

            System.out.println(prefix + "unpark after~");
        }
    }

    static class ThreadB extends Thread {
        private ThreadA threadA;

        public ThreadB(String name, ThreadA threadA) {
            this.setName(name);
            this.threadA = threadA;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            String prefix = "ThreadB " + name + " ";
            System.out.println(prefix + "running~");
            LockSupport.unpark(threadA);

            System.out.println(prefix + ". threadA unpark succ");
        }
    }

    static class TaskA implements Runnable {
        Address address;

        public TaskA(Address address) {
            this.address = address;
        }

        @SneakyThrows
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            String prefix = name + " TaskA ";
            System.out.println(prefix + "start~");

            synchronized (address) {
                System.out.println(prefix + "sleeping~");
                Thread.sleep(10000);
                System.out.println(prefix + "sleeping after~");
            }

            System.out.println(prefix + "finish~");
        }
    }

    static class WaitNotifyExample {
        public void before() {
            System.out.println("before");
            notifyAll();
        }

        public void after() {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("after");
        }
    }

    static class AwaitSignalExample {
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        public void before() {
            lock.lock();
            try {
                System.out.println("before");
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void after() {
            lock.lock();
            try {
                condition.await();
                System.out.println("after");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        //parkLock();

        //testSleep();

        //testWait();

        testSignal();
    }

    /**
     * 必须在 lock 方法包裹下才能使用 condition.await()
     */
    private static void testSignal() {
        ExecutorService service = Executors.newCachedThreadPool();
        AwaitSignalExample signalExample = new AwaitSignalExample();
        service.submit(() -> {
            try {
                signalExample.after();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        service.submit(() -> {
            try {
                signalExample.before();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
    }

    /***
     * wait、notify、notifyAll 必须由 synchronized 修饰
     */
    private static void testWait() {
        ExecutorService service = Executors.newCachedThreadPool();
        WaitNotifyExample waitNotifyExample = new WaitNotifyExample();
        service.submit(() -> {
            try {
                waitNotifyExample.after();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        service.submit(() -> {
            try {
                waitNotifyExample.before();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
    }

    private static void testSleep() {
        Address address = new Address("Hunan", "Changsha", "Yuelu", "Dujuan");

        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(new TaskA(address));
        service.submit(new TaskA(address));

        service.shutdown();
    }


    /**
     * test park & unpark .
     */
    private static void parkLock() {
        ThreadA threadA = new ThreadA("0001");
        ThreadB threadB = new ThreadB("0002", threadA);
        threadA.start();
        threadB.start();
    }

}
