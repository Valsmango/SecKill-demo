package com.ymt.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.vo.LoginVo;
import com.ymt.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ymt
 * @since 2022-03-30
 */
public interface IUserService extends IService<User> {
    /**
     *
     * 功能描述：登录
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest req, HttpServletResponse resp);

    /**
     * 功能描述：根据Cookie获取用户    分布式Session（方法二）
     * @param userTicket
     * @return
     */
    User getUserByCookie(String userTicket, HttpServletRequest req, HttpServletResponse resp);

    /**
     * 功能描述：更新密码
     * @param userTicket
     * @param password
     * @param req
     * @param resp
     * @return
     */
    RespBean updatePassword(String userTicket, String password, HttpServletRequest req, HttpServletResponse resp);
}
