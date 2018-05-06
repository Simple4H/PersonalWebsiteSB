package com.simple.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;
import com.simple.dao.ArticleMapper;
import com.simple.pojo.Article;
import com.simple.service.IArticleService;
import com.simple.util.DateTimeUtil;
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

    // 获取点击量最高的3篇文章
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

    // 获取某篇文章
    public ServerResponse<Article> independent(String title) {
        Article article = articleMapper.getIndependentArticle(title);
        if (article != null) {
            // 添加一阅读数量
            articleMapper.updateArticleStatus(title);
            return ServerResponse.createBySuccess("查询成功", article);
        }
        return ServerResponse.createByErrorMessage("查询异常!!!");
    }

    // 获取所有的文章
    public ServerResponse<PageInfo> getAllArticleList(int pageNum, int pageSize) {
        // 开始分页
        PageHelper.startPage(pageNum, pageSize);
        // 查询所有的文章
        List<Article> articleList = articleMapper.getAllArticleList();
        // 重组VO
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article articleItem : articleList) {
            ArticleVo articleVo = assembleArticleListVo(articleItem);
            articleVoList.add(articleVo);
        }
        // 使用原查询结果放进pageInfo
        PageInfo pageInfo = new PageInfo<>(articleList);
        // 将pageInfo用VO填充
        pageInfo.setList(articleVoList);
        return ServerResponse.createBySuccess("ojbk", pageInfo);
    }

    // 新增文章
    public ServerResponse createNewArticle(String title, String context) {
        int titleExist = articleMapper.checkTitleExist(title);
        // 判断文章是否存在
        if (titleExist < 1){
            int resultCount = articleMapper.createNewArticle(title, context);
            if (resultCount > 0) {
                return ServerResponse.createBySuccessMessage("新增成功");
            }
            return ServerResponse.createByErrorMessage("新增失败");
        }
        return ServerResponse.createByErrorMessage("文章标题已经存在");
    }

    // 组装ArticleVO
    private ArticleVo assembleArticleListVo(Article article) {
        ArticleVo articleVo = new ArticleVo();
        articleVo.setTitle(article.getTitle());
        String s = article.getContent();
        s = s.substring(0, 80) + "...";
        articleVo.setContent(s);
        articleVo.setStatus(article.getStatus());
        articleVo.setCreateTime(DateTimeUtil.dateToStr(article.getCreateTime()));
        return articleVo;
    }

    // 删除文章
    public ServerResponse deleteArticle(String title) {
        int resultCount = articleMapper.deleteArticle(title);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("删除成功");
        }
        return ServerResponse.createByErrorMessage("删除失败");
    }

    // 通过标题获取文章
    public ServerResponse<Article> getArticleByTile(String title) {
        Article article = articleMapper.getArticleByTitle(title);
        if (article != null) {
            return ServerResponse.createBySuccess("获取文章成功", article);
        }
        return ServerResponse.createByErrorMessage("获取文章失败！");
    }

    // 更新文章
    public ServerResponse updateArticleByTitle(String title, String content, Integer id) {
        int resultCount = articleMapper.updateArticleByTitle(title, content, id);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }
}
