package com.jasonlee.practice.jvm.bytecode;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 8/30/21 10:15 AM
 */
public class T_01000_ByteCode003 {

    private volatile Long b = 16L;

    public void setB() {
        this.b = 23L;
    }

    public Long getB() {
        return b;
    }
}
