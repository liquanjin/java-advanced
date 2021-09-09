package com.jasonlee.practice.jvm.bytecode;

/**
 * 属性初始化顺序: 六步; 常量不管; 静态优先,属性优先; 父类优先,属性(Initialization)优先,代码块优先;
 * 1. 常量不用类初始化即可使用. final static
 * 2. 初始化静态变量. 编译后的class 文件内会有一个 clinit 方法专门做此事.
 * 3. 执行 static 代码块.
 * 以上部分,涉及类初始化步骤有: loadClass,verification,preparation,resolution
 *  类还未进行实例化 ,以下部分为实例化的顺序.以下步骤都是父类优先原则.
 * 4. initialization . 给所有字段赋初始值. 这一步的代码是class 文件编译时控制的.
 * 5. 调用 init {} 代码块
 * 6. 调用 构造方法.
 *
 * @author : liquanjin
 * @version :
 * @createAt : 8/30/21 2:46 PM
 */
public class T_01000_ByteCode006 {
    public int a = 2;
    public static int B = 20;
    public final static int C = 200;

    static {
        System.out.println("006_static code~~");
        System.out.println("B == " + B);
    }

    {
        System.out.println("006_init code~");
        System.out.println("006_init code. a == " + a);
        System.out.println();
    }

    public T_01000_ByteCode006() {
        System.out.println("006_construct method~~");
        System.out.println("a == " + a);
        System.out.println();
    }

}
