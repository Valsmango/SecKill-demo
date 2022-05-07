package com.ymt.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ymt.seckill.pojo.Goods;
import com.ymt.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * 功能描述：获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 功能描述：获取商品详情
     * @param goodsId
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
