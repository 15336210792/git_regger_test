package com.zzh.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzh.ebtity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
