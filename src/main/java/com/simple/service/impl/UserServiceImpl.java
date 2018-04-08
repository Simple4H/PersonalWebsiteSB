package com.simple.service.impl;

import com.simple.common.ServerResponse;
import com.simple.dao.UserMapper;
import com.simple.pojo.User;
import com.simple.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by S I M P L E on 2018/04/02 12:21:34
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ServerResponse<User> login(String username, String password){
        int usernameResult = userMapper.checkUsername(username);
        if (usernameResult > 0) {
            User user = userMapper.checkUsernameAndPassword(username, password);
            if (user != null) {
                user.setPassword(StringUtils.EMPTY);
                return ServerResponse.createBySuccess("登录成功",user);
            }
            return ServerResponse.createByErrorMessage("密码错误");
        }
        return ServerResponse.createByErrorMessage("用户名不存在");
    }
}
