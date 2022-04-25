package com.ymt.seckill.controller;

import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IGoodsService;
import com.ymt.seckill.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;

    /**
     * 功能描述：跳转商品页面
     * @param model
     * @param ticket
     * @return
     */
//    @RequestMapping("/toList")
//    // 分布式Session（非分布式/方法一）：toList(HttpSession session, Model model, @CookieValue("userTicket") String ticket)
//    public String toList(HttpServletRequest req, HttpServletResponse resp, Model model, @CookieValue("userTicket") String ticket) {
//        if(StringUtils.isEmpty(ticket)) {
//            return "login";
//        }
////        // 分布式Session（非分布式/方法一）
////        User user = (User)session.getAttribute(ticket);
////        if (null == user) {
////            return "login";
////        }
//        // 当采用分布式Session（方法二），就不能再通过session来获取用户信息；需要通过Redis获取，而根据层层调用，还需要增加Service层的业务逻辑
//        User user = userService.getUserByCookie(ticket, req, resp);
//        if (null == user) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//        return "goodsList";
//    }
//    // 登录功能优化部分，相当于将跳转分为两部分；
//    // 先通过WebConfig + UserArgumentResolver来进行分布式Session获取用户信息，再将用户信息直接传入这里的toList方法形参内
    @RequestMapping("/toList")
    public String toList(Model model, User user) {
        model.addAttribute("user", user);
        model.addAttribute("goodsList", goodsService.findGoodsVo());
        return "goodsList";
    }
}
