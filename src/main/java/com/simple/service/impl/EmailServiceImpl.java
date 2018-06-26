package com.simple.service.impl;

import com.simple.common.Const;
import com.simple.common.ServerResponse;
import com.simple.dao.UserMapper;
import com.simple.pojo.User;
import com.simple.service.IEmailService;
import com.simple.service.IUserService;
import com.simple.util.JsonUtil;
import com.simple.util.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Create by S I M P L E on 2018/06/24 10:44:40
 */
@Service("iEmailService")
@Slf4j
public class EmailServiceImpl implements IEmailService {

    private final UserMapper userMapper;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, UserMapper userMapper) {
        this.javaMailSender = javaMailSender;
        this.userMapper = userMapper;
    }

    public ServerResponse sendEmail(String email) {

        // 建立邮箱消息
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // 发送者
        simpleMailMessage.setFrom(sender);

        // 接收者
        simpleMailMessage.setTo(email);

        // 发送标题
        simpleMailMessage.setSubject("您好，欢迎注册~");

        // 发送内容
        // 生成四位数的验证码
        int token = (int) (Math.random() * (9999 - 1000 + 1) + 1000);
        simpleMailMessage.setText("您的验证码是:" + token);

        try {
            javaMailSender.send(simpleMailMessage);
            String tokenStr = JsonUtil.obj2String(token);
            // 将验证码存入redis中
            RedisPoolUtil.setEx(email + "token", Const.Redis_Time.REDIS_EMAIL_CODE_TIME, tokenStr);
            return ServerResponse.createBySuccess("发送成功", token);
        } catch (MailException e) {
            log.error("发送邮件异常:{}", e);
            return ServerResponse.createBySuccessMessage("发送失败");
        }
    }

    public ServerResponse checkEmailToken(String token, String email) {
        // 先取Redis中的验证码，并且Json反序列化
        String jsonResponse = JsonUtil.string2Obj(RedisPoolUtil.get(email + "token"), String.class);
        if (jsonResponse != null) {
            // 判断验证码是否正确
            if (StringUtils.equals(jsonResponse, token)) {
                // 验证成功后将用户存入数据库
                String userStr = RedisPoolUtil.get(email);
                User user = JsonUtil.string2Obj(userStr, User.class);
                if (user != null) {
                    // 存入数据库
                    if (userMapper.register(user.getUsername(), user.getPassword(), user.getEmail()) > 0) {
                        return ServerResponse.createBySuccessMessage("邮箱验证成功，用户信息存入数据库");
                    }
                    return ServerResponse.createByErrorMessage("存入数据库异常");
                }
                return ServerResponse.createByErrorMessage("超时，验证时间过长");
            }
            return ServerResponse.createByErrorMessage("验证码错误");
        }
        return ServerResponse.createByErrorMessage("验证码超时，请重新获取");

    }
}
