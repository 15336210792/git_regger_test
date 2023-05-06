package com.zzh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.dto.SetmealDto;
import com.zzh.ebtity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时要保存套餐和菜品的关联表
    public Boolean  saveWithDish(SetmealDto setmealDto);
}
