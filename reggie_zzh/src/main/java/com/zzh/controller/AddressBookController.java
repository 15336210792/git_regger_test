package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzh.common.BaseContext;
import com.zzh.common.R;
import com.zzh.entity.AddressBook;
import com.zzh.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 添加地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook) {
        //根据线程，获取当前操作人的id
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("{}", addressBook);
        boolean save = addressBookService.save(addressBook);
        if (save) {
            return R.success("保存成功");
        }

        return R.error("异常，请重试");
    }

    /**
     * 分页查询地址显示到页面上
     * 需要根据线程查询到当前的用户id，用当前的用户id去查询对应的地址
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        log.info("{}", addressBook);
        addressBook.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        addressBookLambdaQueryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        //添加排序条件
        addressBookLambdaQueryWrapper.orderByAsc(AddressBook::getCreateTime);

        //查询
        List<AddressBook> list = addressBookService.list(addressBookLambdaQueryWrapper);

        return R.success(list);
    }

    /**
     * 根据qiandua传回来的参数id查询并且回显数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<?> get(@PathVariable Long id) {
        //log.info("前端传参是：{}",id);//1417414526093082626

        AddressBook byId = addressBookService.getById(id);
        if (byId != null) {
            return R.success(byId);
        }

        return R.error("异常，请重试");
    }

    /**
     * 根据id去修改地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {

        //boolean save = addressBookService.save(addressBook);
        boolean b = addressBookService.updateById(addressBook);
        if (b) {
            return R.success("修改成功");
        }
        return R.error("修改失败");
    }

    /**
     * 根据ids进行逻辑删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
        //log.info("ids={}",ids);

        boolean b = addressBookService.removeById(ids);
        if (b) {
            return R.success("删除成功");
        }
        return R.error("异常，请重试");
    }


    @PutMapping("/default")
    public R<String> setDefault(@RequestBody AddressBook addressBook) {
        //log.info("传回来的信息是：{}",addressBook);
        //传回来的信息是：AddressBook{id=1417414926166769666, userId=null, consignee='null', sex='null', phone='null',
        // provinceCode='null', provinceName='null', cityCode='null', cityName='null', districtCode='null', districtName='null',
        // detail='null', label='null', isDefault=null, createTime=null, updateTime=null, createUser=null, updateUser=null, isDeleted=null}

        //根据id进行设置isDefault 但是前提是需要将其他的默认值设置为不默认（0） 在service中扩展方法使用事务完成同失败，同成功

        R<String> stringR = addressBookService.setIsDefault(addressBook.getId());

        return stringR;
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){

        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getIsDefault,1);

        AddressBook one = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if (one != null){
            return R.success(one);
        }


        return R.error("异常，没有默认地址");
    }
}
