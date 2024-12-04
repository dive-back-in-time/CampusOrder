package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理业务异常：地址簿为空或者购物车数据为空
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        List<ShoppingCart> carts = shoppingCartMapper.selectByUserId(BaseContext.getCurrentId());
        if (address == null || carts == null) {
            throw new RuntimeException("地址栏或者购物车为空");
        }


        //向订单表插入一条数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, order);
        order.setOrderTime(LocalDateTime.now());
        order.setPayStatus(Orders.UN_PAID);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setPhone(address.getPhone());
        order.setConsignee(address.getConsignee());
        order.setUserId(BaseContext.getCurrentId());
        orderMapper.insert(order);

        //向订单明细表插入多条数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : carts) {
            OrderDetail detail = new OrderDetail();
            detail.setDishFlavor(cart.getDishFlavor());
            detail.setDishId(cart.getDishId());
            detail.setSetmealId(cart.getSetmealId());
            detail.setOrderId(order.getId());
            orderDetails.add(detail);
        }
        System.out.println(orderDetails.toArray().toString());
        orderDetailMapper.insertBatch(orderDetails);


        //下单成功之后清空购物车
        shoppingCartMapper.deleteById(carts.get(0));

        //封装VO返回结果
        OrderSubmitVO ordersSubmitVO = OrderSubmitVO.builder()
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();


        return ordersSubmitVO;
    }
}
