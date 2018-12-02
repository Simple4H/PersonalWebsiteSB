package com.simple.controller;

import com.github.pagehelper.PageInfo;
import com.simple.common.Const;
import com.simple.common.RedissonConfig;
import com.simple.common.ServerResponse;
import com.simple.pojo.Article;
import com.simple.pojo.User;
import com.simple.service.IArticleService;
import com.simple.service.IUserService;
import com.simple.util.CookieUtil;
import com.simple.util.JsonUtil;
import com.simple.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.simple.vo.ArticleVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Create by S I M P L E on 2018/03/31 10:33:50
 */

@SuppressWarnings("ALL")
@Controller
@Slf4j
public class ArticleController {


    @Autowired
    public ArticleController(IArticleService iArticleService, IUserService iUserService) {
        this.iArticleService = iArticleService;
        this.iUserService = iUserService;
    }

    private final IArticleService iArticleService;

    private final IUserService iUserService;

    // 获取三篇点击量最高的文章
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getArticleHotList(Model model) {
        ServerResponse<List<ArticleVo>> listServerResponse = iArticleService.getArticleHotList();
        List<ArticleVo> articleHotList = listServerResponse.getData();
        model.addAttribute("articleHotList", articleHotList);
        return "index";
    }

    // 获取单篇文章
    @RequestMapping(value = "/article/independent", method = RequestMethod.GET)
    public String independent(String title, Model model) {
        ServerResponse<Article> serverResponse = iArticleService.independent(title);
        Article article = serverResponse.getData();
        model.addAttribute("article", article);
        return "independent";
    }

    // 前台获取文章
    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String post(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "4") int pageSize, Model model) {
        ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum, pageSize);
        model.addAttribute("pageInfoServerResponse", pageInfoServerResponse);
        return "post";
    }

    // 后台--文章列表
    @RequestMapping(value = "/backstage_post.do", method = RequestMethod.GET)
    public String backstagePost(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize, HttpSession session) {
        ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum, pageSize);
        session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
        return "backstage/tables";
    }

    // 后台--添加新的文章
    @RequestMapping(value = "/article/create_new_article.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse createNewArticle(String title, String context, HttpServletRequest request) {
        RLock lock = RedissonConfig.getRedisson().getLock(Const.Redisson_Name.REDISSON_NAME);
        boolean getLock = false;
        try {
            getLock = lock.tryLock(0,5,TimeUnit.SECONDS);
            if (getLock) {
                String loginToken = CookieUtil.readLoginToken(request);
                String userString = RedisShardedPoolUtil.get(loginToken);
                User user = JsonUtil.string2Obj(userString, User.class);
                if (iUserService.checkUserAuthority(user).isSuccess()) {
                    ServerResponse serverResponse = iArticleService.createNewArticle(title, context);
                    if (serverResponse.isSuccess()) {
                        return serverResponse;
                    }
                    return serverResponse;
                }
                return ServerResponse.createByErrorMessage("权限不够哟，无法添加新的文章~");
            }else{
                ServerResponse.createByErrorMessage("没有获取到分布式锁");
            }
        } catch (InterruptedException e) {
            log.error("create redisson lock exception:{},Thread Name:{}",e,Thread.currentThread().getName());
            return ServerResponse.createByErrorMessage("分布式锁获取异常");
        }
        return ServerResponse.createByErrorMessage("分布式锁获取异常");
    }

    // 后台--删除文章
    @RequestMapping(value = "/article/delete_article.do", method = RequestMethod.GET)
    public String deleteArticle(String title, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize, HttpSession session, HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        String userString = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userString, User.class);
        if (iUserService.checkUserAuthority(user).isSuccess()) {
            ServerResponse serverResponse = iArticleService.deleteArticle(title);
            if (serverResponse.isSuccess()) {
                ServerResponse<PageInfo> a = (ServerResponse<PageInfo>) session.getAttribute("pageInfoServerResponse");
                int pageNum = a.getData().getPageNum();
                ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum, pageSize);
                session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
                return "backstage/tables";
            }
            return "backstage/tables";
        }
        return "error2";
    }

    // 后台--编辑文章
    @RequestMapping(value = "/article/edit.do", method = RequestMethod.GET)
    public String edit(String title, HttpSession session) {
        ServerResponse<Article> articleByTitle = iArticleService.getArticleByTile(title);
        if (articleByTitle.isSuccess()) {
            session.setAttribute("articleByTitle", articleByTitle);
            return "backstage/edit";
        }
        return "backstage/edit";
    }

    // 后台--提交编辑文章
    @RequestMapping(value = "/article/edit_submit.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse editSubmit(String title, String content, @RequestParam(value = "pageSize", defaultValue = "5") int pageSize, HttpSession session, HttpServletRequest request) {
        // 分布式锁
        RLock lock = RedissonConfig.getRedisson().getLock(Const.Redisson_Name.REDISSON_NAME);
        boolean getLock = false;
        try {
            getLock = lock.tryLock(0, 2, TimeUnit.SECONDS);
            if (getLock) {
                log.info("获取到了分布式锁，开始执行相关操作");
                String loginToken = CookieUtil.readLoginToken(request);
                String userString = RedisShardedPoolUtil.get(loginToken);
                User user = JsonUtil.string2Obj(userString, User.class);
                if (iUserService.checkUserAuthority(user).isSuccess()) {
                    ServerResponse<Article> article = (ServerResponse<Article>) session.getAttribute("articleByTitle");
                    Integer id = article.getData().getId();
                    ServerResponse serverResponse = iArticleService.updateArticleByTitle(title, content, id);
                    if (serverResponse.isSuccess()) {
                        ServerResponse<PageInfo> a = (ServerResponse<PageInfo>) session.getAttribute("pageInfoServerResponse");
                        int pageNum = a.getData().getPageNum();
                        ServerResponse<PageInfo> pageInfoServerResponse = iArticleService.getAllArticleList(pageNum, pageSize);
                        session.setAttribute("pageInfoServerResponse", pageInfoServerResponse);
                        session.removeAttribute("articleByTitle");
                        return serverResponse;
                    }
                    return serverResponse;
                }
                return ServerResponse.createByErrorMessage("权限不够哟，无法编辑文章~");
            } else {
                log.error("获取分布式锁失败:{},ThreadName:{}", Const.Redisson_Name.REDISSON_NAME, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("分布式锁异常:{},ThreadName:{}", e, Thread.currentThread().getName());
        }
        return ServerResponse.createByErrorMessage("分布式异常！！！");
    }

}
