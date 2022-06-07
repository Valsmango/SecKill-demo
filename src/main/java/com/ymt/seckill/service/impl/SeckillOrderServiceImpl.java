package com.ymt.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ymt.seckill.mapper.SeckillOrderMapper;
import com.ymt.seckill.pojo.SeckillMessage;
import com.ymt.seckill.pojo.SeckillOrder;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 功能描述：获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId：成功；  -1：秒杀失败；  0：排队中；
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>().eq("user_id",
                user.getId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        } else if (redisTemplate.hasKey("isSticjEmpty:" + goodsId)) {
            return -1L;
        } else {
            return 0L;
        }
    }
}
