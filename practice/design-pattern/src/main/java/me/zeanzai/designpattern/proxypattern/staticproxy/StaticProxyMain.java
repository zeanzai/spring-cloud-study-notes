package me.zeanzai.designpattern.proxypattern.staticproxy;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/25
 */
public class StaticProxyMain {
    public static void main(String[] args) {
        Litigation laowang = new People("laowang", "别车官司");
        Litigation xiaogao = new Lawyer("xiaogao", laowang);
        xiaogao.claim();

        System.out.println("==============");

        Litigation lisi = new People("lisi", "离婚官司");
        Litigation xiaozhang = new Lawyer("xiaozhang", lisi);
        xiaozhang.claim();

        System.out.println("==============");


    }
}
