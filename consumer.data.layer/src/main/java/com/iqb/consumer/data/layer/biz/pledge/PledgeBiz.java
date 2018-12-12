package com.iqb.consumer.data.layer.biz.pledge;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.ConditionsGetCarInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.UpdateAmtRequestPojo;
import com.iqb.consumer.data.layer.dao.PledgeDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 
 * Description: 质押车biz服务
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@Component
public class PledgeBiz extends BaseBiz {

    @Autowired
    private PledgeDao pledgeDao;

    /**
     * 
     * Description: 获取质押车订单信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月29日 下午5:48:48
     */
    public Map<String, Object> getPledgeInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return this.pledgeDao.getPledgeInfo(objs);
    }

    /**
     * 
     * Description: 保存车辆入库信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月30日 下午6:35:30
     */
    public Integer saveVehicleStorageInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return this.pledgeDao.saveVehicleStorageInfo(objs);
    }

    /**
     * 保存车辆是都有贷款
     * 
     * @param objs
     * @return
     */
    public Integer savePledgeInfo(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return this.pledgeDao.savePledgeInfo(objs);
    }

    public List<ConditionsGetCarInfoResponsePojo> cgetCarInfo(JSONObject requestMessage) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        List<ConditionsGetCarInfoResponsePojo> list = pledgeDao.cgetCarInfo(requestMessage);
        return list;
    }

    public void persistCarInfo(PledgeInfoBean pib) {
        super.setDb(0, super.MASTER);
        pledgeDao.persistCarInfo(pib);
    }

    public int updateCarInfo(PledgeInfoBean pib) {
        super.setDb(0, super.MASTER);
        return pledgeDao.updateCarInfo(pib);
    }

    public int updateAmt(UpdateAmtRequestPojo uarp) {
        super.setDb(0, super.MASTER);
        return pledgeDao.updateAmt(uarp);
    }

    /**
     * 更新质押车状态
     * 
     * 
     * @param objs
     * @param request
     * @return
     */
    public int updatePledgeStatus(PledgeInfoBean pib) {
        super.setDb(0, super.MASTER);
        return pledgeDao.updatePledgeStatus(pib);
    }
}
