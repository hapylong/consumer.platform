package com.iqb.consumer.data.layer.biz.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.jys.ExchangeData;
import com.iqb.consumer.data.layer.dao.jys.ExchangeDataDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 交易所业务处理Biz
 * 
 * @author Yeoman
 * 
 */
@Component
public class ExchangeDataBiz extends BaseBiz {

    @Resource
    private ExchangeDataDao exchangeDataDao;

    /**
     * 保存咨询范信息
     * 
     * @param exchangeData
     * @return
     */
    public long saveExchangeData(ExchangeData exchangeData) {
        setDb(0, super.MASTER);
        exchangeDataDao.saveExchangeData(exchangeData);
        return exchangeData.getId();
    }

    /**
     * 通过挂牌资产编号查询
     * 
     * @param listNumber
     * @return
     */
    public ExchangeData selExchangeData(String listNumber) {
        setDb(0, super.SLAVE);
        return exchangeDataDao.selExchangeData(listNumber);
    }

}
