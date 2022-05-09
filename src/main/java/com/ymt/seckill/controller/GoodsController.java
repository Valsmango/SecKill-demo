package com.ymt.seckill.controller;

import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IGoodsService;
import com.ymt.seckill.service.IUserService;
import com.ymt.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

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
     * @param //ticket
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

    /**
     * 功能描述：跳转商品详情页
     * @param goodsId
     * @return
     */
    @RequestMapping("/toDetail/{goodsId}")
    public String toDetai(Model model, User user, @PathVariable Long goodsId) {
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        int seckillStatus = 0;  // 秒杀状态
        int remainSeconds = 0;  // 秒杀倒计时
        if (nowDate.before(startDate)) {
            // 秒杀还未开始
            remainSeconds = ( (int) ( (startDate.getTime() - nowDate.getTime()) / 1000) );
        }else if (nowDate.after(endDate)) {
            // 秒杀已结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {
            // 秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("secKillStatus", seckillStatus);
        model.addAttribute("goods", goodsVo);
        return "goodsDetail";
    }
}
