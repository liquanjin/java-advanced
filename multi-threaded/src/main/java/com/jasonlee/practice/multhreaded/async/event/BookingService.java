package com.jasonlee.practice.multhreaded.async.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/12 10:01 下午
 */
@Slf4j
@Service
public class BookingService {

    private ApplicationContext context;

    private ExecutorService service = Executors.newCachedThreadPool();

    public BookingService(ApplicationContext context) {
        this.context = context;
    }

    /**
     * 通过Event 创建订单
     *
     * @param phone
     */
    public void createOrderByEvent(String phone) {
        OrderEvent orderEvent = new OrderEvent(phone);
        context.publishEvent(orderEvent);
    }

    @EventListener
    public void eventListener(OrderEvent orderEvent) {
        log.info("eventListener start. event:{}", orderEvent.toString());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("eventListener finish. 创建订单成功");
    }

    private void callRpcCreateOrder(String phone) {
        service.submit(() -> {
            log.info("异步调用创建订单开始 :{}", phone);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("异步调用创建订单完成 :{}", phone);
        });
        service.shutdown();
    }

    @Async
    public void createOrderWithAno(String phone) {
        callRpcCreateOrder(phone);
    }

}
