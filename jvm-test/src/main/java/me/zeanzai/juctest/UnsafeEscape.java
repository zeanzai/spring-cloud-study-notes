package me.zeanzai.juctest;

public class UnsafeEscape {
    public final int value;

    public UnsafeEscape(MyListener listener) {
        value = 42;
        listener.register(this); // this在构造函数中逸出了
    }

    public interface MyListener {
        void register(UnsafeEscape obj);
    }

    public static void main(String[] args) {
        MyListener listener = new MyListener() {
            @Override
            public void register(UnsafeEscape obj) {
                // 模拟在构造未完成时另一个线程访问该对象
                new Thread(() -> {
                    System.out.println("Value from another thread: " + obj.value);
                }).start();
            }
        };

        new UnsafeEscape(listener); // 构造函数中已经泄露 this
    }
}

