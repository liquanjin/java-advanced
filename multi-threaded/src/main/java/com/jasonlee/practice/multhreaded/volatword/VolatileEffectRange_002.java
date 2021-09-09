package com.jasonlee.practice.multhreaded.volatword;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 当Volatile 修饰对象时, 该对象的属性是否也具有 volatile 的特点?
 * <p>
 * 补充: volatile 最好不要这样来用. 可使用对象锁和synchronized 关键字来实现.
 *
 * @author : liquanjin
 * @version :
 * @createAt : 2021/5/28 4:58 下午
 */
@Slf4j
public class VolatileEffectRange_002 {

    private volatile static Person person = Person.getInstance();

    public static void main(String[] args) {

        testIntFieldSync();

        //testStringFieldSync();
    }

    /**
     * 当对象使用 volatile 修饰时,对象类的String属性在线程间也可同步.
     */
    private static void testStringFieldSync() {
        new Thread(() -> {
            int nameLength = person.getName().length();
            while (person.getName().length() < Person.MAX_NAME_LENGTH) {
                if (nameLength != person.getName().length()) {
                    log.info("person's name changed. newValue:{}", person.getName());
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Reader").start();


        new Thread(() -> {
            String localName = person.getName();
            while (person.getName().length() < Person.MAX_NAME_LENGTH) {
                localName = localName + UUID.randomUUID().toString().substring(0, 1);
                log.info("person's name updating~ newValue:{}", localName);
                person.name = localName;

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "Updater").start();

    }


    /**
     * 当对象使用 volatile 修饰时,对象类的int属性在线程间也可同步.
     */
    private static void testIntFieldSync() {
        new Thread(() -> {
            int localAge = person.getAge();
            while (person.getAge() < Person.AGE_MAX) {
                if (localAge != person.age) {
                    log.info("person's age read difference. newValue:{}", person.getAge());
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(1800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Reader").start();


        new Thread(() -> {
            int localAge = person.getAge();
            while (person.getAge() < Person.AGE_MAX) {
                log.info("person's age updating~ newValue:{}", ++localAge);
                person.age = localAge;

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }, "Updater").start();

    }

    static class Person {
        int age = 0;
        public static final int AGE_MAX = 5;
        String name = "Li";
        public static final int MAX_NAME_LENGTH = 10;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static Person getInstance() {
            return PersonHolder._instance;
        }

        private Person() {
        }


        static class PersonHolder {
            public static Person _instance = new Person();
        }
    }
}
