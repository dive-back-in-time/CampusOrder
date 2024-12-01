package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 根据用户ID选择相应购物车
     * @return
     */
    List<ShoppingCart> selectByUserId();

    void clean();

    /**
     * 清除购物车中的一条数据
     * @param shoppingCartDTO
     */
    void cleanSub(ShoppingCartDTO shoppingCartDTO);
}
