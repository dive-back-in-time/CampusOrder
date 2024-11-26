package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    public void insertBanch(List<DishFlavor> flavors);

    /**
     * 根据菜品id批量删除口味数据
     * @param ids
     */
    void deleteById(List<Long> ids);

    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getById(Long id);

    /**
     * 修改菜品口味
     * @param flavor
     */
    void update(DishFlavor flavor);
}
