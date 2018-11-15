package com.simple.service;

import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;

/**
 * Create by S I M P L E on 2018/03/30 00:13:54
 */
public interface IMessageService {

    ServerResponse<String> uploadWebsiteMessage(String name, String email, String phone, String message);

    ServerResponse<PageInfo> getAllMessage(int pageNum, int pageSize);

    ServerResponse deleteMessage(String message, String phone);
}
