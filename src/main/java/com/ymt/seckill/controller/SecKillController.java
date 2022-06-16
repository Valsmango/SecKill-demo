package com.ymt.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.tools.json.JSONUtil;
import com.wf.captcha.ArithmeticCaptcha;
import com.ymt.seckill.config.AccessLimit;
import com.ymt.seckill.exception.GlobalException;
import com.ymt.seckill.pojo.*;
import com.ymt.seckill.rabbitmq.MQSenderSeckill;
import com.ymt.seckill.service.IGoodsService;
import com.ymt.seckill.service.IOrderService;
import com.ymt.seckill.service.ISeckillGoodsService;
import com.ymt.seckill.service.ISeckillOrderService;
import com.ymt.seckill.utils.JsonUtil;
import com.ymt.seckill.vo.GoodsVo;
import com.ymt.seckill.vo.RespBean;
import com.ymt.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：秒杀
 */
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MQSenderSeckill mqSender;
    @Autowired
    private RedisScript<Long> script;

    private Map<Long, Boolean> emptyStockMap = new HashMap<>(); // 加Redis预减库存是为了降低直接去访问MySQL，而这里用Map是为了将信息存到内存中，减少大量去Redis中访问

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
////    @RequestMapping("/doSeckill")
////    public String doSeckill(Model model, User user, Long goodsId) {
////        if (user == null) {
////            return "login";
////        }
////        model.addAttribute("user", user);
////        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
////        // 判断库存
////        if (goodsVo.getStockCount() < 1) {
////            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
////            return "secKillFail";
////        }
////        // 判断是否重复抢购
////        SeckillOrder seckillOrder =
////                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
////        if (seckillOrder != null) {
////            model.addAttribute("errmsg", RespBeanEnum.REPEAT_ERROR.getMessage());
////            return "secKillFail";
////        }
////        // 抢购成功，下订单
////        Order order = orderService.seckill(user, goodsVo);
////        model.addAttribute("order", order);
////        model.addAttribute("goods", goodsVo);
////        return "orderDetail";
////    }
//
//    // 页面静态化：
//    @RequestMapping(value="/doSeckill", method = RequestMethod.POST)
//    @ResponseBody
//    public RespBean doSeckill(User user, Long goodsId) {
//        if (user == null) {
//            return RespBean.error(RespBeanEnum.SESSION_ERROR);
//        }
//        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
//        // 判断库存
//        if (goodsVo.getStockCount() < 1) {
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//        // 判断是否重复抢购
////        SeckillOrder seckillOrder =
////                seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
//        // 在OrderServiceImpl中，下单的时候同时将信息存入了Redis缓存中，因此，这里不再需要去SQL数据库中查找，直接去Redis中查找
//        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
//        if (seckillOrder != null) {
//            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
//        }
//        // 抢购成功，下订单
//        Order order = orderService.seckill(user, goodsVo);
////        return RespBean.success(order); // 这里传入的order的Id是正确的，但是因为前端js会造成Long类型的精度丢失，所以在这里和order
//        return RespBean.success(order.getId().toString()); // 这里传入的order的Id是正确的！
//        // 思考：为什么这里用toString可以？用String.valueOf()呢？
//        // 原因：Long下的toString被重写过；Object类下的toString是打印其HashCode
//    }

    // 进行接口优化
    @RequestMapping(value="/{path}/doSeckill", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSeckill(@PathVariable String path, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 校验秒杀接口地址：将path顺便拿过来进行判断，判断秒杀地址是否正确
        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        // 先把库存加载到Redis：由重写的InitializingBean接口方法afterPropertiesSet实现
        // 再预减库存：先在内存中的map表中查，再去redis中找（即：通过内存标记减少Redis的访问）
        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);    // 思考：分布式下能不能用？其次，map不是线程安全的，多线程的情况下可以用吗？
        }
//        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);      // redis的递增递减是一个原子性操作？这里直接上的原子性操作？
        // 这里，尝试不采用原子操作，而采用lua脚本+redisTemplate来执行
        Long stock = (Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:" + goodsId), Collections.EMPTY_LIST);
        if (stock < 0) {
            emptyStockMap.put(goodsId, true);
//            valueOperations.increment("seckillGoods:" + goodsId);       // 原子性操作？为什么这个不需要用脚本呢？ 为了解决lua后的超卖问题，这一句要删掉？
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 抢购成功，下订单(基于RabbitMQ，再MQReceiverSeckill中完成具体的下单逻辑)
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);     // 0 代表排队中，也就是不立即响应是否下单成功，先忽悠住
    }

    /**
     * 功能描述：获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId：成功；  -1：秒杀失败；  0：排队中；
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId.toString());
    }
    /**
     * 系统初始化，把商品库存数量加载到Redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(),false);
        });

    }

    /**
     * 功能描述：获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        // 在校验验证码的基础上，还需要进行计数器方式的限流，也就是限制访问次数，例如5秒内访问5次（这里的业务逻辑也就是注解@AccessLimit()的基础）
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String uri = request.getRequestURI();
//        captcha = "0";  // 这一句是为了方便测试，让captcha的正确值为0，不管前端显示的验证码实际结果为多少，只要输入0就算对。。。
//        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
//        if (count == null) {
//            valueOperations.set(uri + ":" + user.getId(), 1, 5, TimeUnit.SECONDS);
//        } else if (count < 5) {
//            valueOperations.increment(uri + ":" + user.getId());
//        } else {
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
//        }
        // 校验验证码
        Boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }

    /**
     * 功能描述：验证码接口
     * @param user
     * @param goodsId
     * @param response
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if (user == null || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        // 设置请求头为输出图片的类型：https://gitee.com/ele-admin/EasyCaptcha?_from=gitee_search
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 生成验证码，将结果放在Redis里面
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败：", e.getMessage());
        }
    }

}
