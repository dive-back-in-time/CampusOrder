package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;

@Mapper
public interface UserMapper {

    @Select("select * from user where openid = #{openid}")
    public User getByOpenId(String openid);

    /**
     * 增加用户
     * 要增加自增键，因此要重新开一个xml
     * @param user
     */
    void insert(User user);

    /**
     * 查新对应天数的用户量
     * @param date
     * @return
     */
    @Select("select count(id) from user where date(create_time) = #{date}")
    Integer addByDate(LocalDate date);

    /**
     * 查询对应天数之前的总用户数
     * @param date
     * @return
     */
    @Select("select count(id) from user where date(create_time) < #{date}")
    Integer sumAll(LocalDate date);
}
