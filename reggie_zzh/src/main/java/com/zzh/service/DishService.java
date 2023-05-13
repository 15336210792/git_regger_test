package com.zzh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.dto.DishDto;
import com.zzh.entity.Dish;

public interface DishService extends IService<Dish> {


    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：dish、dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品和菜品口味
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新菜品的口味
    Boolean updateWithFlavor(DishDto dishDto);
}
