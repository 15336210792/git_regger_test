package com.zzh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.common.R;
import com.zzh.dto.SetmealDto;
import com.zzh.ebtity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐，同时要保存套餐和菜品的关联表
    public Boolean  saveWithDish(SetmealDto setmealDto);

    //删除套餐，删除两张表，套餐表和菜品关联表
    public boolean removeWithSetmal(List<Long> ids);
}
