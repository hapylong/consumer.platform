package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.ConditionsGetCarInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.UpdateAmtRequestPojo;

/**
 * 
 * Description: 抵押车dao服务
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
public interface PledgeDao {

    /**
     * 
     * Description: 获取抵押车订单信息
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月29日 下午5:50:05
     */
    public Map<String, Object> getPledgeInfo(JSONObject objs);

    /**
     * 
     * Description: 保存车辆入库信息
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月30日 下午6:36:01
     */
    public Integer saveVehicleStorageInfo(JSONObject objs);

    /**
     * 保存车辆是都有贷款
     * 
     * @param objs
     * @return
     */
    public Integer savePledgeInfo(JSONObject objs);

    public List<ConditionsGetCarInfoResponsePojo> cgetCarInfo(JSONObject requestMessage);

    public void persistCarInfo(PledgeInfoBean pib);

    public int updateCarInfo(PledgeInfoBean pib);

    public int updateAmt(UpdateAmtRequestPojo uarp);

    /**
     * 更新质押车状态
     * 
     * @param objs
     * @param request
     * @return
     */
    public int updatePledgeStatus(PledgeInfoBean pib);

}
