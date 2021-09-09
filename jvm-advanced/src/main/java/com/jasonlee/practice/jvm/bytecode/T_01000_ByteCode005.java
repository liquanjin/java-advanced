package com.jasonlee.practice.jvm.bytecode;

/**
 * 1. 静态变量先进行初始化.
 * 2. Jclasslib 中. clinit 方法就是静态变量初始化的代码块;
 *   init 是构造方法代码块. 对比 003 Jclasslib;
 *
 *
 * @author : liquanjin
 * @version :
 * @createAt : 8/30/21 2:46 PM
 */
public class T_01000_ByteCode005 {
    public static int a = 2; // 0
    public static T_01000_ByteCode005 t = new T_01000_ByteCode005();// null

    private T_01000_ByteCode005() {
        a++;
    }
}
