package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Transactional
    @Override
    public void saveWithFlavour(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //向菜品表插入一条数据
        dishMapper.insert(dish);

        //获取生成的主键值
        Long dishId = dish.getId();


        //向口味表插入1条或多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBanch(flavors);
        }

    }

    @Override
    public PageResult<DishVO> queryPage(DishPageQueryDTO dishPageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> pages = dishMapper.queryPage(dishPageQueryDTO);
        long total = pages.getTotal();
        List<DishVO> records = pages.getResult();
        return new PageResult<>(total, records);
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断菜品是否能够删除
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //是否被套餐引用
        List<Long> setmealId = setMealDishMapper.getIdById(ids);
        if (setmealId != null && setmealId.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        for (Long id : ids) {
            dishMapper.deleteById(id);
        }

        dishFlavorMapper.deleteById(ids);

    }

    @Override
    @Transactional
    public DishVO getById(Long id) {
        //获取菜品信息，没有口味
        DishVO dishVO = dishMapper.getByIdFlavor(id);

        //根据菜品id获取口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getById(id);

        //根据菜品categoryId获取分类名称
        String categoryName = categoryMapper.getById(dishVO.getCategoryId());

        dishVO.setCategoryName(categoryName);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        //修改菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //修改对应的口味信息
//        List<DishFlavor> flavors = dishDTO.getFlavors();
//        for (DishFlavor flavor : flavors) {
//            dishFlavorMapper.update(flavor);
//        }
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getById(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        List<Dish> dishes = dishMapper.list(dish);
        return dishes;
    }
}
