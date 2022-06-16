package com.ymt.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymt.seckill.pojo.Order;
import com.ymt.seckill.pojo.User;
import com.ymt.seckill.vo.GoodsVo;
import com.ymt.seckill.vo.OrderDetailVo;

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

    /**
     * 功能描述：订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Long orderId);

    /**
     * 功能描述：获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    String createPath(User user, Long goodsId);

    /**
     * 功能描述：校验秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    boolean checkPath(User user, Long goodsId, String path);

    /**
     * 功能描述：校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    Boolean checkCaptcha(User user, Long goodsId, String captcha);
}
