package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.common.R;
import com.zzh.entity.Orders;
import com.zzh.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        //log.info("订单数据：{}",orders);

        ordersService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 下单后的订单查看
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(Integer page, Integer pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByAsc(Orders::getOrderTime);

        Page<Orders> page1 = ordersService.page(ordersPage, ordersLambdaQueryWrapper);
        if (page1 != null) {
            return R.success(page1);
        }


        return R.error("请下单后查看，谢谢");
    }
}