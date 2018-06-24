package com.simple.service;

import com.simple.common.ServerResponse;

/**
 * Create by S I M P L E on 2018/06/24 10:44:20
 */
public interface IEmailService {

    ServerResponse sendEmail(String email);

    ServerResponse checkEmailToken(String token, String email);
}
