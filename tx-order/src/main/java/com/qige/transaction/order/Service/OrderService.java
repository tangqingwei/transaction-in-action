package com.qige.transaction.order.Service;

import com.qige.transaction.coupon.api.CouponApi;
import com.qige.transaction.coupon.dto.CouponDTO;
import com.qige.transaction.inventory.api.InventoryApi;
import com.qige.transaction.inventory.dto.InventoryDTO;
import com.qige.transaction.order.Dto.OrderDto;
import com.qige.transaction.order.entity.Order;
import com.qige.transaction.order.enums.OrderStatusEnum;
import com.qige.transaction.order.mapper.OrderMapper;
import com.qige.transaction.score.api.ScoreApi;
import com.qige.transaction.score.dto.ScoreDTO;
import org.dromara.hmily.annotation.Hmily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private InventoryApi inventoryApi;
    @Autowired
    private ScoreApi scoreApi;
    @Autowired
    private CouponApi couponApi;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);


    @Hmily(confirmMethod ="confirmMethod",cancelMethod = "cancelMethod")
    public void createOrder(OrderDto obj){
        Order order = new Order();
        BeanUtils.copyProperties(obj, order);
        order.setStatus(OrderStatusEnum.CONFIRM.getCode());
        this.orderMapper.insertSelective(order);

        //减库存
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setUserId(obj.getUserId());
        inventoryDTO.setProductId(obj.getProductId());
        inventoryDTO.setCount(obj.getProductCount());
        inventoryDTO.setOrderNo(obj.getOrderNo());
        this.inventoryApi.decrease(inventoryDTO);

        //减积分
        ScoreDTO scoreDTO = new ScoreDTO();
        scoreDTO.setUserId(obj.getUserId());
        scoreDTO.setOrderNo(obj.getOrderNo());
        scoreDTO.setScore(obj.getDecreaseScore());
        this.scoreApi.decrease(scoreDTO);

        //扣优惠券
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setUserId(obj.getUserId());
        couponDTO.setOrderNo(obj.getOrderNo());
        couponDTO.setCouponId(obj.getCouponReceiveId());
        this.couponApi.decrease(couponDTO);
    }


    public void cancelMethod(OrderDto obj){
        LOGGER.info("-------进入订单的cancel-------");
        Order order = new Order();
        order.setOrderNo(obj.getOrderNo());
        order = this.orderMapper.selectOne(order);
        order.setStatus(OrderStatusEnum.PAY_FAIL.getCode());
        this.orderMapper.updateByPrimaryKeySelective(order);
    }

    public void confirmMethod(OrderDto obj){
        LOGGER.info("-------进入订单的confirm-------");
        Order order = new Order();
        order.setOrderNo(obj.getOrderNo());
        order = this.orderMapper.selectOne(order);
        order.setStatus(OrderStatusEnum.NOT_PAY.getCode());
        this.orderMapper.updateByPrimaryKeySelective(order);
    }
}
