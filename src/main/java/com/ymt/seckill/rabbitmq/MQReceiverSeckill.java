package com.ymt.seckill.rabbitmq;

import com.ymt.seckill.pojo.SeckillMessage;
import com.ymt.seckill.pojo.SeckillOrder;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IGoodsService;
import com.ymt.seckill.service.IOrderService;
import com.ymt.seckill.utils.JsonUtil;
import com.ymt.seckill.vo.GoodsVo;
import com.ymt.seckill.vo.RespBean;
import com.ymt.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 接收到消息后，进行下单操作
 */
@Service
@Slf4j
public class MQReceiverSeckill {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    /**
     * 下单操作
     * @param message
     */
    @RabbitListener(queues = "seckillQueue")
    public void receiveSeckillMessage(String message) {
        log.info("seckillQueue接收消息：" + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;     // 在SeckillController中已经判断了，那这里为什么要再次对库存进行判断呢？？？ 因为Redis和MySQL可能不一致？
        }
        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder != null) {
            return;
        }
        // 下单
        orderService.seckill(user, goodsVo);
    }

}
