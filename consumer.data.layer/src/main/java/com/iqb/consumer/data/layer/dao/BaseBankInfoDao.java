/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2016年12月15日 下午2:33:48
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.iqb.consumer.data.layer.bean.bank.BaseBankInfoBean;

/**
 * 基础卡信息Dao
 * 
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BaseBankInfoDao {

    /**
     * 查询可用银行卡信息
     * 
     * @return
     */
    List<BaseBankInfoBean> getAllBankType();
}
