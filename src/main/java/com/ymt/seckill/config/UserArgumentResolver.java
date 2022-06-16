package com.ymt.seckill.config;

import com.ymt.seckill.pojo.User;
import com.ymt.seckill.service.IUserService;
import com.ymt.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {   // 做一层条件判断
        Class<?> clazz = parameter.getParameterType();
        return clazz == User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        // 这里的业务逻辑就是之前GoodsController之中，获得User的信息（基于Cookie、分布式Session）的业务逻辑
//        HttpServletRequest req = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse resp = webRequest.getNativeResponse(HttpServletResponse.class);
//        String ticket = CookieUtil.getCookieValue(req, "userTicket");
//        if (StringUtils.isEmpty(ticket)) {
//            return null;
//        }
//        return userService.getUserByCookie(ticket, req, resp);
        // 在增加了拦截器之后，获取当前用户已经在AccessLimitInterceptor中二次实现了，同时，将获取的用户存入了当前线程的ThreadLocal中
        // 所以这里的冗余逻辑可以直接修改为从当前线程中获取
        return UserContext.getUser();

    }
}
