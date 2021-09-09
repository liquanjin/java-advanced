package com.jasonlee.practice.multhreaded.volatword;

/**
 * 通过 jvm 汇编解析查看使用的 指令.
 * 查看方式: -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
 *
 * 工具参考:
 * https://juejin.cn/post/6844904038580879367
 */
public class VolatileInstruct_001 {

    private volatile String content;

    public String getContent() {
        if (content == null) {
            //赋值处，通过汇编解析。可查看到 lock addl 命令
            content = "hello";
        }
        return content;
    }

    public static void main(String[] args) {
        new VolatileInstruct_001().getContent();
    }

}