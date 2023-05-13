package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.common.R;
import com.zzh.dto.SetmealDto;
import com.zzh.entity.Category;
import com.zzh.entity.Setmeal;
import com.zzh.service.CategoryService;
import com.zzh.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto setmeal的包装类，继承了setmeal，并扩展了方法
     * @return  返回R类型的消息
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

    /**
     * 分页查询套餐列表
     * @param page  当前在那一页
     * @param pageSize  分页大小是多少
     * @param name  查询条件是啥
     * @return  返回那一页要展示的内容
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> page1 = new Page(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name != null,Setmeal::getName,name);
        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);

        setmealService.page(page1,setmealLambdaQueryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(page1,dtoPage,"records");
        List<Setmeal> records = page1.getRecords();

       List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //得到id
            Long categoryId = item.getCategoryId();
            //根据分类id查询对象
            Category category = categoryService.getById(categoryId);
            if (category != null){
                //分类名称
                String categoryname = category.getName();
                setmealDto.setCategoryName(categoryname);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    //修改
    // 回显
    @GetMapping("/{id}")
    public R<Setmeal> update1(@PathVariable Long id){
        log.info("返回的数据+{}",id);
        Setmeal byId = setmealService.getById(id);
        if (byId != null){
            return R.success(byId);
        }
        return R.error("异常异常");
    }

    /**
     * 删除套餐（单个/批量）
     * @param ids 前端返回的要删除的id
     * @return  是否删除成功的字符串
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
       // log.info("ids{}",ids);
        boolean b = setmealService.removeWithSetmal(ids);
        if (b){
            return R.success("删除成功");
        }
        return R.error("异常，删除失败，请重试");
    }

    /**
     * 在前端页面中展示
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Long categoryId, Integer status){
        //log.info("前端传回来的值是：{}+++++++{}",categoryId,status);//前端传回1413386191767674881+++++++1

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(categoryId!= null,Setmeal::getCategoryId,categoryId);
        setmealLambdaQueryWrapper.eq(status!= null,Setmeal::getStatus,status);

        setmealLambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        if (list!= null){
            return R.success(list);
        }
        return R.error("异常，请重试");
    }

}
