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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Autowired
    private RedisTemplate redisTemplate;

    // 导入logger是因为下面的public String doSeckill(Model model, User user, Long goodsId)写错成了goodId，
    // 所以没办法从goodsDetail.html获得goods.id (<input type="hidden" name="goodsId" th:value="${goods.id}"/>)
    // 所以doSeckill(Model model, User user, Long goodsId)的参数来自于上一个调用它的界面
    // 也就是： 从GoodsController.toDetail(...) 方法内部开始：
    //          --> 通过( model.addAttribute("goods", goodsVo); )传参给前端界面 -->
    //          "goodsDetail" 前端界面
    //          --> 通过( <form id="seckillForm" method="post" action="/seckill/doSeckill"> )
    //              以及( <input type="hidden" name="goodsId" th:value="${goods.id}"/> ) 控制下一个界面跳转与传参给后端 -->
    //          SecKillController.doSeckill(Model model, User user, Long goodsId) 通过参数列表接收前端参数

//    // 用于打log
//    private static Logger logger = LoggerFactory.getLogger(SecKillController.class);
    /**
     * 功能描述：秒杀
     * @param user
     * @param goodsId
     * @return
     */
//    @RequestMapping("/doSeckill")
//    public String doSeckill(Model model, User user, Long goodsId) {
//        if (user == null) {
//            return "login";
//        }
//        model.addAttribute("user", user);
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        // 判断库存
//        if (goodsVo.getStockCount() < 1) {
//            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return "secKillFail";
//        }
//        // 判断是否重复抢购
//        SeckillOrder seckillOrder =
//                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
//        if (seckillOrder != null) {
//            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
//            return "secKillFail";
//        }
//        // 抢购成功，下订单
//        Order order = orderService.seckill(user, goodsVo);
//        model.addAttribute("order", order);
//        model.addAttribute("goods", goodsVo);
//        return "orderDetail";
//    }
    // 页面静态化：
    @RequestMapping(value="/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 判断是否重复抢购
//        SeckillOrder seckillOrder =
//                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        // 在OrderServiceImpl中，下单的时候同时将信息存入了Redis缓存中，因此，这里不再需要去SQL数据库中查找，直接去Redis中查找
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        // 抢购成功，下订单
        Order order = orderService.seckill(user, goodsVo);
//        return RespBean.success(order); // 这里传入的order的Id是正确的，但是因为前端js会造成Long类型的精度丢失，所以在这里和order
        return RespBean.success(order.getId().toString()); // 这里传入的order的Id是正确的！
    }
}
