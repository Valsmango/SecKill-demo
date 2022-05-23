package com.ymt.seckill.controller;


import com.ymt.seckill.pojo.User;
import com.ymt.seckill.vo.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ymt
 * @since 2022-03-30
 */
@Controller
@RequestMapping("/user")
public class UserController {

    /**
     * 功能描述：用户信息（用来测试）
     * @param user
     * @return
     */

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success(user);
    }

}
