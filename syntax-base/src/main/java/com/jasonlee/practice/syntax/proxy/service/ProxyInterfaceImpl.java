package com.jasonlee.practice.syntax.proxy.service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/16 11:07 上午
 */
@Slf4j
public class ProxyInterfaceImpl implements ProxyInterface {

    //@Override
    public void sayHi() {
        log.info("hello word~");
    }
}
