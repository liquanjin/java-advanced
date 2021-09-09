package com.jasonlee.practice.multhreaded.syncword;


import com.jasonlee.practice.multhreaded.bo.Address;
import com.jasonlee.practice.multhreaded.bo.AddressChild;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 *
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/22 7:03 下午
 */
@Slf4j
public class Synchronized_Question_001 {
    private Map<String, Address> map = new HashMap<>();

    /**
     *  synchronized 死锁问题.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        testSynchronizedObj();
    }

    private static void testSynchronizedObj() throws Exception {
        Synchronized_Question_001 synchronizedQuestion001 = new Synchronized_Question_001();
        synchronizedQuestion001.computeIfPresent("aa",
                key -> {
                    // 内部模拟另外一个线程
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    Future<Address> future = service.submit(
                            () -> synchronizedQuestion001.computeIfPresent("aa",
                                    key2 -> new Address("湖南", "长沙", "岳麓区", "杜鹃街道"))
                    );

                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );
    }

    /**
     * 验证. synchronized 关键字锁住某个对象后, 该对象只能被该线程访问.
     * 如果异步其他线程也来访问, 将出现死锁.
     *
     * @param key
     * @param mappingFunction
     * @return
     * @throws Exception
     */
    public Address computeIfPresent(String key,
                                    Function<? super String, ? extends Address>
                                            mappingFunction) throws Exception {

        Address address = null;
        if ((address = map.get(key)) != null) {
            // 如果是其他线程访问被 init 锁住的对象, 将陷入死锁
            log.info("马上开始死锁~");
            synchronized (address) {
                log.info("get synchronized");
            }
        } else {
            AddressChild addressChild = new AddressChild();
            map.put(key, addressChild);
            synchronized (addressChild) {
                log.info("init synchronized");
                Address init = mappingFunction.apply(key);

                Thread.sleep(100000);
            }
        }
        return address;
    }


}
