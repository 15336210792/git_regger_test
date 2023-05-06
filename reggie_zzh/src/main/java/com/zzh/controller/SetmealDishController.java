package com.zzh.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.common.R;
import com.zzh.ebtity.SetmealDish;
import com.zzh.dto.SetmealDto;
import com.zzh.service.SetmealDishService;
import com.zzh.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealDishController {

    @Autowired
    private SetmealService setmealService;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        //log.info("套餐信息：{}"+setmealDto);
        Boolean b = setmealService.saveWithDish(setmealDto);
        if (b){
            return R.success("添加成功");
        }
        return R.error("异常，请重试");
    }
}
