package com.zzh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.dto.DishDto;
import com.zzh.ebtity.Dish;
import com.zzh.ebtity.DishFlavor;
import com.zzh.mapper.DishMapper;
import com.zzh.service.DishFlavorService;
import com.zzh.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    public Boolean saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到dish表中
        this.save(dishDto);

        //得到菜品的id
        Long dishDtoId = dishDto.getId();
        //得到菜品口味对应的list
        List<DishFlavor> flavors = dishDto.getFlavors();
        //循环将菜品id传入（stream流的方式）
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味到菜品口味表 dish_flavor  saveBatch 批量保存
        boolean b = dishFlavorService.saveBatch(flavors);
        return b;

    }

    /**
     *  根据id查询菜品和菜品口味
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品的基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        //对象拷贝
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品的口味信息
        LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> list = dishFlavorService.list(flavorLambdaQueryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }


    @Transactional
    public Boolean updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        boolean b = dishFlavorService.saveBatch(flavors);
        return b;


    }
}
















