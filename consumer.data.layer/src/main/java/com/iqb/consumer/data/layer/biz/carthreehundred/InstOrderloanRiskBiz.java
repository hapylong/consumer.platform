package com.iqb.consumer.data.layer.biz.carthreehundred;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.data.layer.bean.carthreehundred.InstOrderloanRiskBean;
import com.iqb.consumer.data.layer.bean.carthreehundred.InstOrderloanRiskResultBean;
import com.iqb.consumer.data.layer.dao.carthreehundred.InstOrderloanRiskDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年8月14日下午6:04:53 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstOrderloanRiskBiz extends BaseBiz {

    @Autowired
    private InstOrderloanRiskDao instOrderloanRiskDao;

    /**
     * 
     * Description:保存发送车300接口贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    public int saveInstOrderloanRiskInfo(InstOrderloanRiskBean bean) {
        this.setDb(0, MASTER);
        return instOrderloanRiskDao.saveInstOrderloanRiskInfo(bean);
    }

    /**
     * 
     * Description:更新发送车300接口贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    public int updateInstOrderloanRiskInfoByOrderId(InstOrderloanRiskBean bean) {
        this.setDb(0, MASTER);
        return instOrderloanRiskDao.updateInstOrderloanRiskInfoByOrderId(bean);
    }

    /**
     * 
     * Description:根据订单号获取发送车300接口贷后风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月14日
     */
    public InstOrderloanRiskBean getInstOrderloanRiskInfoByOrderId(String orderId) {
        this.setDb(0, SLAVE);
        return instOrderloanRiskDao.getInstOrderloanRiskInfoByOrderId(orderId);
    }

    /**
     * 
     * Description:保存贷后风控返回结果信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月17日
     */
    public int insertInstLoanRiskResult(InstOrderloanRiskResultBean bean) {
        this.setDb(0, MASTER);
        return instOrderloanRiskDao.insertInstLoanRiskResult(bean);
    }
}
