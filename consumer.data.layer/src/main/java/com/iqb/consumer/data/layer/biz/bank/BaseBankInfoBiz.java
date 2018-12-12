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

import com.iqb.consumer.data.layer.bean.bank.BaseBankInfoBean;
import com.iqb.consumer.data.layer.dao.BaseBankInfoDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class BaseBankInfoBiz extends BaseBiz {

    @Resource
    private BaseBankInfoDao baseBankInfoDao;

    /**
     * 查询可用银行卡信息
     * 
     * @return
     */
    public List<BaseBankInfoBean> getAllBankType() {
        setDb(0, super.SLAVE);
        return baseBankInfoDao.getAllBankType();
    }
}
