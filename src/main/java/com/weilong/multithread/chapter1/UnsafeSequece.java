package com.weilong.multithread.chapter1;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author weilong.
 * @description 非线程安全的数值序列生成器.
 */
@NotThreadSafe
public class UnsafeSequece {
    
    private int value;
    
    public int getNext() {
        return value++;
    }
    
    public static void main(String[] args) {
        //实例化对象后，一只调用，值会一直增加
        UnsafeSequece unsafeSequece = new UnsafeSequece();
        for (int i = 0; i < 10; i++) {
            System.out.println(unsafeSequece.getNext());
        }
    }
}
