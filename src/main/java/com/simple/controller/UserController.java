package com.simple.controller;

import com.github.pagehelper.PageInfo;
import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.pojo.User;
import com.simple.service.IArticleService;
import com.simple.service.IEmailService;
import com.simple.service.IUserService;
import com.simple.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Create by S I M P L E on 2018/04/02 12:11:52
 */

@Controller
@RequestMapping("/user/")
@Slf4j
public class UserController {

    // TODO: 分布式的登录

    private final IEmailService iEmailService;

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService, IArticleService iArticleService, IEmailService iEmailService) {
        this.iUserService = iUserService;
        this.iArticleService = iArticleService;
        this.iEmailService = iEmailService;
    }

    private final IArticleService iArticleService;

    // TODO: redis重写
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session, Model model, HttpServletResponse response) {
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        model.addAttribute("loginMessage", userServerResponse.getMsg());
        if (userServerResponse.isSuccess()) {
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1, 5);
            session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
            session.setAttribute(Const.CURRENT_USER, userServerResponse.getData());
            try {
                response.sendRedirect("/tables");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "backstage/login";
    }

    // TODO: redis重写
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess("获取成功", user);
        }
        return ServerResponse.createByErrorMessage("请登录");
    }

    //登出
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    public void userLogout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute(Const.CURRENT_USER);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/checkLogin.do")
    public String login(HttpSession session, Model model, HttpServletResponse response) {
        if (session.getAttribute(Const.CURRENT_USER) != null) {
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1, 5);
            session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
            model.addAttribute("pageInfoServerResponse", pageInfoServerResponse);
            try {
                response.sendRedirect("/tables");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "/backstage/login";
    }

    // 注册
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    public String register(String username, String password1, String password2, String email, Model model, HttpServletResponse response) {
        ServerResponse serverResponse = iUserService.register(username, password1, password2, email);
        // 注册错误时候提示信息
        model.addAttribute("registerMessage", serverResponse.getMsg());
        if (serverResponse.isSuccess()) {
            // 判定用户名与邮箱有效后，将邮箱存储至cookie
            CookieUtil.writeLoginToken(response,email);
            return "backstage/getEmailCode";
        }
        return "backstage/register";
    }

    // 获取验证码
    @RequestMapping(value = "get_email_code.do", method = RequestMethod.POST)
    public String getEmailCode(HttpServletRequest request) {
        // 从cookie中获取登录注册时候的邮箱
        String email = CookieUtil.readLoginToken(request);
        // 调用发送邮箱接口
        ServerResponse response = iEmailService.sendEmail(email);
        if (response.isSuccess()){
            return "backstage/checkEmailCode";
        }
        return "error";
    }

    // 验证验证码
    @RequestMapping(value = "check_email_code.do", method = RequestMethod.POST)
    public String checkEmailCode(String emailCode,HttpServletRequest request) {
        String email = CookieUtil.readLoginToken(request);
        // 从cookie中获取登录注册时候的邮箱
        // 为什么需要重新获取邮箱呢？因为生成验证码的时候是以邮箱作为key
        ServerResponse serverResponse = iEmailService.checkEmailToken(emailCode,email);
        if (serverResponse.isSuccess()){
            return "backstage/login";
        }
        return "error";
    }
}
