package com.jasonlee.practice.multhreaded.async;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 异步转同步
 *
 * ref: 印象笔记. Thread|进阶|2021-04-13
 *
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/8 6:22 下午
 */
public class TestThreadAsyncConvertSync {

    public static void main(String[] args) {

        /**
         * 异步转同步的五种方式：
         *  核心: 主线程等待 + 主线程获取变化
         * 1. Synchronized + wait\nofity 方法。
         *   主、子线程都 synchronizedCountDownLetchCountDownLetchCountDownLetch 包裹，主线程wait() 子线程notify（） 。
         * 2. ReentrantLock + Condition 实现。
         *   一样的思路，主、子线程都用 park 和 unpark包裹，主线程await(), 子线程 sign() .
         * 3. 使用Callable，直接使用Future 对象get 在主线程中获取子线程都结果。
         * 4. CountDownLatch 倒计时对象。构建时指明倒计时次数，主线程await() , 子线程处理完后 countDown() .
         * 5. CyclicBarrier 循环屏障对象。当所有线程（线程数量就是构建时都size）都达到公共屏障时，所有线程都将被释放。可重复使用。
         * 6. join 方法。
         * 7. Thread.sleep(等待) + volatile(获取变化) 用法. 循环判断volatile 结构是否改变.
         */
        //testDemo1();
        //testDemo2();
        //testDemo3();
        //testDemo4();
        //testDemo5();

        testDemo6();
        
        testDemo7();
        /**
         * 测试
         */
    }

    private static void testDemo7() {
    }

    private static void testDemo6() {

    }

    /**
     * 使用 synchronized 关键字加锁保证异步线程又同步效果
     */
    private static void testDemo1() {
        Demo1 demo1 = new Demo1();
        demo1.call();
        synchronized (demo1.lock) {
            try {
                demo1.lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("主线程内容");

    }

    /**
     * 通过ReentrantLock 重入锁判断
     */
    private static void testDemo2() {
        Demo2 demo2 = new Demo2();
        demo2.call();

        demo2.lock.lock();
        try {
            demo2.con.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            demo2.lock.unlock();
        }
        System.out.println("主线程内容");
    }

    /**
     * 测试 Future 异步转同步。
     */
    private static void testDemo3() {
        Demo3 demo3 = new Demo3();
        Future<Long> future = demo3.call();
        demo3.shutdown();

        try {
            System.out.println("主线程内容: " + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void testDemo4() {
        Demo4 demo4 = new Demo4();
        demo4.call();

        try {
            demo4.count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程内容");
    }

    private static void testDemo5() {
        Demo5 demo5 = new Demo5();
        demo5.call();

        try {
            demo5.barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println("主线程内容");
    }

    static class AsyncCall {
        private Random random = new Random(System.currentTimeMillis());

        private ExecutorService tp = Executors.newSingleThreadExecutor();

        //demo1,2,4,5调用方法
        public void call(BaseDemo demo) {
            new Thread(() -> {
                long res = random.nextInt(10);
                try {
                    Thread.sleep(res * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                demo.callback(res);
            }).start();
        }

        //demo3调用方法
        public Future<Long> futureCall() {

            return tp.submit(() -> {
                long res = random.nextInt(10);

                try {
                    Thread.sleep(res * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return res;
            });

        }

        public void shutdown() {

            tp.shutdown();

        }

    }

    static abstract class BaseDemo {

        protected AsyncCall asyncCall = new AsyncCall();

        public abstract void callback(long response);

        public String logPrefix() {
            return Thread.currentThread().getName() + " "
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " ";
        }

        public void call() {
            String prefix = logPrefix();
            System.out.println(prefix + "发起调用");
            asyncCall.call(this);
            System.out.println(prefix + "调用返回");
        }

    }

    static class Demo1 extends BaseDemo {
        public final Object lock = new Object();

        @Override
        public void callback(long response) {
            System.out.println("得到结果");
            System.out.println(response);
            System.out.println("调用结束");

            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    static class Demo2 extends BaseDemo {
        private ReentrantLock lock = new ReentrantLock();
        private Condition con = lock.newCondition();


        @Override
        public void callback(long response) {
            System.out.println("得到结果");
            System.out.println(response);
            System.out.println("调用结束");

            lock.lock();
            try {
                con.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    static class Demo3 {
        private AsyncCall async = new AsyncCall();

        public Future<Long> call() {
            System.out.println("得到结果");
            Future<Long> future = async.futureCall();
            System.out.println("调用结束");
            return future;
        }

        public void shutdown() {
            async.shutdown();
        }
    }

    static class Demo4 extends BaseDemo {
        public final CountDownLatch count = new CountDownLatch(1);


        @Override
        public void callback(long response) {
            System.out.println("得到结果");
            System.out.println(response);
            System.out.println("调用结束");
            count.countDown();
        }
    }

    static class Demo5 extends BaseDemo {
        public final CyclicBarrier barrier = new CyclicBarrier(2);

        @Override
        public void callback(long response) {
            System.out.println("得到结果");
            System.out.println(response);
            System.out.println("调用结束");

            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }


}
