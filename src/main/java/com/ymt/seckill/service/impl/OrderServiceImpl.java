package com.ymt.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ymt.seckill.controller.OrderController;
import com.ymt.seckill.exception.GlobalException;
import com.ymt.seckill.mapper.OrderMapper;
import com.ymt.seckill.pojo.Order;
import com.ymt.seckill.pojo.SeckillGoods;
import com.ymt.seckill.pojo.SeckillOrder;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IGoodsService;
import com.ymt.seckill.service.IOrderService;
import com.ymt.seckill.service.ISeckillGoodsService;
import com.ymt.seckill.service.ISeckillOrderService;
import com.ymt.seckill.vo.GoodsVo;
import com.ymt.seckill.vo.OrderDetailVo;
import com.ymt.seckill.vo.RespBeanEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 用于打log
    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 功能描述：秒杀
     */
    @Override
    @Transactional
    public Order seckill(User user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // 秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        // seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
        // seckillGoodsService.updateById(seckillGoods);
        // 这里的业务逻辑是有问题的，因为没有考虑并发的情况，并且读、改、写没有原子性
        boolean result = seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count - 1").eq("goods_id",
                goodsVo.getId()).gt("stock_count", 0));
//        if (!result) {
//            return null;
//        }
        if (seckillGoods.getStockCount() < 1) {
            // 判断是否还有库存
            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
            return null;
        }
        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrderService.save(seckillOrder);
        // 使用Redis将秒杀订单信息缓存起来，提高响应速度
        valueOperations.set("order:" + user.getId() + ":" + goodsVo.getId(), seckillOrder);
        return order;
    }

    /**
     * 功能描述：订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;
    }

}
