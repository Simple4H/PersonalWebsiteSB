package com.simple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Create by S I M P L E on 2018/03/31 17:09:55
 */

@Controller
public class RedirectionController {

    @RequestMapping(value = "/contact")
    public String contact() {
        return "contact";
    }

    @RequestMapping(value = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/addArticle")
    public String addArticle() {
        return "/backstage/addArticle";
    }

    @RequestMapping(value = "/tables")
    public String tables() {
        return "/backstage/tables";
    }

    @RequestMapping(value = "/messageEdit")
    public String messageEdit() {
        return "/backstage/messageEdit";
    }

    @RequestMapping(value = "/register")
    public String register(){
        return "/backstage/register";
    }

}
