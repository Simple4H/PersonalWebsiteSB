package com.simple.controller;


import com.simple.common.ServerResponse;
import com.simple.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "upload_website_message.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> uploadWebsiteMessage(String name, String email, String phone, String message) {
        return iMessageService.uploadWebsiteMessage(name, email, phone, message);
    }

}
