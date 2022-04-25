package com.ymt.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 这是启动类，配置好了controller、service（及service下impl）和mapper的package，以及pojo包后，就要修改启动类
 * 备注：
 *      1， pojo == entity；实体类，作用是和数据库产生映射，也就是数据库中的一张表对应一个pojo文件夹中的一个类
 */


/**
 * MapperScan注解的含义：这个包下面的接口类，在编译之后都会生成相应的实现类（也就是只需要在pojo里面生成接口即可，不用再一个一个建class）
 */
@SpringBootApplication
@MapperScan("com.ymt.seckill.mapper")
public class SeckillDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }

}
