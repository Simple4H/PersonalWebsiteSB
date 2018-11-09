package com.simple.controller;


import com.github.pagehelper.PageInfo;
import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.pojo.User;
import com.simple.service.IMessageService;
import com.simple.util.CookieUtil;
import com.simple.util.JsonUtil;
import com.simple.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Create by S I M P L E on 2018/03/30 00:10:47
 */

@Controller
@RequestMapping(value = "/message/")
public class MessageController {

    private final IMessageService iMessageService;

    @Autowired
    public MessageController(IMessageService iMessageService) {
        this.iMessageService = iMessageService;
    }

    @RequestMapping(value = "upload_website_message.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> uploadWebsiteMessage(String name, String email, String phone, String message) {
        return iMessageService.uploadWebsiteMessage(name, email, phone, message);
    }

    @RequestMapping(value = "get_all_message.do", method = RequestMethod.GET)
    public String getAllMessage(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        String userString = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userString, User.class);
        assert user != null;
        if (user.getAuthority() == Const.Role.ROLE_ADMIN) {
            ServerResponse<PageInfo> pageInfoMessage = iMessageService.getAllMessage(pageNum, pageSize);
            if (pageInfoMessage.isSuccess()) {
                session.setAttribute("pageInfoMessage", pageInfoMessage);
                return "backstage/messageShow";
            }
            // 异常页面
            return "error";
        }
        // 权限不足页面
        return "error2";
    }
    // TODO: 删除消息
}
