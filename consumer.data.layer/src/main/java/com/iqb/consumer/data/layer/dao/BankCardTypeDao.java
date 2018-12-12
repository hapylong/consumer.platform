/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午6:31:37
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import com.iqb.consumer.data.layer.bean.bank.BankCardTypeBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface BankCardTypeDao {

    List<BankCardTypeBean> getAllBankType();

}
