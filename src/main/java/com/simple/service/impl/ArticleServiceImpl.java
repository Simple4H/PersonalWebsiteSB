package com.simple.service.impl;

import com.simple.common.ServerResponse;
import com.simple.dao.ArticleMapper;
import com.simple.pojo.Article;
import com.simple.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;
import vo.ArticleVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by S I M P L E on 2018/03/31 10:34:53
 */

@Service("iArticleService")
public class ArticleServiceImpl implements IArticleService {

    private final ArticleMapper articleMapper;

    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper) {
        this.articleMapper = articleMapper;
    }

    public ServerResponse<List<ArticleVo>> getArticleList(){
        List<ArticleVo> articleVo = new ArrayList<>();
        List<Article> articleList =  articleMapper.getArticleList();
        if (articleList.size() != 0){
            for (Article article:articleList) {
                ArticleVo articleVo1 = assembleArticleListVo(article);
                articleVo.add(articleVo1);
            }
            return ServerResponse.createBySuccess("查询成功",articleVo);
        }
        return ServerResponse.createByErrorMessage("没有查询到任何博客");
    }

    private ArticleVo assembleArticleListVo(Article article){
         ArticleVo articleVo = new ArticleVo();
         articleVo.setTitle(article.getTitle());
         String s = article.getContent();
         s = s.substring(0,50) + "...";
         articleVo.setContent(s);
         article.setStatus(article.getStatus());
         return articleVo;
    }
}
