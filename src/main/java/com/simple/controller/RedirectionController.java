package com.simple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Create by S I M P L E on 2018/03/31 17:09:55
 */

@Controller
public class RedirectionController {

    @RequestMapping(value = "/")
    public String welcome(){
        return "index";
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/contact")
    public String contact(){
        return "contact";
    }

    @RequestMapping(value = "/post")
    public String post(){
        return "post";
    }

    @RequestMapping(value = "/about")
    public String about(){
        return "about";
    }


}
