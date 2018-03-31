package com.simple.service;

import com.simple.common.ServerResponse;
import com.simple.pojo.Article;

import java.util.List;

/**
 * Create by S I M P L E on 2018/03/31 10:34:37
 */
public interface IArticleService {

    ServerResponse<List<Article>> getArticleList();
}
