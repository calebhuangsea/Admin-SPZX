package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import com.atguigu.spzx.utils.AuthContextUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户接口")
@RestController
@RequestMapping(value = "/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ValidateCodeService validateCodeService;

    // 生成图片验证码
    @GetMapping(value = "/generateValidateCode")
    public Result<ValidateCodeVo> generateValidateCode() {
        ValidateCodeVo validateCodeVo = validateCodeService.generateValidateCode();
        return Result.build(validateCodeVo, ResultCodeEnum.SUCCESS);
    }

    /**
     * 用户登录 使用Post请求
     * @RequestBody 将前端传递的json数据转换为对象 loginDTO
     * @param loginDto 前端传的表单数据
     * @return Result
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.build(loginVo, ResultCodeEnum.SUCCESS);
    }

    @GetMapping("logout")
    public Result logout(@RequestHeader(name = "token") String token) {
        sysUserService.logout(token);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    @GetMapping(value = "/getUserInfo")
    public Result getUserInfo() {
        // 用户信息返回
        return Result.build(AuthContextUtil.get(), ResultCodeEnum.SUCCESS);
    }

//    @GetMapping(value = "/getUserInfo")
//    public Result getUserInfo(@RequestHeader(name = "token") String token) {
//        // 从请求头获取token
//        // 方法一HttpServletRequest request, request.getHeader("token");
//        // 根据token查询redis获取用户信息
//        SysUser sysUser = sysUserService.getUserInfo(token);
//        // 用户信息返回
//        return Result.build(sysUser, ResultCodeEnum.SUCCESS);
//    }
}
