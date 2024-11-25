package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和对应的口味
     * @param dishDTO
     */
    void saveWithFlavour(DishDTO dishDTO);

    PageResult<DishVO> queryPage(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);
}
