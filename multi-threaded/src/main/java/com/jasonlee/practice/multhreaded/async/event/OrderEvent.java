package com.jasonlee.practice.multhreaded.async.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/13 11:10 上午
 */
public class OrderEvent extends ApplicationEvent {
    private String phone;

    public OrderEvent(String phone) {
        super(phone);
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "OrderEvent{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
