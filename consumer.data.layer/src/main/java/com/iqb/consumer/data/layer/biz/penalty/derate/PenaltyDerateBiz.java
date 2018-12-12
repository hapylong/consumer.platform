package com.iqb.consumer.data.layer.biz.penalty.derate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.penalty.derate.PenaltyDerateBean;
import com.iqb.consumer.data.layer.dao.PenaltyDerateDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 
 * Description: 罚息减免biz
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class PenaltyDerateBiz extends BaseBiz {

    @Autowired
    private PenaltyDerateDao penaltyDerateDao;

    /**
     * 
     * Description: 获取罚息减免申请列表
     * 
     * @param
     * @return List<PenaltyDerateBean>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午3:09:00
     */
    public List<PenaltyDerateBean> listPenaltyDerateApply(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return this.penaltyDerateDao.listPenaltyDerateApply(objs);
    }

    /**
     * 
     * Description: 校验保存请求参数数据
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午3:07:34
     */
    public PenaltyDerateBean getPenaltyDerateByInstallDetailId(String installDetailId) {
        super.setDb(0, super.SLAVE);
        return this.penaltyDerateDao.getPenaltyDerateByInstallDetailId(installDetailId);
    }

    /**
     * 
     * Description: 保存罚息减免申请
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午5:00:33
     */
    public Integer savePenaltyDeratable(PenaltyDerateBean penaltyDerateBean) {
        super.setDb(0, super.SLAVE);
        return this.penaltyDerateDao.savePenaltyDeratable(penaltyDerateBean);
    }

    /**
     * 
     * Description: 更新罚息减免申请状态
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午9:45:43
     */
    public Integer updatePenaltyDeratableApplyStatus(PenaltyDerateBean penaltyDerateBean) {
        super.setDb(0, super.MASTER);
        return this.penaltyDerateDao.updatePenaltyDeratableApplyStatus(penaltyDerateBean);
    }

    /**
     * 
     * Description: 根据id查询
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月15日 下午2:39:02
     */
    public PenaltyDerateBean getPenaltyDerateById(String id) {
        super.setDb(0, super.SLAVE);
        return this.penaltyDerateDao.getPenaltyDerateById(id);
    }

    /**
     * 
     * Description: 根据uuid获取罚息减免信息
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 下午3:03:45
     */
    public PenaltyDerateBean getPenaltyDerateByUUId(String uuid) {
        super.setDb(0, super.SLAVE);
        return this.penaltyDerateDao.getPenaltyDerateByUUId(uuid);
    }

    public PenaltyDerateBean getAmtAndMerName(String orderId) {
        super.setDb(0, super.SLAVE);
        return this.penaltyDerateDao.getAmtAndMerName(orderId);
    }

    public PenaltyDerateBean getByUUid(String uuid) {
        super.setDb(0, super.SLAVE);
        return this.penaltyDerateDao.getByUUid(uuid);
    }

    /**
     * 
     * Description:根据订单号查询申请状态为申请中的罚息订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月29日
     */
    public PenaltyDerateBean getPenaltyDerateBeanByOrderId(String orderId) {
        super.setDb(1, super.SLAVE);
        return this.penaltyDerateDao.getPenaltyDerateBeanByOrderId(orderId);
    }
}
