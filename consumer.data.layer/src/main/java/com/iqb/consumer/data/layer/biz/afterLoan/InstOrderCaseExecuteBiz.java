package com.iqb.consumer.data.layer.biz.afterLoan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderCaseExecuteBean;
import com.iqb.consumer.data.layer.dao.afterLoan.InstOrderCaseExecuteDao;
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
public class InstOrderCaseExecuteBiz extends BaseBiz {

    @Autowired
    private InstOrderCaseExecuteDao instOrderCaseExecuteDao;

    /**
     * 
     * 保存案件执行结果
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public int saveInstOrderCaseExecute(JSONObject objs) {

        InstOrderCaseExecuteBean caseExecute = new InstOrderCaseExecuteBean();
        caseExecute.setOrderId(objs.getString("orderId"));
        caseExecute.setExecuteResult(objs.getInteger("executeResult"));
        caseExecute.setExecuteRemark(objs.getString("executeRemark"));
        caseExecute.setOperatorRegId(objs.getString("operatorRegId"));
        caseExecute.setCaseId(objs.getString("caseId"));
        setDb(0, super.MASTER);
        return instOrderCaseExecuteDao.saveInstOrderCaseExecute(caseExecute);
    }

    /**
     * 
     * 更新案件执行结果
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public int updateInstOrderCaseExecute(JSONObject objs) {

        InstOrderCaseExecuteBean caseExecute = new InstOrderCaseExecuteBean();
        caseExecute.setOrderId(objs.getString("orderId"));
        caseExecute.setExecuteResult(objs.getInteger("executeResult"));
        caseExecute.setExecuteRemark(objs.getString("executeRemark"));
        caseExecute.setOperatorRegId(objs.getString("operatorRegId"));
        caseExecute.setId(objs.getInteger("id"));

        setDb(0, super.MASTER);
        return instOrderCaseExecuteDao.updateInstOrderCaseExecute(caseExecute);
    }

    /**
     * 
     * 根据订单号查询最后一次案件执行结果
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public InstOrderCaseExecuteBean getInstOrderCaseExecuteByOrderId(JSONObject objs) {
        setDb(0, super.SLAVE);
        return instOrderCaseExecuteDao.getInstOrderCaseExecuteByOrderId(objs.getString("orderId"));
    }

    /**
     * 
     * 根据订单号查询案件执行结果列表-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年7月18日
     */
    public List<InstOrderCaseExecuteBean> selectInstOrderCaseExecuteByCaseId(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return instOrderCaseExecuteDao.selectInstOrderCaseExecuteByCaseId(objs.getString("caseId"));
    }
}
