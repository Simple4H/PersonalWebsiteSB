package com.simple.controller;

import com.simple.service.IMessageService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Create By S I M P L E On 2018/11/15 17:24:20
 */
public class MessageControllerTest {

    @Autowired
    private IMessageService iMessageService;

    @Test
    public void deleteMessage() {
        iMessageService.deleteMessage("");
    }
}