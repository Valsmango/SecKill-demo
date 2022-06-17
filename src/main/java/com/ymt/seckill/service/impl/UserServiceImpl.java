package com.ymt.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ymt.seckill.exception.GlobalException;
import com.ymt.seckill.mapper.UserMapper;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IUserService;
import com.ymt.seckill.utils.CookieUtil;
import com.ymt.seckill.utils.MD5Util;
import com.ymt.seckill.utils.UUIDUtil;
import com.ymt.seckill.utils.ValidationUtil;
import com.ymt.seckill.vo.LoginVo;
import com.ymt.seckill.vo.RespBean;
import com.ymt.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ymt
 * @since 2022-03-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /**
     * 功能描述：登录
     * @param loginVo
     * @return
     */
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest req, HttpServletResponse resp) {
        // 写登录逻辑
        // 1，首先校验是否输入为空/不是手机号格式
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 2，根据手机号获取数据库中的用户（看是否找得到）
        User user = userMapper.selectById(mobile);  // MyBatis Plus的通用接口，所以在UserMapper中就不用重复写了
        if (null == user) {
//            // 可以改为抛出全局异常，所以不再直接返回RespBean对象了，而交给exception类下的GlobalExceptionHandler来进行异常处理
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 3，判断密码是否正确
        if (!MD5Util.fromPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
//            // 可以改为抛出全局异常，所以不再直接返回RespBean对象了，而交给exception类下的GlobalExceptionHandler来进行异常处理
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        // 可以改为抛出全局异常，所以不再直接返回RespBean对象了，而交给exception类下的GlobalExceptionHandler来进行异常处理
//        // 改为分布式Session（方法二）后，不需要将用户信息放在Session里面了，而是直接将用户信息放在Redis里面，所以下面部分代码需要修改
//        // 生成Cookie，步骤为：生成cookie之后，将用户信息放在Session里面
//        String ticket = UUIDUtil.uuid();
//        req.getSession().setAttribute(ticket, user);
//        CookieUtil.setCookie(req, resp, "userTicket", ticket);
//        // 改为分布式Session（方法二）
        String ticket = UUIDUtil.uuid();    //UUID 让分布式系统中的所有元素，都能有唯一的辨识信息，而不需要通过中央控制端来做辨识信息的指定
        redisTemplate.opsForValue().set("user:" + ticket, user);
        CookieUtil.setCookie(req, resp, "userTicket", ticket);

        return RespBean.success(ticket);
    }

    /**
     * 功能描述：根据Cookie获取用户    分布式Session（方法二）
     * @param userTicket
     * @return
     */
    @Override
    public User getUserByCookie(String userTicket, HttpServletRequest req, HttpServletResponse resp) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User)redisTemplate.opsForValue().get("user:" + userTicket);
        // 引入HttpServletRequest req, HttpServletResponse resp就是为了当用户不为空时，重新设置一个Cookie????但是为什么啊
        if (user != null) {
            CookieUtil.setCookie(req, resp, "userTicket", userTicket);
        }
        return user;
    }

    /**
     * 功能描述：更新密码
     * @param userTicket
     * @param password
     * @param req
     * @param resp
     * @return
     */
    @Override
    public RespBean updatePassword(String userTicket, String password,
                                   HttpServletRequest req, HttpServletResponse resp) {
        User user = getUserByCookie(userTicket, req, resp);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password, user.getSalt()));
        int result = userMapper.updateById(user);
        if (1 == result) {
            // 删除Redis
            redisTemplate.delete("user:" + userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
