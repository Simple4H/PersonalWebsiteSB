package com.simple.service.impl;

import com.simple.common.ServerResponse;
import com.simple.dao.ArticleMapper;
import com.simple.pojo.Article;
import com.simple.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ServerResponse<List<Article>> getArticleList(){
        List<Article> articleList =  articleMapper.getArticleList();
        if (articleList.size() != 0){
            return ServerResponse.createBySuccess("查询成功",articleList);
        }
        return ServerResponse.createByErrorMessage("没有查询到任何博客");
    }
}
