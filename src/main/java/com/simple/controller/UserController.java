package com.simple.controller;

import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.pojo.User;
import com.simple.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Create by S I M P L E on 2018/04/02 12:11:52
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        if (userServerResponse.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, userServerResponse.getData());
        }
        return userServerResponse;
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess("获取成功",user);
        }
        return ServerResponse.createByErrorMessage("请登录");
    }
}
