package com.jasonlee.practice.multhreaded.concurrentmap;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 并发时产生的bug.
 * <p>
 * ref:
 * 1. https://juejin.cn/post/6844904191077384200
 * 2. https://bugs.openjdk.java.net/browse/JDK-8062841
 * <p>
 * fix:
 * 1.  先调用了 get 方法，如果返回为 null，则调用 putIfAbsent 方法，这样就能实现和之前一样的效果了。
 * 2. 升级jdk 1.9
 *
 * @author : jason Lee
 * @version :
 * @createAt : 9/2/21 12:03 PM
 */
@Slf4j
public class ConcurrentHashMap_Question_001 {
    public static void main(String[] args) {
        //concurrentHashMapBug();

        concurrentHashMapBugFix();

    }

    private static void concurrentHashMapBugFix() {
        Map<String, Integer> map = new ConcurrentHashMap<>(16);
        map.computeIfAbsent(
                "AaAa",
                key -> {
                    Integer result = map.get("BBBB");
                    if (result == null) {
                        return map.computeIfAbsent("BBBB", key1 -> 42);
                    }
                    return result;
                }
        );

        log.info(map.toString());
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
