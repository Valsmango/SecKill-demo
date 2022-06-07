package com.ymt.seckill.controller;


import com.ymt.seckill.pojo.User;
//import com.ymt.seckill.rabbitmq.MQSender;
import com.ymt.seckill.vo.RespBean;
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
 * @since 2022-03-30
 */
@Controller
@RequestMapping("/user")
public class UserController {

//    @Autowired
//    private MQSender mqSender;

    /**
     * 功能描述：用户信息（用来测试）
     * @param user
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user) {
        return RespBean.success(user);
    }

//    /**
//     * 功能描述：测试发送RabbitMQ消息
//     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq() {
//        mqSender.send("Hello, the first time to use RabbitMQ!");
//    }
//
//    /**
//     * 功能描述：Fanout模式
//     */
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01() {
//        mqSender.send01("Hello, testing Fanout!");
//    }
//
//    /**
//     * 功能描述：Direct模式
//     */
//    @RequestMapping("/mq/direct01")
//    @ResponseBody
//    public void mq02() {
//        mqSender.send02("Hello, testing Direct - Red!");
//    }
//    @RequestMapping("/mq/direct02")
//    @ResponseBody
//    public void mq03() {
//        mqSender.send03("Hello, testing Direct - Green!");
//    }
//
//    /**
//     * 功能描述：Topic模式
//     */
//    @RequestMapping("/mq/topic01")
//    @ResponseBody
//    public void mq04() {
//        mqSender.send04("Hello, testing Topic - queue.red.message");
//    }
//    @RequestMapping("/mq/topic02")
//    @ResponseBody
//    public void mq05() {
//        mqSender.send05("Hello, testing Topic - both.queue.red.message");
//    }
//
//    /**
//     * 功能描述：Headers模式
//     */
//    @RequestMapping("/mq/headers01")
//    @ResponseBody
//    public void mq06() {
//        mqSender.send06("Hello, testing Headers - both");
//    }
//    @RequestMapping("/mq/headers02")
//    @ResponseBody
//    public void mq07() {
//        mqSender.send07("Hello, testing Headers - only QUEUE01");
//    }
}
