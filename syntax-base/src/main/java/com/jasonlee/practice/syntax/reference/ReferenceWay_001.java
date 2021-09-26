package com.jasonlee.practice.syntax.reference;


import com.jasonlee.practice.syntax.bo.Person;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/11 10:34 上午
 */
@Slf4j
public class ReferenceWay_001 {

    /**
     * 强引用\软引用\弱引用
     * 引用方式:
     * 堆内存中的一个对象可以用N种引用方式.
     * Object o= new Object(); 第一种,强引用.
     * WeakReference<Object> weak =new WeakReference(); 第二种,弱引用.
     * 只要该对象存在强引用的关系,那么肯定不会被回收;
     * 但如果没有任何强引用,只存在弱引用,那么GC的时候该对象会被回收.
     * 如果没有强引用,只存在软引用,如果空间不充足也会被回收.
     * <p>
     * 1. 强:
     * 1.1 最普通的使用方式. 对象直接关联 String str =new String();
     * 1.2 GC垃圾回收时,即使内存不足也不会回收. 除非手动设置为null
     * 2. 软引用.
     * 2.1 SoftReference . 如果垃圾回收时发现空间不足,会回收软引用对象
     * 3. 弱引用
     * 3.1 WeakReference. 只要发生垃圾回收 并且,所有的WeakReference对象都会被回收.
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 美团 面试题
        //System.out.println(meituanIV_01());
        //System.out.println(meituanIV_02());

        testWeakHashMap();

    }

    /**
     * 美团面试题
     * WeakHashMap 的key 是弱引用.
     * <p>
     * WeakReference 也是弱引用
     *
     * @return
     */
    private static String meituanIV_01() {
        String a = new String("a");
        WeakReference<String> b = new WeakReference<String>(a);
        WeakHashMap<String, Integer> weakMap = new WeakHashMap<String, Integer>();
        weakMap.put(b.get(), 1);
        a = null;
        System.gc();
        String c = "";
        try {
            c = b.get().replace("a", "b");
            return c;
        } catch (Exception e) {
            c = "c";
            return c;
        } finally {
            c += "d";
            return c + "e";
        }
    }

    /**
     * 把源对象 放入 强引用的 HashMap中后(hashMap.put(b.get(), 1))
     * GC时对象就不会被移除.
     *
     * @return string
     */
    private static String meituanIV_02() {
        String a = new String("a");
        WeakReference<String> b = new WeakReference<String>(a);
        //区别 .HashMap
        Map<String, Integer> hashMap = new HashMap<String, Integer>();
        hashMap.put(b.get(), 1);
        a = null;
        System.gc();
        String c = "";
        try {
            c = b.get().replace("a", "b");
            return c;
        } catch (Exception e) {
            c = "c";
            return c;
        } finally {
            c += "d";
            return c + "e";
        }
    }

    private static void testWeakHashMap() {
        WeakHashMap w = new WeakHashMap();
        //三个key-value中的key 都是匿名对象，没有强引用指向该实际对象
        w.put(new String("语文"), new String("优秀"));
        w.put(new String("数学"), new String("及格"));
        w.put(new String("英语"), new String("中等"));
        //增加一个字符串的强引用. "java" 是常量池的内容
        w.put("java", new String("特别优秀"));
        System.out.println(w);

        //通知垃圾回收机制来进行回收
        System.gc();
        //再次输出w
        System.out.println("第二次输出:" + w);
    }
}
