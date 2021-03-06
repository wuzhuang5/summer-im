package com.wd.service.impl;

import com.wd.constant.Constant;
import com.wd.enums.StatusEnum;
import com.wd.service.AccountService;
import com.wd.service.UserInfoCacheService;
import com.wd.vo.req.LoginReqVo;
import com.wd.vo.res.RegisterInfoResVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import static com.wd.constant.Constant.ACCOUNT_PREFIX;
/**
 * @author wz
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @Override
    public RegisterInfoResVo register(RegisterInfoResVo info) throws Exception {
        String key = Constant.ACCOUNT_PREFIX+info.getUserId();
        String name = redisTemplate.opsForValue().get(info.getUserName());
        if(name == null) {
            redisTemplate.opsForValue().set(key, info.getUserName());
            redisTemplate.opsForValue().set(info.getUserName(), key);
        } else {
         long userId = Long.parseLong(name.split(":")[1]);
         info.setUserId(userId);
         info.setUserName(info.getUserName());
        }
        return info;
    }

    @Override
    public StatusEnum login(LoginReqVo loginReqVo) throws Exception {
        String key = ACCOUNT_PREFIX+loginReqVo.getUserId();
        String userName = redisTemplate.opsForValue().get(key);
        if(userName == null) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }
        if(!userName.equals(loginReqVo.getUserName())) {
            return StatusEnum.ACCOUNT_NOT_MATCH;
        }
        Boolean loginStatus = userInfoCacheService.saveAndCheckLoginStatus(loginReqVo.getUserId());
        if(loginStatus == false) {
            return StatusEnum.REPEAT_LOGIN;
        }
        return StatusEnum.SUCCESS;
    }
}
