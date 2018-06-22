package com.simple.service.impl;

import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.dao.UserMapper;
import com.simple.pojo.User;
import com.simple.service.IUserService;
import com.simple.util.MD5Util;
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

    public ServerResponse<User> login(String username, String password) {
        int usernameResult = userMapper.checkUsername(username);
        if (usernameResult > 0) {
            String md5Password = MD5Util.MD5EncodeUtf8(password);
            User user = userMapper.checkUsernameAndPassword(username, md5Password);
            if (user != null) {
                user.setPassword(StringUtils.EMPTY);
                userMapper.updateUserLoginTime(username);
                return ServerResponse.createBySuccess("登录成功", user);
            }
            return ServerResponse.createByErrorMessage("密码错误");
        }
        return ServerResponse.createByErrorMessage("用户名不存在");
    }

    public ServerResponse checkUserAuthority(User user) {
        if (user != null && user.getAuthority() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

    // 注册
    public ServerResponse register(String username, String password1, String password2, String email) {
        if (password1.equals(password2)) {
            int usernameCount = userMapper.checkUsername(username);
            if (usernameCount > 0) {
                return ServerResponse.createByErrorMessage("用户名已经存在");
            }
            int emailCount = userMapper.checkEmail(email);
            if (emailCount > 0) {
                return ServerResponse.createByErrorMessage("邮箱已经存在");
            }
            if ((userMapper.register(username, password1, email) > 0)) {
                return ServerResponse.createBySuccessMessage("注册成功");
            }
            return ServerResponse.createByErrorMessage("注册异常");
        }
        return ServerResponse.createByErrorMessage("两次密码不一致");

    }
}
