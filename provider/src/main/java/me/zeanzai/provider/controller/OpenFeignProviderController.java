package me.zeanzai.provider.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zeanzai.provider.model.Order;
import me.zeanzai.provider.model.Payment;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping("/openfeign/provider")
@Api(value = "测试OpenFeign", tags = { "测试OpenFeign" })
public class OpenFeignProviderController {

    @GetMapping("/test/{id}")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "路径中带参数")
    public String test(@PathVariable("id")Integer id){
        return "accept one msg id="+id;
    }

    @PostMapping("/order1")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "传递实体")
    public Order createOrder1(Order order){
        return order;
    }

    @PostMapping("/order2")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "post传递实体")
    public Order createOrder2(@RequestBody Order order){
        return order;
    }

    @PostMapping("/order3")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "接收list")
    public String batchOrder(@RequestBody List<Order> orders)  {
        //造出异常
        System.out.println(1/0);
        return orders.toString();
    }

    @PostMapping("/test2")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "接收属性列表")
    public String test2(String id,String name) throws InterruptedException {
        Thread.sleep(3000);
        return MessageFormat.format("accept on msg id={0}，name={1}",id,name);
    }


    @PostMapping("/pay")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "测试超时")
    public String pay(@RequestBody Payment payment) throws InterruptedException {
        Thread.sleep(3000);
        return payment.toString();
    }

}

