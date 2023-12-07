# 商品甄选功能流程详解

---

## 后台系统

### 用户登陆
- 登陆图形验证码
  - 通过hutool厘米的CircleCaptcha工具生成图形验证码
  - 把验证码存储到redis中
- 登陆信息验证
  - 比较用户验证码与图形验证码的值（删除redis里的验证码）
  - 数据库里查询用户信息，比较密码
- 获取用户信息
  - 从AuthContextUtil的ThreadLocal里拿
  - 通过请求头用户的token从redis里取用户对象
- 登出
  - 删除redis里的用户数据
- 涉及到的类
  - Controller：IndexController
  - Service: SysUserService, ValidateCodeService
  - Mapper: SysUSerMapper


---

## 配置

- Knife4jConfig
  - 配置了API接口文档信息
- AuthContextUtil
  - 把用户信息存到ThreadLocal里，帮助实现登陆校验拦截器
- WebMvcConfiguration
  - 添加跨域请求的规则，解决跨区请求的问题
  - 登陆校验的拦截器，除了登陆和获取验证码意外以外所有的借口都需要被拦截检测登陆用户信息