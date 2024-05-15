package me.zeanzai.designpattern.proxypattern.staticproxy;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/25
 */
public class Lawyer implements Litigation{
    private String name;
    private Litigation client;

    private void prepare() {
        System.out.println("【"+this.getName()+"】出席， 准备诉状...");
    }

    private void evidence() {
        System.out.println("【"+this.getName()+"】出席， 固定证据...");
    }

    @Override
    public void claim() {
        this.prepare();
        this.evidence();
        client.claim();
        this.judgement();
    }

    private void judgement() {
        System.out.println("【"+this.getName()+"】出席， 参与判决...");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Lawyer(String name, Litigation client) {
        this.name = name;
        this.client = client;
    }

}
