package com.zzh.controller;




import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzh.common.R;
import com.zzh.entity.User;
import com.zzh.service.SendMailService;
import com.zzh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private UserService userService;

    /**
     * 根据邮箱获取验证码
     *
     * @param user    将前端的返回来邮箱进行封装
     * @param session 将生成的验证码封装到session中
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //log.info("邮箱是：{}",user);
        //获取手机号
        String phone = user.getPhone();
        //log.info("qq邮箱是；{}",phone);
        if (StringUtils.isNotEmpty(phone)) {
            //生成验证码(直接发送了，发送在实现方法中写着）并且得到验证码
            String code = sendMailService.SendMail(phone, phone, 6);//根据phone生成验证码，发送到"phone",生成x位

            //将验证码存到session中
            System.out.println("存到session中的phone："+phone);
            System.out.println("存到session中的code："+code);
            session.setAttribute(phone, code);

            return R.success("验证码发送成功");
        }
        return R.error("异常，请重新操作");
    }


    /**
     * 登录
     * @param map    用户填写的手机号和验证码
     * @param session   上一过程中得到验证码存的
     * @return
     */
    @PostMapping("/login")
    public R<User> logIn(@RequestBody Map map, HttpSession session) {
        /*log.info("传回来的值：{}",map);    传回来的值：{phone=2805753067@qq.com, code=617805}
        log.info("对比验证码");*/
        //获取手机号
        String phone = map.get("phone").toString();  //{phone=2805753067@qq.com, code=617805}


        System.out.println("phone:"+phone);
        //获取输入的验证码
        String code = map.get("code").toString();


        System.out.println("code:"+code);
        //获取session中保存的验证码
        Object sessionCode = session.getAttribute(phone);
        System.out.println("sessionCode:"+sessionCode);
        //对比，是否成功
        if (sessionCode != null && sessionCode.equals(code)) {
            //如果登录成功，确定是否第一次登录，不是则要直接进行注册

            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(userLambdaQueryWrapper);
            if (user == null) {
                //为空，所以当前手机号为首次登录，需要直接注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);

                userService.save(user);

            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }


        return R.error("登录异常");
    }



}
