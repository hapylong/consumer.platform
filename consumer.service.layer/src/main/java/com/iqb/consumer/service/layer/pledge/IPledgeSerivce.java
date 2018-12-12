package com.iqb.consumer.service.layer.pledge;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.Pledge.PledgeInfoBean;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.ConditionsGetCarInfoResponsePojo;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 质押车服务接口
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
public interface IPledgeSerivce {

    /**
     * 
     * Description: 获取质押车订单信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月29日 下午2:58:17
     */
    public Object getPledgeInfo(JSONObject objs) throws IqbException;

    /**
     * 
     * Description: 更新质押车订单状态
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月30日 下午4:10:20
     */
    public Integer updatePledgeOrderByWF(OrderBean orderBean) throws IqbException;

    /**
     * 
     * Description: 处理风控审批节点通过业务逻辑处理
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月30日 下午5:03:33
     */
    public void dealRiskWFReturn(String procBizId) throws IqbException;

    /**
     * 
     * Description: 保存车辆入库信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月30日 下午6:34:47
     */
    public Integer saveVehicleStorageInfo(JSONObject objs) throws IqbException;

    /**
     * 保存车辆是否有贷款
     * 
     * @param objs
     * @return
     */
    public Integer savePledgeInfo(JSONObject objs);

    public PageInfo<ConditionsGetCarInfoResponsePojo> cgetCarInfo(JSONObject requestMessage) throws GenerallyException;

    public void persistCarInfo(JSONObject requestMessage) throws GenerallyException;

    public void updateCarInfo(JSONObject requestMessage) throws GenerallyException;

    public void updateAmt(JSONObject requestMessage) throws GenerallyException;

    /**
     * 更新质押车状态
     * 
     * 
     * @param objs
     * @param request
     * @return
     */
    public int updatePledgeStatus(PledgeInfoBean pib);
}
