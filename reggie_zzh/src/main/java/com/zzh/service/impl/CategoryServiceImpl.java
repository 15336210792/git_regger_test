package com.zzh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.common.CustomException;
import com.zzh.ebtity.Category;
import com.zzh.ebtity.Dish;
import com.zzh.ebtity.Setmeal;
import com.zzh.mapper.CategoryMapper;
import com.zzh.service.CategoryService;
import com.zzh.service.DishService;
import com.zzh.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    //自定义删除操作
    //需要查询菜品和套餐中是否关联了分类
    @Override
    public void remove(Long id) {
        //添加查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        //如果当前分类关联了一个菜品，就不能删除，需抛出异常
        if (count1 > 0) {
            //抛出异常
            throw  new CustomException("当前分类下关联有菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            //抛出异常
            throw  new CustomException("当前分类下关联有套餐，不能删除");
        }

        //正常分类
        super.removeById(id);
    }
}
