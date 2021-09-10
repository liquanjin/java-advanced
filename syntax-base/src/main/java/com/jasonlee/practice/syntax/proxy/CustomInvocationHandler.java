package com.jasonlee.practice.syntax.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/10/21 11:48 AM
 */
@Slf4j
public class CustomInvocationHandler implements InvocationHandler {

    private Object object;

    public CustomInvocationHandler(Class clz) throws IllegalAccessException, InstantiationException {
        this.object = clz.newInstance();
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("方法执行前~");
        Object result = method.invoke(object, args);
        log.info("方法执行后~");
        return result;
    }
}
