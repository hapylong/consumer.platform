package com.iqb.consumer.data.layer.biz.afterLoan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawResultBean;
import com.iqb.consumer.data.layer.dao.afterLoan.InstOrderLawResultDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * Description:
 * 
 * @author chenyong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年7月18日11:14:24 	chenyong   	1.0        	1.0 Version 
 * </pre>
 */
@Repository
public class InstOrderLawResultBiz extends BaseBiz {

    @Autowired
    private InstOrderLawResultDao instOrderLawResultDao;

    /**
     * 
     * 保存案件庭审结果
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public int saveInstOrderLawResult(JSONObject objs) {

        InstOrderLawResultBean lawResult = new InstOrderLawResultBean();
        lawResult.setCaseResult(objs.getString("caseResult"));
        lawResult.setOrderId(objs.getString("orderId"));
        lawResult.setExecuteFlag(objs.getInteger("executeFlag"));
        lawResult.setCourtRemark(objs.getString("courtRemark"));
        lawResult.setOperatorRegId(objs.getString("operatorRegId"));
        lawResult.setCaseId(objs.getString("caseId"));
        setDb(0, super.MASTER);
        return instOrderLawResultDao.saveInstOrderLawResult(lawResult);
    }

    /**
     * 
     * 更新案件庭审结果
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public int updateInstOrderLawResult(JSONObject objs) {

        InstOrderLawResultBean lawResult = new InstOrderLawResultBean();
        lawResult.setCaseResult(objs.getString("caseResult"));
        lawResult.setOrderId(objs.getString("orderId"));
        lawResult.setExecuteFlag(objs.getInteger("executeFlag"));
        lawResult.setCourtRemark(objs.getString("courtRemark"));
        lawResult.setOperatorRegId(objs.getString("operatorRegId"));
        lawResult.setId(objs.getInteger("id"));

        setDb(0, super.MASTER);
        return instOrderLawResultDao.updateInstOrderLawResult(lawResult);
    }

    /**
     * 
     * 根据订单号查询案件庭审结果
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public InstOrderLawResultBean getInstOrderLawResultByOrderId(JSONObject objs) {
        setDb(0, super.SLAVE);
        return instOrderLawResultDao.getInstOrderLawResultByOrderId(objs.getString("orderId"));
    }

    /**
     * 
     * 根据订单号查询案件庭审结果列表
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<InstOrderLawResultBean> selectInstOrderLawResultByOrderId(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return instOrderLawResultDao.selectInstOrderLawResultByOrderId(objs.getString("caseId"));
    }
}
