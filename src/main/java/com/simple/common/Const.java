package com.simple.common;

/**
 * Create by S I M P L E on 2018/04/02 13:02:32
 */
public class Const {

    public static final String CURRENT_USER = "CURRENT_USER";

    public interface Role {
        int ROLE_ADMIN = 1; // 管理员
        int ROLE_CUSTOMER = 0; // 普通用户
    }

    public interface Redis_Time {
        // cookie缓存
        int REDIS_COOKIE_EXIST_TIME = 60 * 60 * 24 * 7;
        // 邮件验证码缓存
        int REDIS_EMAIL_EXIST_TIME = 60 * 5;
        int REDIS_EMAIL_CODE_TIME = 60 * 5;
    }

    public interface Cookie_Time {
        int COOKIE_EXIST_TIME = 60 * 60 * 24 * 7;
    }

}
