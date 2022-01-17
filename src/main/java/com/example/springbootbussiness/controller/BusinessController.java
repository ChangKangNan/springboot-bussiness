package com.example.springbootbussiness.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.example.springbootbussiness.service.BusinessService;
import com.example.springbootcommonservice.dto.BusinessDTO;
import com.example.springbootcommonservice.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: heshouyou
 * @Description Dubbo业务执行入口
 * @Date Created in 2019/1/14 17:15
 */
@RestController
@RequestMapping("/business/dubbo")
@Slf4j
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    @NacosValue(value = "${id:9}", autoRefreshed = true)
    Long id;

    @GetMapping("/k")
    Long getKids()
    {
        return id;
    }
    /**
     * 模拟用户购买商品下单业务逻辑流程
     *
     * @Param:
     * @Return:
     */
    @PostMapping("/buy")
    ObjectResponse handleBusiness(@RequestBody BusinessDTO businessDTO) {
        log.info("请求参数：{}", businessDTO.toString());
        return businessService.handleBusiness(businessDTO);
    }

    /**
     * 模拟用户购买商品下单业务逻辑流程
     *
     * @Param:
     * @Return:
     */
    @PostMapping("/buy2")
    ObjectResponse handleBusiness2(@RequestBody BusinessDTO businessDTO) {
        log.info("请求参数：{}", businessDTO.toString());
        return businessService.handleBusiness2(businessDTO);
    }
}
