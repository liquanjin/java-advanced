package com.jasonlee.practice.jvm.verify;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;

/**
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/22 11:34 上午
 */
@Slf4j
public class IAddAddQuestion {

    /**
     * i ++ 是线程安全的吗?
     * <p>
     * 答案:
     * 不是. 即使用volatile 修饰也不是.
     * 同理,value = value +1 也是.
     *
     * 原因:
     *
     * 涉及到 三步运算. 取值入栈; 栈内自增; 出栈后更新局部变量表.
     * 只能保证 取值入栈时,该值为内存最新值.
     * 但是栈内自增,更新局部变量表时不能保证这期间i的值不被修改.
     *
     */
    public static void main(String[] args) throws Exception {
        int cycCount = 20;
        IncrementDemoB demoA = new IncrementDemoB(cycCount, true);
        log.info("线程数" + processor);
        Thread[] threads = new Thread[processor];
        for (int i = 0; i < processor; i++) {
            (threads[i] = new Thread(demoA)).start();
        }

        // 启动后join
        for (int i = 0; i < processor; i++)
            threads[i].join();

        if (!Integer.valueOf(cycCount * processor).equals(demoA.value)) {
            log.info("运算结果和预期结果不一致. 预期结果:{} , 运算结果:{}", cycCount * processor, demoA.value);
        } else {
            log.info("恰巧相等,再试一次");
        }
    }

    static int processor = Runtime.getRuntime().availableProcessors();

    /**
     * value ++ 就相当于 value = value +1 ;
     * 也就是说 只有能保证同一单位时间内,只有一个线程在 执行 value = value +1; 这个操作才能保证
     */
    @Slf4j
    static class IncrementDemoB implements Runnable {
        private CyclicBarrier barrier = new CyclicBarrier(processor);

        public volatile Integer value = 0;

        private int cycCount;
        private boolean useAddMethod = true;

        @Override
        public void run() {
            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 重新解读 value ++. 和 value = value +1

            // 因为 value ++ 是三步操作,所以并不能保证原子性
            //三步为:取值入栈; 栈自增; 栈顶出栈更新局部变量表
            // 原因: 并不能保证取值入栈的时候,取到的是最后一个线程操作后的结果.
            for (int i = 1; i <= cycCount; i++) {
                if (useAddMethod) {
                    value++;
                } else {
                    value = value + 1;
                }
            }
        }

        public IncrementDemoB(int cycCount, boolean useAddMethod) {
            this.cycCount = cycCount;
            this.useAddMethod = useAddMethod;
        }
    }


}
