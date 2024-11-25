package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    public void insertBanch(List<DishFlavor> flavors);

    /**
     * 根据菜品id批量删除口味数据
     * @param ids
     */
    void deleteById(List<Long> ids);
}
