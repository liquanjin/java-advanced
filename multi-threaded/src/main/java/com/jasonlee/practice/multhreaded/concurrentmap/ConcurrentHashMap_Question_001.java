package com.jasonlee.practice.multhreaded.concurrentmap;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 并发时产生的bug.
 * <p>
 * <p>
 * <p>
 * ref:
 *  1. https://juejin.cn/post/6844904191077384200
 *  2. https://bugs.openjdk.java.net/browse/JDK-8062841
 *
 * @author : jason Lee
 * @version :
 * @createAt : 9/2/21 12:03 PM
 */
@Slf4j
public class ConcurrentHashMap_Question_001 {
    public static void main(String[] args) {
        concurrentHashMapBug();
    }

    private static void concurrentHashMapBug() {
        Map<String, Integer> map = new ConcurrentHashMap<>(16);
        map.computeIfAbsent(
                "AaAa",
                key -> map.computeIfAbsent("BBBB", key1 -> 42)
        );
        log.info(map.toString());
    }

}
