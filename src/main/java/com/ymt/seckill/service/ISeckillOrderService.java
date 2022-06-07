package com.ymt.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymt.seckill.pojo.SeckillOrder;
import com.ymt.seckill.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    /**
     * 功能描述：获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId：成功；  -1：秒杀失败；  0：排队中；
     */
    Long getResult(User user, Long goodsId);
}
