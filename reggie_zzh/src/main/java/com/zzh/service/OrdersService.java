package com.zzh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.entity.Orders;

public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}

