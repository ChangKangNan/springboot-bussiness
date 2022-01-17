package com.example.springbootbussiness.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 创建订单
 * </p>
 *
 * * @author lidong
 *
 * @since 2019-09-04
 */
@Mapper
public interface BussinessMapper {
    @Update("update t_order set amount=20 where id=65")
    void updateStock();

    @Update("update t_order set amount=21 where id=65")
    void updateStock1();
}
