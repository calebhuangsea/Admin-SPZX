package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface SysUserService {

    // 用户登录
    LoginVo login(LoginDto loginDto);

    SysUser getUserInfo(String token);

    void logout(String token);
}
