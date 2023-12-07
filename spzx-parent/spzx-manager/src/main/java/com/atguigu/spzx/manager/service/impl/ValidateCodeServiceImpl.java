package com.atguigu.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    RedisTemplate<String, String> redisTemplate; // 用redis暂存验证码
    @Override
    public ValidateCodeVo generateValidateCode() {
        // 通过hutool里面的CircleCaptcha工具生成图片验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 4, 2);
        String codeValue = circleCaptcha.getCode(); // CircleCaptcha产生的4位数code
        String imageBase64 = circleCaptcha.getImageBase64(); // 返回图片验证码，base64编码
        // 把验证码储存到redis里面，设置redis的key，redis的value：验证码值， 设置过期时间5分钟
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set("user:validate"+key, codeValue, 5, TimeUnit.MINUTES);
        // 返回ValidateCodeVo对象
        ValidateCodeVo validateCodeVo = new ValidateCodeVo();
        validateCodeVo.setCodeKey(key); // redis里存储数据的key
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64); // 用户看到的验证码图片
        return validateCodeVo;
    }
}
