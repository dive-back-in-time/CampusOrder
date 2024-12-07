package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper

public interface ReportMapper {
    /**
     * 根据日期查询营业总额
     * @param date
     * @return
     */
    @Select("select sum(amount) from orders where status = 5 and date(order_time) = #{date}")
    Double sumByDate(LocalDate date);
}
