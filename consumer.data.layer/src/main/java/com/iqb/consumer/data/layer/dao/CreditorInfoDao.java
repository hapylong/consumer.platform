package com.iqb.consumer.data.layer.dao;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.creditorinfo.CreditorInfoBean;

public interface CreditorInfoDao {

    int insertCreditorInfo(CreditorInfoBean creditorInfoBean);

    CreditorInfoBean selectOneByOrderId(@Param("orderId") String orderId);

    int updateCreditorInfo(CreditorInfoBean creditorInfoBean);

}
