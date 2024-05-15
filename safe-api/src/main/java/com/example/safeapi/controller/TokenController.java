package com.example.safeapi.controller;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: LinZiYu
 * \* Date: 2020/6/3
 * \* Time: 15:48
 * \* Description:
 * \
 */

import com.example.safeapi.token.*;
import com.example.safeapi.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/api/token")
public class TokenController {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * API Token
     *
     * @param sign
     * @return
     */
    @PostMapping("/api_token")
    public ApiResponse<AccessToken> apiToken(String appId, @RequestHeader("timestamp") String timestamp, @RequestHeader("sign") String sign) {
        Assert.isTrue(!StringUtils.isEmpty(appId) && !StringUtils.isEmpty(timestamp) && !StringUtils.isEmpty(sign), "参数错误");
        log.info(appId +"    "  +timestamp +"    " + sign);
        long reqeustInterval = System.currentTimeMillis() - Long.valueOf(timestamp);
//        Assert.isTrue(reqeustInterval < 5 * 60 * 100000000, "请求过期，请重新请求");

        // 1. 根据appId查询数据库获取appSecret
        AppInfo appInfo = new AppInfo("1", "12345678954556");

        // 2. 校验签名
        String signString = timestamp + appId + appInfo.getKey();
        String signature = MD5Util.encode(signString);
        log.info(signature);
        Assert.isTrue(signature.equals(sign), "签名错误");

        // 3. 如果正确生成一个token保存到redis中，如果错误返回错误信息
        AccessToken accessToken = this.saveToken(0, appInfo, null);
        return ApiResponse.success(accessToken);
    }


    @NotRepeatSubmit(50000)
    @PostMapping("user_token")
    public ApiResponse<UserInfo> userToken(String username, String password) {
        // 根据用户名查询密码, 并比较密码(密码可以RSA加密一下)
        UserInfo userInfo = new UserInfo(username, "81255cb0dca1a5f304328a70ac85dcbd", "111111");
        String pwd = password + userInfo.getSalt();
        String passwordMD5 = MD5Util.encode(pwd);
        Assert.isTrue(passwordMD5.equals(userInfo.getPassword()), "密码错误");

        // 2. 保存Token
        AppInfo appInfo = new AppInfo("1", "12345678954556");
        AccessToken accessToken = this.saveToken(1, appInfo, userInfo);
        userInfo.setAccessToken(accessToken);
        return ApiResponse.success(userInfo);
    }

    private AccessToken saveToken(int tokenType, AppInfo appInfo,  UserInfo userInfo) {
        String token = UUID.randomUUID().toString();
        // token有效期为2小时
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 7200);
        Date expireTime = calendar.getTime();

        // 4. 保存token
        ValueOperations<String, TokenInfo> operations = redisTemplate.opsForValue();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTokenType(tokenType);
        tokenInfo.setAppInfo(appInfo);

        if (tokenType == 1) {
            tokenInfo.setUserInfo(userInfo);
        }

        operations.set(token, tokenInfo, 7200, TimeUnit.SECONDS);

        AccessToken accessToken = new AccessToken(token, expireTime);

        return accessToken;
    }

    public static void main(String[] args) {

        long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);

        String appid = "1";
        String appKey = "12345678954556";
        String signString = timestamp + appid + appKey;
        String sign = MD5Util.encode(signString);

        System.out.println(sign);


        String token = "86b59bad-71b9-4706-8642-68b61417d007";
        String nonce = "A1scr6";
//        String timestamps = "1680702174260";
//        signString = "password=123456&username=1&12345678954556" +  token + timestamps + nonce;
        String username="1";
        String password = "123456";
        signString = "password="+password+"&username="+username+"&"+appKey+token+timestamp+nonce;

        sign = MD5Util.encode(signString);


        System.out.println("登录接口------------------- ");
        System.out.println("     sign: "+sign);
        System.out.println("    token: "+token);
        System.out.println("    nonce: "+nonce);
        System.out.println("timestamp: "+timestamp);

        System.out.println("需要登录后，但是不需要传参的相关接口 -------------------");
        String s = appKey + token + timestamp + nonce;
        System.out.println("     sign: "+MD5Util.encode(s));
        System.out.println("    token: "+token);
        System.out.println("    nonce: "+nonce);
        System.out.println("timestamp: "+timestamp);

    }


    /*
    1680688706858
d39c61e5bcc03d1ec344b35b25eecc56
-------------------
6f7db60613e9d9d868731375d3079232
    * */


/*
1591172901666
1591172999
1591173075453


347aad7d9029195b69b8bf6d057bf4a5
-------------------
1668be862b9ac1f03c54515763481e7d


password=123456&username=1&123456789545560a2b4a1d-44e1-4042-82eb-a8da49cc76641680703993973A1scr6
password=123456&username=1123456789545560a2b4a1d-44e1-4042-82eb-a8da49cc76641680704200586A1scr6

password=123456&username=1&123456789545560a2b4a1d-44e1-4042-82eb-a8da49cc76641680704775242A1scr6
password=123456&username=1&1234567895455604c3fc75-dd29-4bd7-baf7-e672c9c401f91680704775242A1scr6


password=123456&username=1&123456789545560a2b4a1d-44e1-4042-82eb-a8da49cc76641680704985828A1scr6
password=123456&username=1&1234567895455604c3fc75-dd29-4bd7-baf7-e672c9c401f91680704985828A1scr6

 */
}
