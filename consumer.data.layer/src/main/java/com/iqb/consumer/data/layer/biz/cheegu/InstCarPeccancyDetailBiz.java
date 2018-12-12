package com.iqb.consumer.data.layer.biz.cheegu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyDetailBean;
import com.iqb.consumer.data.layer.dao.cheegu.InstCarPeccancyDetailDao;
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
 * 2018年5月28日上午10:56:03 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstCarPeccancyDetailBiz extends BaseBiz {
    @Autowired
    private InstCarPeccancyDetailDao instCarPeccancyDetailDao;

    /**
     * 
     * Description:根据订单号 车架号查询车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public List<InstCarPeccancyDetailBean> selectInstCarPeccancyDetailList(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        return instCarPeccancyDetailDao.selectInstCarPeccancyDetailList(objs);
    }

    /**
     * 
     * Description:根据订单号 车架号修改违章明细处理状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int updateInstCarPeccancyDetailStatusByOrderId(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return instCarPeccancyDetailDao.updateInstCarPeccancyDetailStatusByOrderId(objs);
    }

    /**
     * 
     * Description:批量插入车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int insertInstCarPeccancyDetail(InstCarPeccancyDetailBean bean) {
        super.setDb(0, super.MASTER);
        return instCarPeccancyDetailDao.insertInstCarPeccancyDetail(bean);
    }

    /**
     * 
     * Description:根据订单号车架号查询车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    public InstCarPeccancyDetailBean selectInstCarPeccancyDetailInfo(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return instCarPeccancyDetailDao.selectInstCarPeccancyDetailInfo(objs);
    }

    /**
     * 
     * Description:根据订单号 车架号 查询上月未处理的违章信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    public List<InstCarPeccancyDetailBean> selectInstCarDetailList(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return instCarPeccancyDetailDao.selectInstCarDetailList(objs);
    }

    /**
     * 
     * Description:根据订单号 车价号查询违章总扣分 总罚款金额
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    public InstCarPeccancyBean selectInstCarDetailSum(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return instCarPeccancyDetailDao.selectInstCarDetailSum(objs);
    }
}
