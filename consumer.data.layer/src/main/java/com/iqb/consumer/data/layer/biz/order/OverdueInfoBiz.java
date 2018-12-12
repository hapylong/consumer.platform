package com.iqb.consumer.data.layer.biz.order;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.order.ArchivesBean;
import com.iqb.consumer.data.layer.bean.order.OverdueInfoBean;
import com.iqb.consumer.data.layer.dao.order.OverdueInfoDao;
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
 * 2018年3月16日上午11:56:30 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class OverdueInfoBiz extends BaseBiz {
    @Resource
    private OverdueInfoDao overdueInfoDao;

    /**
     * 
     * Description:客户履约保证金结算分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public List<OverdueInfoBean> selectOverdueInfoSettlementList(JSONObject objs) {
        setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return overdueInfoDao.selectOverdueInfoSettlementList(objs);
    }

    /**
     * 
     * Description:客户履约保证金结算导出查询
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年3月22日 16:03:02
     */
    public List<OverdueInfoBean> selectOverdueInfoSettlementList1(JSONObject objs) {
        setDb(1, super.MASTER);
        return overdueInfoDao.selectOverdueInfoSettlementList(objs);
    }

    /**
     * 查询订单是否已经发起流程
     * 
     * @param objs
     * @return
     */
    public int selOrderCount(JSONObject objs) {
        setDb(0, super.SLAVE);
        return overdueInfoDao.selOrderCount(objs);
    }

    /**
     * 
     * Description:根据条件查询单条违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public OverdueInfoBean selectOverdueInfo(JSONObject objs) {
        setDb(1, super.MASTER);
        return overdueInfoDao.selectOverdueInfo(objs);
    }

    /**
     * 
     * Description:插入违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public int insertOverdueInfo(OverdueInfoBean overdueInfoBean) {
        setDb(0, super.MASTER);
        return overdueInfoDao.insertOverdueInfo(overdueInfoBean);
    }

    /**
     * 
     * Description:更新违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public int updateOverdueInfo(OverdueInfoBean overdueInfoBean) {
        setDb(0, super.MASTER);
        return overdueInfoDao.updateOverdueInfo(overdueInfoBean);
    }

    /**
     * 
     * Description:根据batchId更新违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    public int updateOverdueInfoByBatchId(OverdueInfoBean overdueInfoBean) {
        setDb(0, super.MASTER);
        return overdueInfoDao.updateOverdueInfoByBatchId(overdueInfoBean);
    }

    /**
     * 
     * Description:根据批次号查询总结算金额、总保证金、总笔数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月19日
     */
    public OverdueInfoBean getOverdueInfoByBatchId(JSONObject objs) {
        setDb(1, super.MASTER);
        return overdueInfoDao.getOverdueInfoByBatchId(objs);
    }

    /**
     * 
     * Description:档案查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月26日
     */
    public List<ArchivesBean> queryArchivesList(JSONObject objs) {
        setDb(1, super.MASTER);
        PageHelper.startPage(getPagePara(objs));
        return overdueInfoDao.queryArchivesList(objs);
    }
}
