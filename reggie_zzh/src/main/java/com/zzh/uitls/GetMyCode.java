package com.zzh.uitls;

import org.springframework.stereotype.Component;

@Component
public class GetMyCode {

    private String[] pach = {"000000", "00000", "0000", "000", "00", "0", ""};

    public String getCode(String tele) {

        //得到hash
        int i = tele.hashCode();
        //固定一个数值
        int i1 = 20230501;
        //i 和 i1 数值进行异或
        long i2 = i ^ i1;
        //得到当前的时间
        long i3 = System.currentTimeMillis();
        //i2 和 i3 数值进行异或
        long i4 = i3 ^ i2;
        //取余，即得到后6位
        long l = i4 % 1000000;

        long l1 = l < 0 ? -l : l;
        String s = l1 + "";

        int length = s.length();

        String s1 = pach[length] + s;

        return s1;
    }

}
