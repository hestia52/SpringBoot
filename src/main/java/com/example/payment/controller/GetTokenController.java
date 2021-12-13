package com.example.payment.controller;

import com.example.payment.util.IdUtils;
import com.example.payment.util.RedisUtil;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/pay/token")
public class GetTokenController {

    private static final String JWT_PRIVATE_KEY = "JWT_PRIVATE_KEY_1225413895";

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 生成token&token存入redis  设置时效时间
     */
    @GetMapping("/getToken")
    public String getToken() {
        //生成随机数
        String token = IdUtils.fastUUID();
        //把token存入redis
        redisUtil.setCacheObject(token, "", 1800, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 生成token
     *
     * @param code
     * @param expire
     * @return
     * @throws Exception
     */
    public static String generateToken(String code, int expire) throws Exception {
        JwtClaims claims = new JwtClaims();
        claims.setSubject(code);
        claims.setExpirationTimeMinutesInTheFuture(expire == 0 ? 60 * 24 : expire);
        HmacKey key = new HmacKey(JWT_PRIVATE_KEY.getBytes("UTF-8"));

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(key);
        jws.setDoKeyValidation(false); // relaxes the key length requirement

        //签名
        String token = jws.getCompactSerialization();
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static String getInfoFromToken(String token) throws Exception {

        if (token == null) {
            return null;
        }

        HmacKey key = new HmacKey(JWT_PRIVATE_KEY.getBytes("UTF-8"));

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(30)
                .setRequireSubject()
                .setVerificationKey(key)
                .setRelaxVerificationKeyValidation() // relaxes key length requirement
                .build();

        JwtClaims processedClaims = jwtConsumer.processToClaims(token);

        return processedClaims.getSubject();
    }


    public static void main(String[] agars) throws Exception {
        String uuid = IdUtils.fastUUID();
        System.out.println(uuid);
        String token = generateToken(uuid, 0);
        System.out.println(token);
        String infoFromToken = getInfoFromToken(token);
        System.out.println(infoFromToken);

    }
}
