package com.zzh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzh.common.R;
import com.zzh.ebtity.Employee;
import com.zzh.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request  将登录成功的员工对象的id存到session中，表示登录成功，想获取当前登录用户就可以获取出来
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /**
         * 1、将页面提交的密码password进行md5加密处理
         * 2、根据页面提交的用户名username查询数据库
         * 3、如果没有查询到则返回登录失败结果
         * 4、密码对比，如果对比不一致则返回登录失败结果
         * 5、查看员工状态，如果为禁用状态，则返回员工已禁用结果
         * 6、登录成功，精员工id存入session并返回登录成功结果
         */
        // 1、将页面提交的密码password进行md5加密处理
        /*log.error("热部署成功");*/
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if (emp == null){
            return R.error("没查到。登录失败");
        }

        //4、密码对比，如果对比不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误，登录失败");
        }

        //5、查看员工状态，如果为禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("账号已经禁用");
        }

        //6、登录成功，精员工id存入session并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> Logout(HttpServletRequest request){
       //清理Session中保存的登录的员工信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
       //设置初始密码，需要进行md5加密处理，
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

       /* //设置创建时间为系统时间
        employee.setCreateTime(LocalDateTime.now());
        //设置更新时间
        employee.setUpdateTime(LocalDateTime.now());

        //设置创建人(先转为string再转为Long）
        Long empId = Long.valueOf(request.getSession().getAttribute("employee").toString());
        employee.setCreateUser(empId);
        //设置最后的修改创建人
        employee.setUpdateUser(empId);*/

        //log.info("新增员工信息：{}",employee.toString());
        //保存到数据库
        employeeService.save(employee);
        //会出异常，Duplicate entry 'zhangsan' for key 'idx_username';（可以使用try——catch/使用异常处理器）
        //try——catch不建议,会弄很多个
        return R.success("新增成功");
    }

    /**
     * 分页+条件查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //log.info("page = {},pagesize = {},name = {}",page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,lambdaQueryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        //log.info("根据id修改员工信息");

        long id = Thread.currentThread().getId();
        //log.info("修改中的线程的id{}",id);
        /*//设置修改时间
        employee.setUpdateTime(LocalDateTime.now());
        //设置修改人
        Long aLong = Long.valueOf(request.getSession().getAttribute("employee").toString());
        employee.setUpdateUser(aLong);*/

        //执行
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    //修改
    /**
     * 修改回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        //log.info("返回的id是：{}",id);
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到相关数据");
    }
}
