package com.simple.service;

import com.simple.common.ServerResponse;
import com.simple.pojo.User;

/**
 * Create by S I M P L E on 2018/04/02 12:21:00
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse checkUserAuthority(User user);
}
