package com.zzh.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 *
 */
@ControllerAdvice(annotations = {RestController.class, Conditional.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
       // log.error("有异常：{}",ex.getMessage());
        //处理（截取判断是否是这个类）
        if (ex.getMessage().contains("Duplicate entry")){
            String[] s = ex.getMessage().split(" ");
            String msg =  s[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误，请重试");
    }

    /**
     * 处理自定义异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
       // log.error("有异常：{}",ex.getMessage());
        return R.error(ex.getMessage());
    }
}
