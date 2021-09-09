package com.jasonlee.practice.syntax.operator;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author : jason Lee
 * @version :
 * @createAt : 9/8/21 8:51 PM
 */
@Slf4j
public class MultiEqual {
    public static void main(String[] args) {

        test();
    }

    private static void test() {
        String[] strArrays = new String[4];
        String content = "hello,dog崽";

        for (int i = 0; i < strArrays.length; i++) {
            strArrays[i] = Integer.valueOf(i).toString();
        }

        log.info("change before~");
        log.info(" arrays:{}, content:{}", Arrays.toString(strArrays), content);

        // 所有前面的内容都变成最后一个的值.
        strArrays[0] = content = strArrays[3];

        log.info("change after~");
        log.info(" arrays:{}, content:{}", Arrays.toString(strArrays), content);

    }
}
