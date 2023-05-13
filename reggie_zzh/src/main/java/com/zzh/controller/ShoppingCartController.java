package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzh.common.BaseContext;
import com.zzh.common.R;
import com.zzh.entity.ShoppingCart;
import com.zzh.service.SendMailService;
import com.zzh.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 展示购物车里的数据
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {

        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        if (list != null) {
            return R.success(list);
        }
        return R.success(list);
    }

    /**
     * 加入购物车功能
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        //log.info("传回来的数据是这+{}", shoppingCart);
        //传回来的数据是这+
        // ShoppingCart(id=null,
        // name=毛氏红烧肉,
        // userId=null,
        // dishId=1397850140982161409,
        // setmealId=null,
        // dishFlavor=常温,不要蒜,微辣,
        // number=null,
        // amount=68,
        // image=0a3b3288-3446-4420-bbff-f263d0c02d8e.jpg,
        // createTime=null)
        //设置当前用户id，指定是那个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, currentId);
        //查询购物车，是否有这个菜品
        if (dishId != null) {
            //添加到购物车的是菜品
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, dishId);
        }

        if (dishId == null) {
            //添加到购物车的是套餐
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart one = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);
        //如果有，就给菜品的数量+1即可
        if (one != null) {
            //更新操作
            Integer number = one.getNumber();
            one.setNumber(number + 1);
            shoppingCartService.updateById(one);
        }
        //如果没有，就保存就行
        if (one == null) {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        boolean b = shoppingCartService.remove(shoppingCartLambdaQueryWrapper);
        if (b){
            return R.success("删除成功");
        }

        return R.error("删除失败");
    }
}
