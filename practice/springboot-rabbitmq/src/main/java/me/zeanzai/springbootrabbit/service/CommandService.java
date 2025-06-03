package me.zeanzai.springbootrabbit.service;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
public interface CommandService {

    // 构造num个设备信息
    Long genDevice(int num);

    // 发送单条命令
    void sendSingleMsg();

    // 发送同步抄表命令
    void syncChaobiao();

    void syncChaobiao1();

    // 异步抄表
    void asyncChaobiao();

//    Map<String, Long> getsyncChaobiao1();

    // SpringBoot 原生框架中的注解，异步发送命令


    // 阻塞队列+多线程方式发送命令


    // 批量抄表
    void sendPatchCmd();


    // 模拟 指令结果 返回到队列
    void sendCmdResultToQueue();

    // 模拟 自动上报 消息
    void dataReportToQueue();
}
