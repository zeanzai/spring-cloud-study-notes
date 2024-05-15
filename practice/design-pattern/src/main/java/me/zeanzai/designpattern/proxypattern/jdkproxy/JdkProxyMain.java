package me.zeanzai.designpattern.proxypattern.jdkproxy;


import java.lang.reflect.Proxy;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/25
 */
public class JdkProxyMain {
    public static void main(String[] args) {

        People laowang = new People("laowang", "别车官司");

        JdkInvocationHandler jdkInvocationHandler = new JdkInvocationHandler(laowang, "xiaogao");
        Litigation laowangdaguansi = (Litigation) Proxy.newProxyInstance(
                People.class.getClassLoader(),
                People.class.getInterfaces(),
                jdkInvocationHandler);
        laowangdaguansi.claim();


        People xiaozhang = new People("xiaozhang", "离婚官司");
        JdkInvocationHandler xiaogao = new JdkInvocationHandler(xiaozhang, "xiaogao");
        Litigation xiaozhangdaguansi = (Litigation) Proxy.newProxyInstance(
                People.class.getClassLoader(),
                People.class.getInterfaces(),
                xiaogao
        );
        xiaozhangdaguansi.claim();


    }
}
