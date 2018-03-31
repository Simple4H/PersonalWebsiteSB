package com.simple.service;

import com.simple.common.ServerResponse;
import vo.ArticleVo;

import java.util.List;

/**
 * Create by S I M P L E on 2018/03/31 10:34:37
 */
public interface IArticleService {

    ServerResponse<List<ArticleVo>> getArticleList();
}
