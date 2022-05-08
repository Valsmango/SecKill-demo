package com.ymt.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymt.seckill.pojo.Order;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
public interface IOrderService extends IService<Order> {
    /**
     * 功能描述：秒杀
     * @param user
     * @param goodsVo
     * @return
     */
    Order seckill(User user, GoodsVo goodsVo);
}
