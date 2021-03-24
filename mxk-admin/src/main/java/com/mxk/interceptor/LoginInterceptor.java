package com.mxk.interceptor;

import com.mxk.constants.AdminConstants;
import com.mxk.constants.MxkExceptionEnum;
import com.mxk.exception.MxkException;
import com.mxk.pojo.PayLoad;
import com.mxk.util.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("/user/login/page");
            return false;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(AdminConstants.TOKEN_NAME)) {
                token = cookie.getValue();
            }
        }
        if (StringUtils.isEmpty(token)) {
            response.sendRedirect("/user/login/page");
            return false;
        }
        boolean result = JwtUtils.checkSignature(token);
        if (!result) {
                throw new MxkException(MxkExceptionEnum.TOKEN_ERROR);
        }
        PayLoad payLoad = JwtUtils.getPayLoad(token);
        request.setAttribute("currUser", payLoad.getName());
        return true;
    }
}
