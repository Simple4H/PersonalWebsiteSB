package com.simple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;
import com.simple.dao.ArticleMapper;
import com.simple.pojo.Article;
import com.simple.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.simple.vo.ArticleVo;

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

    //获取点击量最高的3篇文章
    public ServerResponse<List<ArticleVo>> getArticleHotList() {
        List<ArticleVo> articleVo = new ArrayList<>();
        List<Article> articleList = articleMapper.getArticleList();
        if (articleList.size() != 0) {
            for (Article article : articleList) {
                ArticleVo articleVo1 = assembleArticleListVo(article);
                articleVo.add(articleVo1);
            }
            return ServerResponse.createBySuccess("查询成功", articleVo);
        }
        return ServerResponse.createByErrorMessage("没有查询到任何博客");
    }

    //获取某篇文章
    public ServerResponse<Article> independent(String title) {
        Article article = articleMapper.getIndependentArticle(title);
        if (article != null) {
            //添加一阅读数量
            articleMapper.updateArticleStatus(title);
            return ServerResponse.createBySuccess("查询成功", article);
        }
        return ServerResponse.createByErrorMessage("查询异常!!!");
    }

    //获取所有的文章
    public ServerResponse<PageInfo> getAllArticleList(int pageNum, int pageSize) {
        //开始分页
        PageHelper.startPage(pageNum, pageSize);
        //查询所有的文章
        List<Article> articleList = articleMapper.getAllArticleList();
        //重组VO
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article articleItem : articleList) {
            ArticleVo articleVo = assembleArticleListVo(articleItem);
            articleVoList.add(articleVo);
        }
        //使用原查询结果放进pageInfo
        PageInfo pageInfo = new PageInfo<>(articleList);
        //将pageInfo用VO填充
        pageInfo.setList(articleVoList);
        return ServerResponse.createBySuccess("ojbk", pageInfo);
    }

    public ServerResponse createNewArticle(String title,String context){
        int resultCount = articleMapper.createNewArticle(title, context);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("新增成功");
        }
        return ServerResponse.createByErrorMessage("新增失败");
    }

    //组装ArticleVO
    private ArticleVo assembleArticleListVo(Article article) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setTitle(article.getTitle());
        String s = article.getContent();
        s = s.substring(0, 80) + "...";
        articleVo.setContent(s);
        articleVo.setStatus(article.getStatus());
        articleVo.setCreateTime(article.getCreateTime());
        return articleVo;
    }
}
