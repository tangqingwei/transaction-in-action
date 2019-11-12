package com.qige.transaction.order.controller;

import com.qige.transaction.order.Dto.OrderDto;
import com.qige.transaction.order.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/t", method = RequestMethod.GET)
    public String test(@RequestParam("c") Integer c){
        System.out.println("------------>" + c);

        OrderDto obj = new OrderDto();
        obj.setUserId(1);
        obj.setProductId(1);
        obj.setPaymentMoney(new BigDecimal(100));
        obj.setProductCount(1);
        obj.setOrderNo(UUID.randomUUID().toString());
        //扣减2个积分
        obj.setDecreaseScore(2);
        obj.setCouponReceiveId(c);
        this.orderService.createOrder(obj);
        return "OK";
    }
}
