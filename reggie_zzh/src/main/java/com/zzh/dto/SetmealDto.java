package com.zzh.dto;

import com.zzh.ebtity.Setmeal;
import com.zzh.ebtity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;

    private String categoryName;

}
