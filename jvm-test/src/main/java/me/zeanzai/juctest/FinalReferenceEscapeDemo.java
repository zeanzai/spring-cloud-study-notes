package me.zeanzai.juctest;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FinalReferenceEscapeDemo {
    private final int a;
    private FinalReferenceEscapeDemo referenceDemo;

    public FinalReferenceEscapeDemo() {
        a = 1;  //1
        referenceDemo = this; //2
    }

    public void writer() {
        new FinalReferenceEscapeDemo();
    }

    public void reader() {
        if (referenceDemo != null) {  //3
            int temp = referenceDemo.a; //4
            if (temp == 1) {
                System.out.println(temp);
            }else{
                System.out.println("null");
            }

        }

    }

    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
            200,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(100000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());
    public static void main(String[] args) {
        for (AtomicInteger i =new AtomicInteger(0); i.get() < 10000; i.incrementAndGet()) {
            FinalReferenceEscapeDemo finalReferenceEscapeDemo = new FinalReferenceEscapeDemo();
            executor.submit(()->{
                System.out.println("i="+i.get());
                finalReferenceEscapeDemo.reader();

                System.out.println();
            });
        }
        executor.shutdown();
    }
}