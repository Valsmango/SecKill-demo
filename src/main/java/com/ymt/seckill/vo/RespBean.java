package com.ymt.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long ajax_1;
    private String message;
    private Object obj;

    /**
     * 功能描述：成功返回结果
     * @return
     */
    public static RespBean success() {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMessage(), null);
    }

    public static RespBean success(Object obj) {
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),  RespBeanEnum.SUCCESS.getMessage(), obj);
    }

    /**
     * 功能描述：失败返回结果
     * @return
     */
    public static RespBean error() {
        return new RespBean(RespBeanEnum.ERROR.getCode(),  RespBeanEnum.ERROR.getMessage(), null);
    }
    public static RespBean error(RespBeanEnum respBeanEnum) {     // 和utils类相似，方法得是静态的，因为到处都要调用
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMessage(),null);
    }
}
