package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 添加订单
     * @param order
     */
    public void insert(Orders order);

    /**
     * 分页查询
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 更新用户订单
     * @param orders
     */
    void update(Orders orders);


    /**
     * 根据订单编号查询订单
     * @param outTradeNo
     * @return
     */
    @Select("select * from orders where number = #{outTradeNo}")
    Orders getByNumber(String outTradeNo);

    /**
     * 根据状态查询订单数量
     * @param toBeConfirmed
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countStatus(Integer toBeConfirmed);

    /**
     *
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * 根据日期查询营业总额
     * @param date
     * @return
     */
    @Select("select sum(amount) from orders where status = 5 and date(order_time) = #{date}")
    Double sumByDate(LocalDate date);

    /**
     * 计算每天订单数量
     * @param date
     * @return
     */
    @Select("select count(*) from orders where date(order_time) = #{date}")
    Integer sumNumByDate(LocalDate date);

    /**
     * 计算每天有效订单数量
     * @param date
     * @return
     */
    @Select("select count(*) from orders where date(order_time) = #{date} and status = 5")
    Integer sumNumComByDate(LocalDate date);

    /**
     * 查询商品销量
     * select from orders or left join order_detail od on or.id = od.order_id
     * where date(order_time) >= begin and date(order_time) <= end
     * group by or.name
     * limit 10
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> selectTop10(LocalDate begin, LocalDate end);
}
