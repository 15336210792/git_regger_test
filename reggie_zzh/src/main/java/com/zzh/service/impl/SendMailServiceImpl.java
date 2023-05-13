package com.zzh.service.impl;

import com.zzh.service.SendMailService;
import com.zzh.uitls.GetMyCode;
import com.zzh.uitls.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailServiceImpl implements SendMailService{
    private static String from="2805753067@qq.com";    //发件人
    private static String to;      //收件人
    private static String subject;           //邮件主题
    private static String text;                       //设置正文内容
    private static String code;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private GetMyCode getMyCode;

    @Autowired
    private ValidateCodeUtils validateCodeUtils;


    @Override
    public String SendMail(String tele,String ToEmail,Integer x) {

        to = ToEmail;
        code = validateCodeUtils.generateValidateCode4String(x);
        //code = getMyCode.getCode(tele);

        subject = "登录验证码";
        text = "您的验证码是："+code;
        System.out.println("text:" +text);
        System.out.println("code:" +code);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);

        return code;
    }
}
