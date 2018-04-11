package com.simple.service;

import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;
import com.simple.pojo.Article;
import com.simple.vo.ArticleVo;

import java.util.List;

/**
 * Create by S I M P L E on 2018/03/31 10:34:37
 */
public interface IArticleService {

    ServerResponse<List<ArticleVo>> getArticleHotList();

    ServerResponse<Article> independent(String title);

    ServerResponse<PageInfo> getAllArticleList(int pageNum, int pageSize);

    ServerResponse createNewArticle(String title,String context);

    ServerResponse deleteArticle(String title);
}
