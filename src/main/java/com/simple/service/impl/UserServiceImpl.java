package com.simple.service.impl;

import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.dao.UserMapper;
import com.simple.pojo.User;
import com.simple.service.IUserService;
import com.simple.util.JsonUtil;
import com.simple.util.MD5Util;
import com.simple.util.RedisPoolUtil;
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
        ServerResponse checkResult = loginCheckUsernameAndEmail(username);
        if (checkResult.isSuccess()) {
            String md5Password = MD5Util.MD5EncodeUtf8(password);
            User user = userMapper.checkUsernameAndPassword(username, md5Password);
            if (user != null) {
                user.setPassword(StringUtils.EMPTY);
                userMapper.updateUserLoginTime(username);
                return ServerResponse.createBySuccess("登录成功", user);
            }
            return ServerResponse.createByErrorMessage("密码错误");
        }
        return ServerResponse.createByErrorMessage(checkResult.getMsg());
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
            ServerResponse checkResult = checkUsernameAndEmail(username, email);
            if (checkResult.isSuccess()) {
                // 没有验证的先保存到redis
                User user = new User();
                user.setUsername(username);
                user.setPassword(MD5Util.MD5EncodeUtf8(password1));
                user.setEmail(email);
                String userString = JsonUtil.obj2String(user);
                // 将注册好，但是未验证邮箱的账户存入redis
                String result = RedisPoolUtil.setEx(email, Const.Redis_Time.REDIS_EXIST_TIME, userString);
                if (StringUtils.equals(result, "OK")) {
                    return ServerResponse.createBySuccessMessage("ok");
                }
                return ServerResponse.createByErrorMessage("error");
            }
            return ServerResponse.createByErrorMessage(checkResult.getMsg());
        }
        return ServerResponse.createByErrorMessage("两次密码不一致");
    }

    // 封装验证账号密码
    private ServerResponse checkUsernameAndEmail(String username, String email) {
        int usernameCount = userMapper.checkUsername(username);
        if (usernameCount > 0) {
            return ServerResponse.createByErrorMessage("用户名已经存在");
        }
        int emailCount = userMapper.checkEmail(email);
        if (emailCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经存在");
        }
        return ServerResponse.createBySuccessMessage("验证成功");
    }

    // 封装验证登录用户名或邮箱
    private ServerResponse loginCheckUsernameAndEmail(String login){
        int isUsername = userMapper.checkUsername(login);
        if (isUsername > 0){
            return ServerResponse.createBySuccessMessage("用户名校验成功");
        }
        int isEmail = userMapper.checkEmail(login);
        if (isEmail > 0) {
            return ServerResponse.createBySuccessMessage("邮箱验证成功");
        }
        return ServerResponse.createByErrorMessage("用户名与邮箱都不存在");
    }
}
