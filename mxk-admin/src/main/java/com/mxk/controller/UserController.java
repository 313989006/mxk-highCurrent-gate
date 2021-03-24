package com.mxk.controller;

import com.mxk.constants.AdminConstants;
import com.mxk.pojo.UserDTO;
import com.mxk.pojo.vo.Result;
import com.mxk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("")
    public Result add(@RequestBody @Validated UserDTO userDTO) {
        userService.add(userDTO);
        return Result.success();
    }


    @PostMapping("/login")
    public void login(@Validated UserDTO userDTO, HttpServletResponse response) throws IOException {
        userService.login(userDTO, response);
        response.sendRedirect("/app/list");
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie(AdminConstants.TOKEN_NAME, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "login";
    }
}
