package com.example.payment.controller;

import com.example.payment.entity.Test;
import com.example.payment.service.TestConnectService;
import com.example.payment.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test/connert")
public class TestConnectController {

    @Autowired
    private TestConnectService testConnectService;

    @Autowired
    private RedisUtil redis;

    @GetMapping("/list")
    public List<Test> list(){
        return testConnectService.selectAll();
    }

    @GetMapping("/redis")
    public String testResdis(){
        redis.setCacheObject("key", "oldInfo", 1800, TimeUnit.SECONDS);
        return "ss";
    }

}
