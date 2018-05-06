package com.simple.dao;

import com.simple.pojo.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> getArticleList();

    Article getIndependentArticle(String title);

    int updateArticleStatus(String title);

    List<Article> getAllArticleList();

    int createNewArticle(@Param("title") String title,@Param("context") String context);

    int deleteArticle(String title);

    Article getArticleByTitle(String title);

    int updateArticleByTitle(@Param("title")String title,@Param("content")String content,@Param("id")Integer id);

    int checkTitleExist(String title);

}