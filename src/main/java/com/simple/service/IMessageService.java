package com.simple.service;

import com.simple.common.ServerResponse;

/**
 * Create by S I M P L E on 2018/03/30 00:13:54
 */
public interface IMessageService {

    ServerResponse<String> uploadWebsiteMessage(String name, String email, String phone, String message);
}
