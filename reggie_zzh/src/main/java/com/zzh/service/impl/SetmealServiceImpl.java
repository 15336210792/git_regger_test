package com.zzh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.common.R;
import com.zzh.dto.SetmealDto;
import com.zzh.ebtity.Setmeal;
import com.zzh.ebtity.SetmealDish;
import com.zzh.mapper.SetmealMapper;
import com.zzh.service.SetmealDishService;
import com.zzh.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    //新增套餐，同时要保存套餐和菜品的关联表
    @Transactional
    public Boolean saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，执行setmeal,执行insert操作
        this.save(setmealDto);
        //保存套餐和菜品有关系的信息，操作setmeal_dish 执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((itme) -> {
            itme.setSetmealId(setmealDto.getId());
            return itme;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息，操作setmeal_dish执行insert操作
        return setmealDishService.saveBatch(setmealDishes);
    }
}
