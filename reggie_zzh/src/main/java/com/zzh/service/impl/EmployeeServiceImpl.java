package com.zzh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzh.entity.Employee;
import com.zzh.mapper.EmployeeMapper;
import com.zzh.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService {
}
