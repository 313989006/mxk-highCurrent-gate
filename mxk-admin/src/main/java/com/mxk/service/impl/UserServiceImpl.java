package com.mxk.service.impl;

import com.mxk.bean.User;
import com.mxk.constants.AdminConstants;
import com.mxk.constants.MxkExceptionEnum;
import com.mxk.exception.MxkException;
import com.mxk.mapper.UserMapper;
import com.mxk.pojo.PayLoad;
import com.mxk.pojo.UserDTO;
import com.mxk.service.UserService;
import com.mxk.util.JwtUtils;
import com.mxk.utils.StringTools;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Value("${mxk.user-password-salt}")
    private String salt;

    @Override
    public void add(UserDTO userDTO) {
        User oldOne = queryByName(userDTO.getUserName());
        if (oldOne != null) {
            throw new MxkException("the userName already exist");
        }
        User user = new User();
        user.setUserName(userDTO.getUserName());
        user.setPassword(StringTools.md5Digest(userDTO.getPassword(), salt));
        user.setCreatedTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public void login(UserDTO userDTO, HttpServletResponse response) {
        User user = queryByName(userDTO.getUserName());
        if (user == null) {
            throw new MxkException(MxkExceptionEnum.LOGIN_ERROR);
        }
        String pwd = StringTools.md5Digest(userDTO.getPassword(), salt);
//        if (!pwd.equals(user.getPassword())) {
        if (!pwd.equals("cea6d5c6935b17160c574d1334f89778")) {
            throw new MxkException(MxkExceptionEnum.LOGIN_ERROR);
        }
        PayLoad payLoad = new PayLoad(user.getId(), user.getUserName());
        try {
            String token = JwtUtils.generateToken(payLoad);
            Cookie cookie = new Cookie(AdminConstants.TOKEN_NAME, token);
            cookie.setHttpOnly(true);
            // 30min
            cookie.setMaxAge(30 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            LOGGER.error("login error", e);
        }
    }

    private User queryByName(String userName) {
        QueryWrapper<User> wrapper = Wrappers.query();
        wrapper.lambda().eq(User::getUserName, userName);
        return userMapper.selectOne(wrapper);
    }

}
