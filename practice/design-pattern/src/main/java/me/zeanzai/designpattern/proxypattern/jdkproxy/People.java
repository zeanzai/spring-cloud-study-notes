package me.zeanzai.designpattern.proxypattern.jdkproxy;


/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/25
 */
public class People implements Litigation {

    private String name;

    private String issueName;

    @Override
    public void claim() {
        System.out.println("【" +this.getName()+ "】出席， "+this.issueName+"...");
    }

    public People(String name, String issueName) {
        this.name = name;
        this.issueName = issueName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
}
