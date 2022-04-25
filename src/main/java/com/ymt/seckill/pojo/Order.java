package com.ymt.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 收货地址ID
     */
    private Long deliveryAddrId;

    /**
     * 冗余过来的商品名称
     */
    private String goodsName;

    /**
     * 下单的商品数量
     */
    private Integer goodsCount;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 下单渠道：1代表PC，2代表Android，3代表IOS
     */
    private Integer orderChannel;

    /**
     * 订单状态：0代表新建未支付，1代表已支付，2代表已发货，3代表已收货，4代表退款，5代表已完成
     */
    private Integer status;

    /**
     * 订单的创建时间
     */
    private Date createDate;

    /**
     * 订单的支付时间
     */
    private Date payDate;


}
