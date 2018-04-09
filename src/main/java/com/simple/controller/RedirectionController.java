package com.simple.controller;

import com.simple.common.Const;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Create by S I M P L E on 2018/03/31 17:09:55
 */

@Controller
public class RedirectionController {

    @RequestMapping(value = "/contact")
    public String contact(){
        return "contact";
    }

    @RequestMapping(value = "/about")
    public String about(){
        return "about";
    }

    @RequestMapping(value = "/login")
    public String login(HttpSession session){
        if (session.getAttribute(Const.CURRENT_USER) != null){
            return "/backstage/tables";
        }
        return "/backstage/login";
    }

    @RequestMapping(value = "/edit")
    public String edit(){
        return "/backstage/edit";
    }

    @RequestMapping(value = "/tables")
    public String tables(){
        return "/backstage/tables";
    }

}
