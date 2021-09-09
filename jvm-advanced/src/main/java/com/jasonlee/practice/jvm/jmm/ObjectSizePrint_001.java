package com.jasonlee.practice.jvm.jmm;

import com.jasonlee.practice.jvm.agent.ObjectSizeAgent;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/2/21 4:03 PM
 */
public class ObjectSizePrint_001 {

    /**
     * 对象头大小
     * 配置命令:
     * -javaagent:${userHome}/work/workspace/me/java-advanced/jvm-advanced/src/main/resources/agent-object-size.jar
     * <p>
     * - 不开启; + 开启.
     * <p>
     * -XX:-UseCompressedClassPointers 不开启Class指针压缩.
     * -XX:+UseCompressedOops
     * Oops : ordinary object pointers. 普通对象指针的压缩.
     *
     * @param args
     */
    public static void main(String[] args) {


        /**
         * 头: 8字节
         * classPointer: 4字节. 有压缩
         * padding: 4字节.
         */
        System.out.println(ObjectSizeAgent.sizeOf(new Object()));

        /**
         * 是否开启压缩效果不一样
         * 头: 8 字节
         * classPointer: 4字节. 可能会压缩
         * 长度: 4字节.
         */
        System.out.println(ObjectSizeAgent.sizeOf(new int[]{}));


        System.out.println(ObjectSizeAgent.sizeOf(new G()));

    }

    private static class G {
        //8 _markword
        //4 class pointer 指针. 不压缩就是8 .
        int id;         //4
        //String name;    //4. 受 UseCompressedOops 影响. 不压缩就是8

    }
}
