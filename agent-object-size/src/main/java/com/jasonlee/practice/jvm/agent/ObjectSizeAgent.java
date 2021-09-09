package com.jasonlee.practice.jvm.agent;

import java.lang.instrument.Instrumentation;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/2/21 3:43 PM
 */
public class ObjectSizeAgent {
    private static Instrumentation instr;

    /**
     * 标准格式
     *
     * @param agentArgs
     * @param instrument
     */
    public static void premain(String agentArgs, Instrumentation instrument) {
        instr = instrument;
    }

    public static long sizeOf(Object object) {
        return instr.getObjectSize(object);
    }
}
