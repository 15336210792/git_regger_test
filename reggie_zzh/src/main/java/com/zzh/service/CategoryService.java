package com.zzh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.ebtity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
