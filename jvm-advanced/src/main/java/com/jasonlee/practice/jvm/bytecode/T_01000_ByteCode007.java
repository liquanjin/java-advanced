package com.jasonlee.practice.jvm.bytecode;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 8/30/21 2:46 PM
 */
public class T_01000_ByteCode007 extends T_01000_ByteCode006 {
    public int h = 3;
    public static int J = 30;
    public final static int K = 300;

    static {
        System.out.println("07_static code~~");
        System.out.println("J == " + J);
    }

    {
        System.out.println("007_init code~");
        System.out.println("007_init code. h == " + h);
        System.out.println();
    }

    public T_01000_ByteCode007() {
        System.out.println("07_construct method~~");
        System.out.println("h == " + h);
        System.out.println();
    }


}
