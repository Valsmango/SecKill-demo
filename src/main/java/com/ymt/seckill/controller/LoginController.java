package com.ymt.seckill.controller;

import com.ymt.seckill.service.IUserService;
import com.ymt.seckill.vo.LoginVo;
import com.ymt.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录
 */

// 这里页面跳转不能用@RestController，只能用Controller，因为如果RestController = Controller + RequestBody
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * 功能描述：跳转登陆页面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest req, HttpServletResponse resp) {
//        // 为什么可以直接用log？因为用了Lombok + Slf4j注解
//        log.info("{}",loginVo);   // 用于测试是否成功接收到了前端输入的参数，同时校验MD5加密是否一致
        return userService.doLogin(loginVo, req, resp);
    }

}
