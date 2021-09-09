package com.jasonlee.practice.multhreaded.syncword;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * synchronized 的使用方式
 *
 * @author : liquanjin
 * @version :
 * @createAt : 2021/3/22 7:13 下午
 */
@Slf4j
public class Synchronized_Base_001 {

    public static void main(String[] args) {
        syncLockObject();

        //syncLockClass();
    }


    /**
     * synchronized 对 Object(某个具体示例) 对象加锁.
     * 就是在该对象的对象头中添加了标记. 所以该对象任何 synchronized 方法都不能调用了
     * <p>
     * 理解:
     */
    @SneakyThrows
    private static void syncLockObject() {
        Long lockTime = 1000L;
        Synchronized_Base_001 base = new Synchronized_Base_001(lockTime);
        ExecutorService service = Executors.newCachedThreadPool();
        // 先执行 object 加锁的方法
        service.submit(() -> base.syncUseByThisObj(lockTime * 9));
        Thread.sleep(lockTime);

        // 受影响:object加锁, 以及该实例的synchronized方法
        service.submit(() -> base.syncUseByThisObj(null));
        service.submit(() -> base.syncUsedForMethod(null));
        service.submit(() -> base.syncUsedForMethod_2(null));

        //没有影响: Class 对象. 其他实例
        service.submit(() -> new Synchronized_Base_001(500L).syncUseByThisObj(null));
        service.submit(() -> base.syncUseByThisClass(null));
        service.submit(() -> base.noSyncMethod(null));

        service.shutdown();
    }


    /**
     * synchronized 针对 Class 加锁的效果
     * 理解:
     * 相当于针对当前Class 对象加锁了,所以静态方法上使用 synchronized 的也不能用了.
     * 当然还有 synchronized(Synchronized_Base_001.class) 也不能用
     */
    @SneakyThrows
    private static void syncLockClass() {
        Long lockTime = 1000L;
        Synchronized_Base_001 base = new Synchronized_Base_001(lockTime);
        ExecutorService service = Executors.newCachedThreadPool();
        // class加锁的方法
        service.submit(() -> base.syncUseByThisClass(lockTime * 9));
        Thread.sleep(lockTime);

        //受影响 class 对象的方法
        service.submit(() -> base.syncUseByThisClass(null));
        service.submit(() -> Synchronized_Base_001.syncUsedForStaticMethod(lockTime));

        //不受影响: object,实例方法,普通方法
        service.submit(() -> base.syncUseByThisObj(null));
        service.submit(() -> base.noSyncMethod(null));
        service.submit(() -> base.syncUsedForMethod(null));

        service.shutdown();
    }

    private Long lockTimeForMS;

    public Synchronized_Base_001(Long lockTimeForMS) {
        this.lockTimeForMS = lockTimeForMS;
    }

    public void noSyncMethod(Long times) {
        try {
            log.info("noSyncMethod start~");
            Thread.sleep(times == null ? lockTimeForMS : times);
            log.info("noSyncMethod finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void syncUsedForMethod(Long times) {
        try {
            log.info("syncUsedForMethod start~");
            Thread.sleep(times == null ? lockTimeForMS : times);
            log.info("syncUsedForMethod finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void syncUsedForMethod_2(Long times) {
        try {
            log.info("syncUsedForMethod_2 start~");
            Thread.sleep(times == null ? lockTimeForMS : times);
            log.info("syncUsedForMethod_2 finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void syncUsedForStaticMethod(@NonNull Long times) {
        try {
            log.info("syncUsedForStaticMethod start~");
            Thread.sleep(times);
            log.info("syncUsedForStaticMethod finish");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void syncUseByThisClass(Long times) {
        synchronized (Synchronized_Base_001.class) {
            try {
                log.info("syncUseByThisClass start~");
                Thread.sleep(times == null ? lockTimeForMS : times);
                log.info("syncUseByThisClass finish");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void syncUseByThisObj(Long times) {
        synchronized (this) {
            try {
                log.info("hashCode:{} syncUseByThisObj start~", System.identityHashCode(this));
                Thread.sleep(times == null ? lockTimeForMS : times);
                log.info("hashCode:{} syncUseByThisObj finish", System.identityHashCode(this));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
