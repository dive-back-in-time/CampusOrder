package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * *  ?")
    public void processTimeOutOrder() {


        log.info("定时处理超时订单：{}" , LocalDateTime.now());

        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);

        //select * from orders where status = ? and order_time <(当前时间 - 15mins)
        List<Orders> orders = orderMapper.getByStatusOrderTime(Orders.PENDING_PAYMENT, orderTime);

        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单超时，自动取消");
                orderMapper.update(order);
            }
        }

    }

    /**
     * 处理一直在派送中的订单
     */
    @Scheduled(cron = "0 0 1 * *  ?")
    public void processDelivery() {
        log.info("处理派送中的订单");
        LocalDateTime orderTime = LocalDateTime.now().plusHours(-1);


        //select * from orders where status = ?
        List<Orders> orders = orderMapper.getByStatusOrderTime(Orders.DELIVERY_IN_PROGRESS, orderTime);
        if (orders != null && orders.size() > 0) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("订单超时，自动取消");
                orderMapper.update(order);
            }
        }

    }




}
