package com.ymt.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymt.seckill.pojo.Goods;
import com.ymt.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 功能描述：获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 功能描述：获取商品详情页
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodId);
}
