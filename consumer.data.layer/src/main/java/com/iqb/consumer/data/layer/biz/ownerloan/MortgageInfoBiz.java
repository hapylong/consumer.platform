package com.iqb.consumer.data.layer.biz.ownerloan;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.ownerloan.DeptSignInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.MortgageInfoBean;
import com.iqb.consumer.data.layer.bean.ownerloan.OwnerLoanBaseInfoBean;
import com.iqb.consumer.data.layer.dao.ownerloan.MortgageInfoDao;
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
 * 2017年11月10日上午10:48:36 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class MortgageInfoBiz extends BaseBiz {
    @Resource
    private MortgageInfoDao mortgageInfoDao;

    /**
     * 
     * Description:车主贷-保存车辆抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    public int insertMortgageInfo(MortgageInfoBean mortgageInfoBean) {
        setDb(0, super.MASTER);
        return mortgageInfoDao.insertMortgageInfo(mortgageInfoBean);
    }

    /**
     * 
     * Description:车主贷-更新车辆抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    public int updateMortgageInfo(JSONObject objs) {
        setDb(0, super.MASTER);
        return mortgageInfoDao.updateMortgageInfo(objs);
    }

    /**
     * 
     * Description:查询车辆抵押 风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月10日
     */
    public MortgageInfoBean selectOneByOrderId(JSONObject objs) {
        setDb(1, super.MASTER);
        return mortgageInfoDao.selectOneByOrderId(objs);
    }

    /**
     * 
     * Description:车主贷-获取订单 人员 卡信息接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月13日
     */
    public OwnerLoanBaseInfoBean getBaseInfo(JSONObject objs) {
        setDb(1, super.MASTER);
        return mortgageInfoDao.getBaseInfo(objs);
    }

    /**
     * 
     * Description:获取车辆以及抵押信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月15日
     */
    public MortgageInfoBean getCarinfo(JSONObject objs) {
        setDb(1, super.MASTER);
        return mortgageInfoDao.getCarinfo(objs);
    }

    /**
     * 
     * Description:获取门店签约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    public DeptSignInfoBean getDeptSignInfo(JSONObject objs) {
        setDb(1, super.MASTER);
        return mortgageInfoDao.getDeptSignInfo(objs);
    }

    /**
     * 
     * Description:获取放款确认信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月16日
     */
    public DeptSignInfoBean getLoanInfo(JSONObject objs) {
        setDb(1, super.MASTER);
        return mortgageInfoDao.getLoanInfo(objs);
    }
}
