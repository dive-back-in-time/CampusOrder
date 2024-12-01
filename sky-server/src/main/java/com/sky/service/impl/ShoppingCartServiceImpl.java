package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //查询购物车是否存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //存在则更新数量，判断是菜品还是套餐
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            //存在则更改数量
            Integer count = cart.getNumber() + 1;
            cart.setNumber(count);
            shoppingCartMapper.updateCount(cart);
        } else {
            //判断是套餐还是菜品
            if (shoppingCart.getSetmealId() != null) {
                //套餐，直接添加套餐
                Long setmealId = shoppingCart.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setNumber(1);
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.insert(shoppingCart);

            } else {
                //菜品，判断有无口味
                Long dishId = shoppingCart.getDishId();
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setNumber(1);
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCartMapper.insert(shoppingCart);

            }
        }
    }

    @Override
    public List<ShoppingCart> selectByUserId() {
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> lists = shoppingCartMapper.selectByUserId(userId);
        return lists;
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.clean(userId);
    }

    @Override
    public void cleanSub(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, cart);
        cart.setUserId(userId);
        List<ShoppingCart> lists = shoppingCartMapper.list(cart);
        ShoppingCart result = lists.get(0);
        //如果菜品数量大于1则减一，否则直接删除对应菜品
        Integer num = result.getNumber();
        if (num > 1) {
            result.setNumber(num - 1);
            shoppingCartMapper.updateCount(result);
        } else {
            shoppingCartMapper.deleteById(result);
        }

    }
}
