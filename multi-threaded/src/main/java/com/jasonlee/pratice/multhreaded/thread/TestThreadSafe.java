package com.jasonlee.pratice.multhreaded.thread;

import com.jasonlee.pratice.multhreaded.bo.Address;
import com.jasonlee.pratice.multhreaded.bo.AddressChild;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/21 4:03 下午
 */
public class TestThreadSafe {

    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public TestThreadSafe() {
    }

    public TestThreadSafe(Address address) {
        this.address = address;
    }


    private Map<String, Address> map = new HashMap<>();

    public Address computeIfPresent(String key,
                                    Function<? super String, ? extends Address> mappingFunction)
            throws Exception {

        Address address = null;
        if ((address = map.get(key)) != null) {
            synchronized (address) {
                System.out.println("get synchronized");

            }
        } else {
            AddressChild o = new AddressChild();
            map.put(key, o);

            synchronized (o) {
                System.out.println("init synchronized");
                Address init = mappingFunction.apply(key);

                Thread.sleep(100000);
            }
        }
        return address;
    }


    public static void main(String[] args) throws Exception {

        //testVectorRun();

        //testConcurrentHashMap();

        testSynchronized();
    }

    public void accessSynchronized() throws InterruptedException {
        synchronized (address) {
            String name = Thread.currentThread().getName();
            System.out.println("threadName :" + name + " .entry accessSynchronized method! " + address.toString());

            address.setStreet(name + Long.valueOf(System.currentTimeMillis()).toString());
            Thread.sleep(10000);


            System.out.println("threadName :" + name + " . accessSynchronized method sleep after!" + address.toString());
        }
    }

    private static void testSynchronized() throws Exception {
        //Address address = new Address("湖南", "长沙", "岳麓区", "杜鹃街道");
        //
        //TestThreadSafe testThreadSafe = new TestThreadSafe();
        //testThreadSafe.setAddress(address);
        //
        //ExecutorService executorService = Executors.newSingleThreadExecutor();
        //executorService.submit(() -> {
        //    try {
        //        new TestThreadSafe(address).accessSynchronized();
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //});
        //
        //testThreadSafe.accessSynchronized();


        TestThreadSafe testThread = new TestThreadSafe();

        testThread.computeIfPresent("aa",
                key -> {
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    Future<Address> future = service.submit(
                            () -> testThread.computeIfPresent("aa", key2 -> new Address("湖南", "长沙", "岳麓区", "杜鹃街道"))
                    );
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return null;
                });


    }

    /**
     * 测试 concurrentHashMap 并发问题
     */
    private static void testConcurrentHashMap() {
        Map<String, Integer> map = new ConcurrentHashMap<>(16);
        map.computeIfAbsent(
                "AaAa",
                key -> map.computeIfAbsent("BBBB", key1 -> 42)
        );

        System.out.println(map.toString());
    }

    /**
     * 即使是线程安全的对象, 也不要忘了某些属性可能有线程问题.
     * 比如:Iterator 迭代器
     *
     * @throws InterruptedException
     */
    private static void testVectorRun() throws InterruptedException {
        Vector<Integer> vector = new Vector<>();
        // 先存放1000个值让iterator有值可以遍历
        for (int i = 0; i < 10; i++) {
            vector.add(i);
        }

        Thread iteratorThread = new Thread(new IteratorRunnable(vector));
        iteratorThread.start();

        // 主线程休眠5秒，让iteratorThread能够充分跑起来。这段时间是不会有问题的。
        TimeUnit.MILLISECONDS.sleep(1);

        // 该线程启动之后，会结构化修改Vector，然后就会抛出ConcurrentModificationException异常
        Thread modifyVectorThread = new Thread(new ModifyVectorRunnable(vector));
        modifyVectorThread.start();
    }


    /**
     * 这个Runnable会不断使用迭代器(for-each语句)遍历Vector
     */
    private static class IteratorRunnable implements Runnable {

        private Vector<Integer> vector;

        public IteratorRunnable(Vector<Integer> vector) {
            this.vector = vector;
        }

        @Override
        public void run() {
            System.out.println("IteratorRunnable run ");
            for (int a = 0; a < 100; a++) {
                //synchronized (vector) {
                //    //todo : 即使是线程安全的,也要注意使用的方式. Iterator线程不安全
                //    for (Integer i : vector) {
                //        System.out.println(i);
                //    }
                //}

                // 如果 使用get 就没问题
                for (int i = 0; i < vector.size(); i++) {
                    System.out.println(vector.get(i));
                }
            }
        }
    }

    /**
     * 这个Runnable会不断添加新元素，也就是会结构化修改Vector
     */
    private static class ModifyVectorRunnable implements Runnable {
        private Vector<Integer> vector;

        public ModifyVectorRunnable(Vector<Integer> vector) {
            this.vector = vector;
        }

        @Override
        public void run() {
            System.out.println("ModifyVectorRunnable run ");
            for (int a = 100; a < 200; a++) {
                vector.add(a);
                System.out.println("modify 线程抢占成功了~");
            }
        }
    }
}
