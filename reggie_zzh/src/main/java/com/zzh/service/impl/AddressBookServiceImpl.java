package com.zzh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.common.R;
import com.zzh.entity.AddressBook;
import com.zzh.mapper.AddressBookMapper;
import com.zzh.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

    @Autowired
    private AddressBookService addressBookService;

    //根据id进行设置isDefault 但是前提是需要将其他的默认值设置为不默认（0）
    // 在service中扩展方法使用事务完成同失败，同成功
    @Transactional
    @Override
    public R<String> setIsDefault(Long ids) {

        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper1.eq(AddressBook::getIsDefault, 1);

        AddressBook one1 = addressBookService.getOne(addressBookLambdaQueryWrapper1);
        //log.info("查出到有其他的默认值：{}",one);
        if (one1 != null) {
            one1.setIsDefault(0);
            addressBookService.updateById(one1);
        }


        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper2.eq(ids!=null,AddressBook::getId,ids);

        int count = count(addressBookLambdaQueryWrapper2);
        if (count == 1){
            AddressBook one2 = addressBookService.getOne(addressBookLambdaQueryWrapper2);

            one2.setIsDefault(1);
            addressBookService.updateById(one2);
            return R.success("修改成功");
        }



       return R.error("异常，修改失败");
    }
}
