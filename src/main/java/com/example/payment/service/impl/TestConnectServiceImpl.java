package com.example.payment.service.impl;

import com.example.payment.entity.Test;
import com.example.payment.mapper.TestConnectMapper;
import com.example.payment.service.TestConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestConnectServiceImpl implements TestConnectService {

    @Autowired
    private TestConnectMapper testConnectMapper;

    @Override
    public List<Test> selectAll() {
        return testConnectMapper.selectAll();
    }
}
