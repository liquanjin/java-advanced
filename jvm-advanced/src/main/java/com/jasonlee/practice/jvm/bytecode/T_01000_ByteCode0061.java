package com.jasonlee.practice.jvm.bytecode;

/**
 * 属性初始化顺序:
 * 1. 常量不用类初始化即可使用. final static
 * 2. 初始化静态变量. class 文件内会有一个 clinit 方法专门做此事.
 * 3. 执行 static 代码块.
 * 4. 调用构造方法. class 文件中称为 init 方法.
 * init 方法中设置field 的默认值.
 *
 * @author : liquanjin
 * @version :
 * @createAt : 8/30/21 2:46 PM
 */
public class T_01000_ByteCode0061 {
    public int a = 7;
    public static int B = 20;

    {
        a= 5;
    }

    public T_01000_ByteCode0061() {
        a++;
    }

}
