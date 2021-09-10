package com.jasonlee.practice.syntax.proxy;

import com.jasonlee.practice.syntax.proxy.service.ProxyInterface;
import com.jasonlee.practice.syntax.proxy.service.ProxyInterfaceImpl;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/10/21 11:37 AM
 */
public class JDKProxyBase_001 {

    /**
     * 如何调用.
     * 限制产生的原因.
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        //useJDKProxy();

        analysisProxy();
    }


    /**
     * jdk 代理基础使用
     * 1. 要求代理类必须要实现接口. (因为真实方法调用时,是通过接口的方法名来调用)
     * 2. 代理的类不能继承其他类(内部要继承)
     */
    private static void useJDKProxy() throws Exception {
        //bean 对象可来自自己构建,或反射构建
        CustomInvocationHandler handler = new CustomInvocationHandler(ProxyInterfaceImpl.class);
        ProxyInterface obj = (ProxyInterface) Proxy.newProxyInstance(JDKProxyBase_001.class.getClassLoader(),
                new Class[]{ProxyInterface.class}, handler);
        obj.sayHi();
    }

    /**
     * 分析反射生成的class 文件
     * 1. 内部会继承一个Proxy 对象
     * 2. 方法的调用,使用的是接口的名称
     *
     * @throws IOException
     */
    private static void analysisProxy() throws IOException {
        String name = "$ProxyInterface";
        byte[] classByte = ProxyGenerator.generateProxyClass(name, new Class[]{ProxyInterface.class}, 1);

        String path = JDKProxyBase_001.class.getClassLoader().getResource(".").getPath() + name + ".class";
        FileOutputStream outputStream = new FileOutputStream(new File(path));
        outputStream.write(classByte);
        outputStream.flush();
    }


}
