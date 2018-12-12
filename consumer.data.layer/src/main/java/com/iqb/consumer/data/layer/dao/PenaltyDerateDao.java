package com.iqb.consumer.data.layer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.penalty.derate.PenaltyDerateBean;

/**
 * 
 * Description: 罚息减免dao
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
public interface PenaltyDerateDao {

    /**
     * 
     * Description: 获取罚息减免申请列表
     * 
     * @param
     * @return List<PenaltyDerateBean>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午3:13:15
     */
    public List<PenaltyDerateBean> listPenaltyDerateApply(JSONObject objs);

    /**
     * 
     * Description: 校验保存请求参数数据
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午3:26:48
     */
    public PenaltyDerateBean getPenaltyDerateByInstallDetailId(String installDetailId);

    /**
     * 
     * Description: 保存罚息减免申请
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午5:00:51
     */
    public Integer savePenaltyDeratable(PenaltyDerateBean penaltyDerateBean);

    /**
     * 
     * Description: 更新罚息减免申请状态
     * 
     * @param
     * @return Integer
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午9:46:14
     */
    public Integer updatePenaltyDeratableApplyStatus(PenaltyDerateBean penaltyDerateBean);

    /**
     * 
     * Description: 根据id查询
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月15日 下午2:43:12
     */
    public PenaltyDerateBean getPenaltyDerateById(String id);

    /**
     * 
     * Description: 根据uuid获取罚息减免信息
     * 
     * @param
     * @return PenaltyDerateBean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 下午3:04:08
     */
    public PenaltyDerateBean getPenaltyDerateByUUId(String uuid);

    public PenaltyDerateBean getAmtAndMerName(String orderId);

    public PenaltyDerateBean getByUUid(@Param("uuid") String uuid);

    /**
     * 
     * Description:根据订单号查询申请状态为申请中的罚息订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月29日
     */
    public PenaltyDerateBean getPenaltyDerateBeanByOrderId(String orderId);

}
