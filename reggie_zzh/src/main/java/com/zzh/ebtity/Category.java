package com.zzh.ebtity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer Type;

    private String name;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)//创建时间
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)//创建人
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)//修改时间
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)//修改人
    private Long updateUser;

    /*//是否删除
    private Integer isDeleted;*/
}


