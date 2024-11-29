package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
