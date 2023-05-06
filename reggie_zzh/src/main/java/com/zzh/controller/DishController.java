package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.common.R;
import com.zzh.dto.DishDto;
import com.zzh.ebtity.Category;
import com.zzh.ebtity.Dish;
import com.zzh.service.CategoryService;
import com.zzh.service.DishFlavorService;
import com.zzh.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增保存，保存基本信息和口味信息（利用自己扩展的方法操作）
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        //log.info(dishDto.toString());
        Boolean aBoolean = dishService.saveWithFlavor(dishDto);
        if (aBoolean) {
            return R.success("新增菜品成功");
        }

        return R.error("新增异常，请重试");
    }


    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //排序
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);

        Page<Dish> page1 = dishService.page(pageInfo, queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 修改数据，回显数据(根据id查两张表）
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {

        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);

        return R.success(byIdWithFlavor);
    }

    /**
     * 修改数据，保存基本信息和口味信息（利用自己扩展的方法操作）
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("dishDto.toString()=====" + dishDto.toString());
        Boolean aBoolean = dishService.updateWithFlavor(dishDto);
        if (aBoolean) {
            return R.success("修改成功");
        }
        return R.error("异常，请重新操作");
    }

    /**
     * 根据id修改售卖状态
     *
     * @param id
     * @param ids
     * @return
     */
    @PostMapping("/status/{id}")
    public R<String> updateStatus(@PathVariable Integer id, Long ids) {

        //log.info("id++++++++"+id);
        //log.info("ids++++++++"+ids);
        Dish dish = new Dish();
        dish.setStatus(id);
        dish.setId(ids);
        boolean b = dishService.updateById(dish);
        if (b) {
            return R.success("修改成功");
        }
        return R.error("异常，请重新尝试");
        //return null;
    }

    /**
     * 根据id逻辑删除   有异常，需要待处理
     *
     * @param ids
     * @return
     */
    @Transactional
    @DeleteMapping
    public R<String> delete(Long[] ids) {

        //log.info("delete+id ======"+ids);
       /* boolean b = dishFlavorService.removeById(ids);
        boolean b1 = dishService.removeById(ids);*/
        String msg = null;
        Integer code = null;

        for (int i = 0; i < ids.length; i++) {
            /*//System.out.println(ids[i]);
            boolean b;
            b = dishFlavorService.removeById(ids[i]);
            if (b) {
                b = dishFlavorService.removeById(ids[i]);
            }
            if (b) {
                code = 1;
                msg = "删除成功";
            }
            msg = "异常，请重试";*/
            /*dishFlavorService.removeById(ids[i]);*/
            dishFlavorService.removeById(ids[i]);
            dishService.removeById(ids[i]);

        }
        return R.success("删除成功");
    }

    /**
     * 根据id查询对应菜品的数据，用于展示在套餐新增中展示数据
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();

        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //添加查询起售状态的菜品
        dishLambdaQueryWrapper.eq(Dish::getStatus,1);
        //添加排序
        dishLambdaQueryWrapper.orderByDesc(Dish::getSort).orderByAsc(Dish::getUpdateTime);


        List<Dish> list = dishService.list(dishLambdaQueryWrapper);

        return R.success(list);
    }
}

















