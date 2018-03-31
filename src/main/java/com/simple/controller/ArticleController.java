package com.simple.controller;

import com.simple.common.ServerResponse;
import com.simple.pojo.Article;
import com.simple.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Create by S I M P L E on 2018/03/31 10:33:50
 */

@Controller
public class ArticleController {

    private final IArticleService iArticleService;

    @Autowired
    public ArticleController(IArticleService iArticleService) {
        this.iArticleService = iArticleService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getArticleList(HttpSession session) {
        ServerResponse<List<Article>> listServerResponse = iArticleService.getArticleList();
                List<Article> articleList = listServerResponse.getData();
            session.setAttribute("articleList",articleList);
        return "index";
    }
}
