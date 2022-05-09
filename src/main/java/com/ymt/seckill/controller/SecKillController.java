package com.ymt.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ymt.seckill.pojo.Order;
import com.ymt.seckill.pojo.SeckillGoods;
import com.ymt.seckill.pojo.SeckillOrder;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IGoodsService;
import com.ymt.seckill.service.IOrderService;
import com.ymt.seckill.service.ISeckillGoodsService;
import com.ymt.seckill.service.ISeckillOrderService;
import com.ymt.seckill.vo.GoodsVo;
import com.ymt.seckill.vo.RespBean;
import com.ymt.seckill.vo.RespBeanEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 功能描述：秒杀
 */
@Controller
@RequestMapping("/seckill")
public class SecKillController {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;

    // 导入logger是因为下面的public String doSeckill(Model model, User user, Long goodsId)写错成了goodId，
    // 所以没办法从goodsDetail.html获得goods.id (<input type="hidden" name="goodsId" th:value="${goods.id}"/>)
    // 所以doSeckill(Model model, User user, Long goodsId)的参数来自于上一个调用它的界面
    // 也就是： 从GoodsController.toDetail(...) 方法内部开始：
    //          --> 通过( model.addAttribute("goods", goodsVo); )传参给前端界面 -->
    //          "goodsDetail" 前端界面
    //          --> 通过( <form id="seckillForm" method="post" action="/seckill/doSeckill"> )
    //              以及( <input type="hidden" name="goodsId" th:value="${goods.id}"/> ) 控制下一个界面跳转与传参给后端 -->
    //          SecKillController.doSeckill(Model model, User user, Long goodsId) 通过参数列表接收前端参数
//    private static Logger logger = LoggerFactory.getLogger(SecKillController.class);
    /**
     * 功能描述：秒杀
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping("/doSeckill")
    public String doSeckill(Model model, User user, Long goodsId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "secKillFail";
        }
        // 判断是否重复抢购
        SeckillOrder seckillOrder =
                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
            return "secKillFail";
        }
        // 抢购成功，下订单
        Order order = orderService.seckill(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }
}
