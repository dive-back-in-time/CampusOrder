package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dates.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate date : dates) {
            //查询日期对应的营业额
            //select sum(amount) from orders where status = 5 and date(order_time) = #{date};
            Double result = orderMapper.sumByDate(date);
            result = result == null ? 0.0 : result;
            turnoverList.add(result);
        }


        String join = StringUtils.join(dates, ",");
        String joinAmount = StringUtils.join(turnoverList, ",");
        return TurnoverReportVO.builder()
                .dateList(join)
                .turnoverList(joinAmount)
                .build();


    }

    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dates.add(begin);
        }

        List<Integer> userNewList = new ArrayList<>();
        List<Integer> userList = new ArrayList<>();
        for (LocalDate date : dates) {
            //查询每天新增的用户数
            //查询总用户数
            Integer resultNew = userMapper.addByDate(date);
            Integer resultSum = userMapper.sumAll(date);
            userNewList.add(resultNew);
            userList.add(resultSum);
        }


        return UserReportVO.builder()
                .totalUserList(StringUtils.join(userList, ","))
                .newUserList(StringUtils.join(userNewList, ","))
                .dateList(StringUtils.join(dates, ","))
                .build();
    }

    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dates = new ArrayList<>();
        dates.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dates.add(begin);
        }

        List<Integer> orderCompleted = new ArrayList<>();//每天所有完成订单列表
        List<Integer> orders = new ArrayList<>();//每天所有订单

        for (LocalDate date : dates) {
            //计算每天订单
            Integer result = orderMapper.sumNumByDate(date);

            //每天有效订单
            Integer resultCom = orderMapper.sumNumComByDate(date);

            orderCompleted.add(resultCom);
            orders.add(result);

        }

        //计算日期区间内的有效订单
        Integer allResultCom = 0;
        for (Integer order : orderCompleted) {
            allResultCom += order;
        }

        //计算日期区间内的总订单
        Integer allResult = 0;
        for (Integer order : orders) {
            allResult += order;
        }


        //计算日期区间内的订单完成率
        Double radio = (double)allResultCom / allResult;

        return OrderReportVO.builder()
                .orderCountList(StringUtils.join(orders, ","))
                .validOrderCountList(StringUtils.join(orderCompleted, ","))
                .totalOrderCount(allResult)
                .validOrderCount(allResultCom)
                .dateList(StringUtils.join(dates, ","))
                .orderCompletionRate(radio)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        //查询对应的商品名称与销量
        List<GoodsSalesDTO> lists = orderMapper.selectTop10(begin, end);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (GoodsSalesDTO list : lists) {
            nameList.add(list.getName());
            numberList.add(list.getNumber());
        }


        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }
}
