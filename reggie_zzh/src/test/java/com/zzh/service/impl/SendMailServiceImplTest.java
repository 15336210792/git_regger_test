package com.zzh.service.impl;

import com.zzh.service.SendMailService;
import com.zzh.uitls.GetMyCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SendMailServiceImplTest {

    @Autowired
    private SendMailService sendMailService;
    @Test
    public void test01(){

        String s = sendMailService.SendMail("15335353535", "2805753067@qq.com", 5);
        System.out.println("ssss:"+s);
    }

}
