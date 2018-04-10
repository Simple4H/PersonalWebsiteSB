package com.simple.controller;

import com.github.pagehelper.PageInfo;
import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.pojo.User;
import com.simple.service.IArticleService;
import com.simple.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Create by S I M P L E on 2018/04/02 12:11:52
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService, IArticleService iArticleService) {
        this.iUserService = iUserService;
        this.iArticleService = iArticleService;
    }

    private final IArticleService iArticleService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public String login( String username, String password, HttpSession session,Model model,HttpServletResponse response) {
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        model.addAttribute("loginMessage",userServerResponse.getMsg());
        if (userServerResponse.isSuccess()) {
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1,5);
            session.setAttribute("pageInfoServerResponse",pageInfoServerResponse);
            session.setAttribute(Const.CURRENT_USER, userServerResponse.getData());
            try {
                response.sendRedirect("/tables");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "backstage/login";
    }

    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess("获取成功", user);
        }
        return ServerResponse.createByErrorMessage("请登录");
    }

    //等出
    @RequestMapping(value = "logout.do",method = RequestMethod.GET)
    public void userLogout(HttpSession session, HttpServletResponse response){
        session.removeAttribute(Const.CURRENT_USER);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/checkLogin.do")
    public String login(HttpSession session,Model model,HttpServletResponse response){
        if (session.getAttribute(Const.CURRENT_USER) != null){
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1,5);
            session.setAttribute("pageInfoServerResponse",pageInfoServerResponse);
            model.addAttribute("pageInfoServerResponse",pageInfoServerResponse);
            try {
                response.sendRedirect("/tables");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "/backstage/login";
    }
}
