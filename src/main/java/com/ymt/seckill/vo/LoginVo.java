package com.ymt.seckill.vo;

import com.ymt.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotNull;

/**
 * 用于接收登录参数；和Login.html中Ajax传入参数要相对应
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
