package com.simple.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.common.ServerResponse;
import com.simple.dao.MessageMapper;
import com.simple.pojo.Message;
import com.simple.service.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by S I M P L E on 2018/03/30 00:14:46
 */
@Service("iMessageService")
public class MessageServiceImpl implements IMessageService {

    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public ServerResponse<String> uploadWebsiteMessage(String name, String email, String phone, String message) {
        int resultCount = messageMapper.uploadMessage(name, email, phone, message);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("提交成功");
        }
        return ServerResponse.createByErrorMessage("提交失败");
    }

    public ServerResponse<PageInfo> getAllMessage(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Message> messages = messageMapper.getAllMessage();
        PageInfo pageInfo = new PageInfo(messages);
        return ServerResponse.createBySuccess("查询成功",pageInfo);
    }

}
