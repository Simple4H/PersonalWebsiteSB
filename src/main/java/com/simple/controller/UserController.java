package com.simple.controller;

import com.github.pagehelper.PageInfo;
import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.pojo.User;
import com.simple.service.IArticleService;
import com.simple.service.IEmailService;
import com.simple.service.IUserService;
import com.simple.util.CookieUtil;
import com.simple.util.JsonUtil;
import com.simple.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    // TODO: 2018/10/20 获取登录时间

    private final IEmailService iEmailService;

    private final IUserService iUserService;

    @Autowired
    public UserController(IUserService iUserService, IArticleService iArticleService, IEmailService iEmailService) {
        this.iUserService = iUserService;
        this.iArticleService = iArticleService;
        this.iEmailService = iEmailService;
    }

    private final IArticleService iArticleService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session, Model model, HttpServletResponse response) {
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        model.addAttribute("loginMessage", userServerResponse.getMsg());
        if (userServerResponse.isSuccess()) {
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1, 5);
            session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
            CookieUtil.writeLoginToken(response, session.getId());
            RedisShardedPoolUtil.setEx(session.getId(), Const.Redis_Time.REDIS_COOKIE_EXIST_TIME, JsonUtil.obj2String(userServerResponse.getData()));
//            // 保存登录时间
//            model.addAttribute("UserLoginTime", "abc");
            try {
                response.sendRedirect("/tables");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "backstage/login";
    }

    // 获取用户信息
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserInfo(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (!StringUtils.isEmpty(loginToken)) {
            String userString = RedisShardedPoolUtil.get(loginToken);
            User user = JsonUtil.string2Obj(userString, User.class);
            return ServerResponse.createBySuccess("获取成功", user);
        }
        return ServerResponse.createByErrorMessage("请登录");
    }

    // 登出
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    public String userLogout(HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request, response);
        RedisShardedPoolUtil.del(loginToken);
        return "redirect:/";
    }

    // 检查登录信息
    @RequestMapping(value = "checkLogin.do")
    public String login(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (!StringUtils.isEmpty(loginToken)) {
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1, 5);
            session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
            model.addAttribute("pageInfoServerResponse", pageInfoServerResponse);
            try {
                response.sendRedirect("/tables");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "backstage/login";
    }

    // 注册
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    public String register(String username, String password1, String password2, String email, Model model, HttpServletResponse response) {
        ServerResponse serverResponse = iUserService.register(username, password1, password2, email);
        // 注册错误时候提示信息
        model.addAttribute("registerMessage", serverResponse.getMsg());
        if (serverResponse.isSuccess()) {
            // 判定用户名与邮箱有效后，将邮箱存储至cookie
            CookieUtil.writeLoginToken(response, email);
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
        if (response.isSuccess()) {
            return "backstage/checkEmailCode";
        }
        return "error";
    }

    // 验证验证码
    @RequestMapping(value = "check_email_code.do", method = RequestMethod.POST)
    public String checkEmailCode(String emailCode, HttpServletRequest request, HttpServletResponse response) {
        String email = CookieUtil.readLoginToken(request);
        // 从cookie中获取登录注册时候的邮箱
        // 为什么需要重新获取邮箱呢？因为生成验证码的时候是以邮箱作为key
        ServerResponse serverResponse = iEmailService.checkEmailToken(emailCode, email);
        if (serverResponse.isSuccess()) {
            // 注册完后删除了cookie
            CookieUtil.delLoginToken(request, response);
            return "backstage/login";
        }
        return "error";
    }
}
