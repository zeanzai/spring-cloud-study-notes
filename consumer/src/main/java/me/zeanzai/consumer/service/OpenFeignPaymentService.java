package me.zeanzai.consumer.service;

import me.zeanzai.consumer.model.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "provider")
public interface OpenFeignPaymentService {

    /**
     * 付款的服务
     * @param payment
     * @return
     */
    @PostMapping("/openfeign/provider/pay")
    String pay(Payment payment);
}
