package com.sky.mapper;


import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 提取对应的购物车
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 修改购物车物件对应数量
     * @param cart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id} ")
    void updateCount(ShoppingCart cart);

    /**
     * 添加购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (user_id, dish_id, setmeal_id, name, image, amount, number, dish_flavor, create_time) "
            +
            "values " +
            "(#{userId}, #{dishId}, #{setmealId}, #{name}, #{image}, #{amount}, #{number}, #{dishFlavor}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> selectByUserId(Long userId);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void clean(Long userId);

    /**
     * 删除购物车对应记录
     * @param result
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(ShoppingCart result);
}
