package com.zzh.dto;

import com.zzh.entity.Setmeal;
import com.zzh.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
