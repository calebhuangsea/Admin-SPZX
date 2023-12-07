package com.atguigu.spzx.common.exception;

import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice // Controller的统一增强器，对Controller进行统一的操作（和AOP类似）
public class GlobalExceptionHandler {

    //全局异常处理
    @ExceptionHandler(Exception.class) // 异常处理器，捕捉抛出的异常
    @ResponseBody // 让Result以Json的格式返回
    public Result error() {
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(GuiguException.class)
    @ResponseBody
    public Result error(GuiguException e) {
        return Result.build(null, e.getResultCodeEnum());
    }
}
