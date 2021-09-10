package com.jasonlee.practice.syntax.proxy;

import com.jasonlee.practice.syntax.proxy.service.ProxyInterfaceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/10/21 3:41 PM
 */
@Slf4j
public class CGLibProxyBase_001 {

    /**
     * CGLIB(Code Generation Library)是一个基于ASM的字节码生成库，
     * 它允许我们在运行时对字节码进行修改和动态生成.
     *
     * @param args
     */
    public static void main(String[] args) {

        base();
    }

    /**
     *
     * feature:
     * 1. 类不能是finally , 因为CGLib 底层使用的是extend 机制
     * 2. 方法也不能时finally, 继承模式下无法覆盖
     *
     */
    private static void base() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                log.info("方法调用前");
                // 调用原始真实方法
                Object result = methodProxy.invokeSuper(o, objects);
                log.info("方法调用后");
                return result;
            }
        });

        enhancer.setSuperclass(ProxyInterfaceImpl.class);
        ProxyInterfaceImpl proxyInterface = (ProxyInterfaceImpl) enhancer.create();

        proxyInterface.sayHi();
    }
}
