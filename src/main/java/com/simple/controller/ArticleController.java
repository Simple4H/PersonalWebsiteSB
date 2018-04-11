package com.simple.controller;

import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;
import com.simple.pojo.Article;
import com.simple.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.simple.vo.ArticleVo;

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
    public String getArticleHotList(Model model) {
        ServerResponse<List<ArticleVo>> listServerResponse = iArticleService.getArticleHotList();
        List<ArticleVo> articleHotList = listServerResponse.getData();
        model.addAttribute("articleHotList", articleHotList);
        return "index";
    }

    @RequestMapping(value = "/article/independent", method = RequestMethod.GET)
    public String independent(String title,Model model) {
        ServerResponse<Article> serverResponse = iArticleService.independent(title);
        Article article = serverResponse.getData();
        model.addAttribute("article",article);
        return "independent";
    }

    //前台
    @RequestMapping(value = "/post",method = RequestMethod.GET)
    public String post(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "4") int pageSize, Model model){
        ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum,pageSize);
        model.addAttribute("pageInfoServerResponse",pageInfoServerResponse);
        return "post";
    }

    //后台
    @RequestMapping(value = "/backstage_post.do",method = RequestMethod.GET)
    public String backstagePost(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize,HttpSession session){
        ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum,pageSize);
        session.setAttribute("pageInfoServerResponse",pageInfoServerResponse);
        return "backstage/tables";
    }

    @RequestMapping(value = "/article/create_new_article.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse createNewArticle(String title,String context,HttpSession session){
        ServerResponse serverResponse = iArticleService.createNewArticle(title, context);
        if (serverResponse.isSuccess()){
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(1,5);
            session.setAttribute("pageInfoServerResponse",pageInfoServerResponse);
            return serverResponse;
        }
        return serverResponse;
    }

    //删除文章
    @RequestMapping(value = "/article/delete_article.do",method = RequestMethod.GET)
    public String deleteArticle(String title, @RequestParam(value = "pageSize",defaultValue = "5") int pageSize,HttpSession session){
        ServerResponse serverResponse = iArticleService.deleteArticle(title);
        if (serverResponse.isSuccess()){
            ServerResponse<PageInfo> a = (ServerResponse<PageInfo>) session.getAttribute("pageInfoServerResponse");
            int pageNum = a.getData().getPageNum();
            ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum,pageSize);
            session.setAttribute("pageInfoServerResponse",pageInfoServerResponse);
            return "backstage/tables";
        }
        return "backstage/tables";
    }
}
