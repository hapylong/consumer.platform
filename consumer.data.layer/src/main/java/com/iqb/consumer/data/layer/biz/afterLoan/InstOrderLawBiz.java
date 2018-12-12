package com.iqb.consumer.data.layer.biz.afterLoan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.afterLoan.InstOrderLawBean;
import com.iqb.consumer.data.layer.dao.afterLoan.InstOrderLawDao;
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
 * 2018年7月13日下午2:52:24 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Repository
public class InstOrderLawBiz extends BaseBiz {
    @Autowired
    private InstOrderLawDao instOrderLawDao;

    /**
     * 
     * 保存案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    public int saveInstOrderLawnInfo(JSONObject objs) {
        setDb(0, super.MASTER);
        return instOrderLawDao.saveInstOrderLawnInfo(objs);
    }

    /**
     * 
     * 更新案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    public int updateInstOrderLawnInfo(JSONObject objs) {
        setDb(0, super.MASTER);
        return instOrderLawDao.updateInstOrderLawnInfo(objs);
    }

    /**
     * 
     * 根据caseId查询案件登记资料
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    public InstOrderLawBean getInstOrderLawnInfoByCaseId(JSONObject objs) {
        setDb(0, super.SLAVE);
        return instOrderLawDao.getInstOrderLawnInfoByCaseId(objs);
    }

    /**
     * 
     * 根据订单号查询案件登记资料列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月13日
     */
    public List<InstOrderLawBean> selectInstOrderLawnInfoByOrderId(JSONObject objs) {
        setDb(0, super.SLAVE);
        return instOrderLawDao.selectInstOrderLawnInfoByOrderId(objs);
    }

    /**
     * 
     * 根据caseNo查询案件信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月30日
     */
    public List<InstOrderLawBean> selectInstOrderLawnInfoByCaseNo(JSONObject objs) {
        setDb(0, super.SLAVE);
        return instOrderLawDao.selectInstOrderLawnInfoByCaseNo(objs);
    }
}
