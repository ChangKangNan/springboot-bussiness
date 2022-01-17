package com.example.springbootbussiness.service.impl;

import com.example.springbootbussiness.mapper.BussinessMapper;
import com.example.springbootbussiness.service.BusinessService;
import com.example.springbootcommonservice.dto.BusinessDTO;
import com.example.springbootcommonservice.dto.CommodityDTO;
import com.example.springbootcommonservice.dto.OrderDTO;
import com.example.springbootcommonservice.dubbo.AccountDubboService;
import com.example.springbootcommonservice.dubbo.OrderDubboService;
import com.example.springbootcommonservice.dubbo.StockDubboService;
import com.example.springbootcommonservice.enums.RspStatusEnum;
import com.example.springbootcommonservice.exception.DefaultException;
import com.example.springbootcommonservice.response.ObjectResponse;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: lidong
 * @Description Dubbo业务发起方逻辑
 * @Date Created in 2019/9/5 18:36
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

    @DubboReference
    private StockDubboService stockDubboService;

    @DubboReference
    private OrderDubboService orderDubboService;

    @DubboReference
    private AccountDubboService accountDubboService;

    @Autowired
    BussinessMapper bussinessMapper;

    boolean flag;

    /**
     * 处理业务逻辑 正常的业务逻辑
     *
     * @Param:
     * @Return:
     * @return
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public ObjectResponse handleBusiness(BusinessDTO businessDTO) {
        log.info("开始全局事务，XID = " + RootContext.getXID());
        ObjectResponse<Object> objectResponse = new ObjectResponse<>();
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        ObjectResponse stockResponse = stockDubboService.decreaseStock(commodityDTO);
        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        ObjectResponse<OrderDTO> response = orderDubboService.createOrder(orderDTO);

        bussinessMapper.updateStock();

        bussinessMapper.updateStock1();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    /*    try {
            throw new DefaultException(RspStatusEnum.FAIL);
        }catch (Exception e){

        }*/

        if (stockResponse.getStatus() != 200 || response.getStatus() != 200) {
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        objectResponse.setStatus(RspStatusEnum.SUCCESS.getCode());
        objectResponse.setMessage(RspStatusEnum.SUCCESS.getMessage());
        objectResponse.setData(response.getData());
        return objectResponse;
    }

    /**
     * 出处理业务服务，出现异常回顾
     *
     * @param businessDTO
     * @return
     */
    @GlobalTransactional(timeoutMills = 300000, name = "dubbo-gts-seata-example")
    @Override
    public ObjectResponse handleBusiness2(BusinessDTO businessDTO) {
        log.info("开始全局事务，XID = " + RootContext.getXID());
        ObjectResponse<Object> objectResponse = new ObjectResponse<>();
        //1、扣减库存
        CommodityDTO commodityDTO = new CommodityDTO();
        commodityDTO.setCommodityCode(businessDTO.getCommodityCode());
        commodityDTO.setCount(businessDTO.getCount());
        ObjectResponse stockResponse = stockDubboService.decreaseStock(commodityDTO);

        //2、创建订单
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(businessDTO.getUserId());
        orderDTO.setCommodityCode(businessDTO.getCommodityCode());
        orderDTO.setOrderCount(businessDTO.getCount());
        orderDTO.setOrderAmount(businessDTO.getAmount());
        ObjectResponse<OrderDTO> response = orderDubboService.createOrder(orderDTO);

        //        打开注释测试事务发生异常后，全局回滚功能
        if (!flag) {
            throw new RuntimeException("测试抛异常后，分布式事务回滚！");
        }

        if (stockResponse.getStatus() != 200 || response.getStatus() != 200) {
            throw new DefaultException(RspStatusEnum.FAIL);
        }

        objectResponse.setStatus(RspStatusEnum.SUCCESS.getCode());
        objectResponse.setMessage(RspStatusEnum.SUCCESS.getMessage());
        objectResponse.setData(response.getData());
        return objectResponse;
    }
}
