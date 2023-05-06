package com.zzh.common;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * 自定义元数据对象处理器
 */
@Configuration
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {

    /**
     * 插入操作时，执行该操作
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    /**
     * 更新操作时，执行该操作
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("修改自动填充线程id为：{}",id);

        metaObject.setValue("updateTime",LocalDateTime.now());
       metaObject.setValue("updateUser",BaseContext.getCurrentId());

    }
}
