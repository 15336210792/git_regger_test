package com.zzh.controller;

import com.zzh.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${regger.path}")
    private String bassPath;

    /**
     *文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){//此处的命名必须为file
        String originalFilename = file.getOriginalFilename();//先得到文件的整体名称的 //得到文件的后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));//从“.”开始截取后边的

        //使用uuid重新生成文件名称，防止文件名称重复
        String s = UUID.randomUUID().toString() + substring;

        //创建一个目录对象
        File file1 = new File(bassPath);
        //判断当前目录是否存在
        if (!file1.exists()){
            file1.mkdir();
        }

        //将文件转存到指定位置
        try {
            file.transferTo(new File((bassPath+s)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(s);
    }

    /**
     * 文件下载（展示在窗口）
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流读取数据
            FileInputStream fileInputStream = new FileInputStream(new File(bassPath+name));

            //输出流，通过输出流文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
