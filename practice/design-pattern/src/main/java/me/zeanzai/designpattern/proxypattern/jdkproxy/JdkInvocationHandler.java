package me.zeanzai.designpattern.proxypattern.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/25
 */
public class JdkInvocationHandler implements InvocationHandler {
    private String name;

    private Object object;

    public JdkInvocationHandler(Object object, String name) {
        this.object = object;
        this.name = name;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        prepare();
        evidence();
        Object invoke = method.invoke(object, args);
        judgement();

        return invoke;
    }

    private String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private void prepare() {
        System.out.println("【"+this.getName()+"】出席， 准备诉状...");
    }

    private void evidence() {
        System.out.println("【"+this.getName()+"】出席， 固定证据...");
    }


    private void judgement() {
        System.out.println("【"+this.getName()+"】出席， 参与判决...");
    }
}
