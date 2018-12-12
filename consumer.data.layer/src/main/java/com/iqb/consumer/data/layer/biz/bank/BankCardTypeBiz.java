/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午6:38:51
 * @version V1.0
 */
package com.iqb.consumer.data.layer.biz.bank;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.iqb.consumer.data.layer.bean.bank.BankCardTypeBean;
import com.iqb.consumer.data.layer.dao.BankCardTypeDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class BankCardTypeBiz extends BaseBiz {

    @Resource
    private BankCardTypeDao bankCardTypeDao;

    public List<BankCardTypeBean> getAllBankType() {
        setDb(0, super.SLAVE);
        return bankCardTypeDao.getAllBankType();
    }
}
