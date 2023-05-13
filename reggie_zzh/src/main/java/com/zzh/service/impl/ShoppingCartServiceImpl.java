package com.zzh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.entity.ShoppingCart;
import com.zzh.mapper.ShoppingCartMapper;
import com.zzh.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
