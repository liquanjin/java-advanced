package com.jasonlee.practice.jvm.command;

/**
 * 指令重排 & happen before 原则
 *
 * @author : liquanjin
 * @version :
 * @createAt : 2021/4/6 2:46 下午
 */
public class RearrangeCommand {
    private static int x = 0, y = 0;
    private static int a = 0, b = 0;

    public static void main(String[] args) throws InterruptedException {
        //rearrangeOne();

        happenBefore();
    }

    /**
     * happen before 原则:
     * 指令重排不能改变程序逻辑顺序.
     * 可能发生指令重排，但是依然x =2 。
     */
    public static void happenBefore() {
        int x, y;
        x = 1;
        try {
            x = 2;
            y = 0 / 0;
        } catch (Exception e) {
        } finally {
            System.out.println("x = " + x);
        }
    }

    //private static void rearrangeOne() throws InterruptedException {
    //    for (int i = 0; ; i++) {
    //        x = 0;
    //        y = 0;
    //        a = 0;
    //        b = 0;
    //        Thread one = new Thread(() -> {
    //            a = 1;
    //            x = b;
    //        });
    //        Thread other = new Thread(() -> {
    //            b = 1;
    //            y = a;
    //        });
    //        one.start();
    //        other.start();
    //        one.join();
    //        other.join();
    //
    //        //默认情况下，两个线程执行完毕后a、b 至少有一个变成1。所以不会出现都为0的场景
    //        // 由于发生了指令重排，所以出现问题。
    //        if (x == 0 && y == 0) {
    //            String result = "第" + i + "次（" + x + ", " + y + "）";
    //            System.out.println(result);
    //        }
    //    }
    //}
}
