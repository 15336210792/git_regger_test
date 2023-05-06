package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.common.R;
import com.zzh.ebtity.Category;
import com.zzh.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoeyService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        boolean b = categoeyService.save(category);
        if (b) {
            return R.success("新增分类成功");
        }
        return R.error("新增分类失败");
    }

    /**
     * 分页
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        //分页查询
        categoeyService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除  自定义删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("根据id删除，{}", ids);
        // boolean b = categoeyService.removeById(ids);
        categoeyService.remove(ids);
        /*if (b){
            return R.success("已经删除成功");
        }*/

        /*return R.error("异常，请重新操作");*/
        return R.success("已经删除成功");
    }

    /**
     * 根据id修改
     *
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {

        boolean b = categoeyService.updateById(category);
        if (b) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    /**
     *  根据条件查询分类
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());

        //添加排序条件
        queryWrapper.orderByDesc(Category::getSort).orderByAsc(Category::getUpdateTime);

        List<Category> list = categoeyService.list(queryWrapper);

        return R.success(list);
    }
}
