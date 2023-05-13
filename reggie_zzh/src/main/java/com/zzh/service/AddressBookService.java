package com.zzh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzh.common.R;
import com.zzh.entity.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    //根据id进行设置isDefault 但是前提是需要将其他的默认值设置为不默认（0） 在service中扩展方法使用事务完成同失败，同成功
    R<String> setIsDefault(Long ids);
}
