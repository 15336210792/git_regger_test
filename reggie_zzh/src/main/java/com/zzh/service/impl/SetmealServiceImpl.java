package com.zzh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.common.CustomException;
import com.zzh.dto.SetmealDto;
import com.zzh.entity.Setmeal;
import com.zzh.entity.SetmealDish;
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

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐，同时要保存套餐和菜品的关联表
     *
     * @param setmealDto
     * @return
     */
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

    /**
     * 删除套餐，删除两张表，套餐表和菜品关联表
     *
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public boolean removeWithSetmal(List<Long> ids) {
        //1、查询是否在售状态，如在售就不可删除，抛出一个自定义异常即可
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //因为ids是数组，要用in
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("有在售商品，不能删除"); //this =====setmealDishService
        }
        //2、如果可以删除，先删除菜品表，在删除套餐表（要加事务，同成功，同失败）
        //删除套餐
        this.removeByIds(ids);

        //删除菜品
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        boolean remove = setmealDishService.remove(setmealDishLambdaQueryWrapper);
        return remove;
    }
}
