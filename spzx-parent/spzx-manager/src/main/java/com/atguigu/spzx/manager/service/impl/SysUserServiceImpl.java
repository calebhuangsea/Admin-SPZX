package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public LoginVo login(LoginDto loginDto) {
        // 获取输入的验证码和验证码存储在redis里的key
        String captcha = loginDto.getCaptcha();
        String key = loginDto.getCodeKey();
        // 根据获取到的redis里的key查询redis里面存储的验证码
        String redisCode = redisTemplate.opsForValue().get("user:validate" + key);
        System.out.println(redisCode + " : " + captcha);
        // 比较输入的验证码
        if (StrUtil.isEmpty(redisCode) || !StrUtil.equalsIgnoreCase(captcha, redisCode)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        // 如果验证码正确，删除缓存的验证码
        redisTemplate.delete("user:validate" + key);
        // 根据用户名查询数据库表 sys_user
        String userName = loginDto.getUserName();
        SysUser sysUser = sysUserMapper.selectByUserName(userName);
        // 如果根据用户名查不到对应信息，用户不存在，返回错误信息
        if (sysUser == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 获取输入的密码，比较输入的密码和数据库密码是否一致
        String database_password = sysUser.getPassword();
        // 加密输入的密码
        String input_password = DigestUtils.md5DigestAsHex(loginDto.getPassword().getBytes());
        // 如果密码一致，登录成功，如果密码不一致登录失败
        if (!input_password.equals(database_password)) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
        }
        // 登录成功，使用UUID生成用户唯一标识token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        // 登录成功用户信息放到redis里，把对象转成JSON字符串
        redisTemplate.opsForValue().set("user:login" + token,
                JSON.toJSONString(sysUser), 7, TimeUnit.DAYS);
        // 返回loginvo对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        return loginVo;
    }

    @Override
    public void logout(String token) {
        // 根据请求头中的token把redis里对应的数据删除
        redisTemplate.delete("user:login" + token);
    }

    @Override
    public SysUser getUserInfo(String token) {
        // 返回一个Json的字符串，需要转成一个Json对象
        String userJson = redisTemplate.opsForValue().get("user:login" + token);
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }


}
