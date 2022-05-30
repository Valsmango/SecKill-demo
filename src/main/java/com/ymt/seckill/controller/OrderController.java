package com.ymt.seckill.controller;


import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IOrderService;
import com.ymt.seckill.vo.OrderDetailVo;
import com.ymt.seckill.vo.RespBean;
import com.ymt.seckill.vo.RespBeanEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ymt
 * @since 2022-04-05
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

//    // 用于打log
//    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    /**
     * 功能描述：订单详情
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId){
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);    // 那么这里直接传输detail会存在精度丢失吗？
    }

}
