package com.simple.vo;


import java.io.Serializable;

/**
 * Create by S I M P L E on 2018/03/31 19:25:04
 */
public class ArticleVo implements Serializable {

    private String title;

    private String content;

    private String status;

    private String createTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
