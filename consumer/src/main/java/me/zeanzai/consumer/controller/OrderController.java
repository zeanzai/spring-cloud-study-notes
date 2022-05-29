package me.zeanzai.consumer.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zeanzai.consumer.model.Order;
import me.zeanzai.consumer.model.Payment;
import me.zeanzai.consumer.service.OpenFeignPaymentService;
import me.zeanzai.consumer.service.OpenFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/openfeign/order")
@Slf4j
@Api(value = "测试OpenFeign", tags = { "测试OpenFeign" })
public class OrderController {

    @Autowired
    private OpenFeignService openFeignService;

    @Autowired
    private OpenFeignPaymentService paymentService;

    @PostMapping("/create")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "post传递实体")
    public String create(@RequestBody Order order){
        //1. 调用生成订单服务
        Order order2 = openFeignService.createOrder2(order);
        log.info("下单成功：{}",order2.toString());

        Payment payment = Payment.builder()
                .id(1200L)
                .money(10000L)
                .build();
        String res = paymentService.pay(payment);
        log.info("付款成功：{}",res);
        return "success";
    }

    @PostMapping("/batchOrder")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "测试哨兵异常")
    public String batchOrder(@RequestBody List<Order> orders){
        return openFeignService.batchOrder(orders);
    }

}
