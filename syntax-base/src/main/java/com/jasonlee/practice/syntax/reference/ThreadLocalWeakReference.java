package com.jasonlee.practice.syntax.reference;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 *  分析为什么 ThreadLocal 中使用了WeakReference
 * @author : jason Lee
 * @version :
 * @createAt : 9/16/21 4:59 PM
 */
@Slf4j
public class ThreadLocalWeakReference {
    public static void main(String[] args) throws Exception {
        testThreadLocal();
    }


    /**
     * 核心:
     * 1.  {@link java.lang.ThreadLocal.ThreadLocalMap.Entry} 查看该类的结构
     *  内部弱引用的是 key对象而不是value; 同时key对象为 ThreadLocal 对象.
     * 2.  ThreadLocal中的 {@link java.lang.ThreadLocal.ThreadLocalMap#getEntryAfterMiss} 方法和
     *     {@link java.lang.ThreadLocal.ThreadLocalMap#expungeStaleEntry} 两个方法已经表明了get是如果key为空时,
     *     会将该对象从Entry.table 中移除掉
     * 作用:
     * 1. 如果ThreadLocal 对象没有强引用&GC后,意味着该ThreadLocal中保存的数据将无法被获取,为了便于GC所以内部使用了WeakReference.
     * 2. 虽然Key是WeakReference,但是Value 依然存在Entry中.
     * 3. 所以ThreadLocal 在get方法中添加逻辑检验, 移除 key 为 null 对应的 value .
     *
     */
    private static void testThreadLocal() throws Exception {
        ThreadLocal<String> local = new ThreadLocal<>();
        String content = new String("test");
        local.set(content);

        log.info("GC before:{}", local.get());

        content = null;
        System.gc();
        // 事实证明. 即使 content即使只有ThreadLocal引用时也不会被GC
        log.info("GC after:{}", local.get());

        // 测试 当 ThreadLocal 变为null后的效果
        log.info("build ThreadLocal set null after");


        ThreadLocal<String> emptyObject = new ThreadLocal<>();
        Thread currentThread = Thread.currentThread();
        // 通过反射获取到当前 线程中的 ThreadLocal.ThreadLocalMap threadLocals
        Class clz = Class.forName("java.lang.ThreadLocal");
        Method method = clz.getDeclaredMethod("getMap", new Class[]{Thread.class});
        method.setAccessible(true);

        // 通过Debug 对比 test 关联的 key
        Object object =method.invoke(emptyObject,currentThread );
        local = null;
        System.gc();
        object =method.invoke(emptyObject,currentThread );

        log.info("content:" );
    }
}
