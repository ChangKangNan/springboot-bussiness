package com.example.springbootbussiness.service;


import com.example.springbootcommonservice.dto.BusinessDTO;
import com.example.springbootcommonservice.response.ObjectResponse;

/**
 * @Author: lidong
 * @Description
 * @Date Created in 2019/9/5 17:17
 */
public interface BusinessService {

    /**
     * 出处理业务服务
     *
     * @param businessDTO
     * @return
     */
    ObjectResponse handleBusiness(BusinessDTO businessDTO);

    /**
     * 出处理业务服务，出现异常回顾
     *
     * @param businessDTO
     * @return
     */
    ObjectResponse handleBusiness2(BusinessDTO businessDTO);
}
