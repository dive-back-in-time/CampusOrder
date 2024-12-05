package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    final private String URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用微信用户接口，获取openid
        String openid = getOpenid(userLoginDTO);




        // 判断 openid 是否为空，如果为空，抛出异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //直接插入用户名字
        User user = userMapper.getByOpenId(userLoginDTO.getCode());
        if (user == null) {
            user = User.builder()
                    .openid(userLoginDTO.getCode())
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    private String getOpenid(UserLoginDTO userLoginDTO) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("appid", weChatProperties.getAppid());
        hashMap.put("secret", weChatProperties.getSecret());
        hashMap.put("js_code", userLoginDTO.getCode());
        hashMap.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(URL, hashMap);
        JSONObject parseJson = JSONObject.parseObject(json);
        String openid = parseJson.getString("openid");
        return openid;
    }
}
