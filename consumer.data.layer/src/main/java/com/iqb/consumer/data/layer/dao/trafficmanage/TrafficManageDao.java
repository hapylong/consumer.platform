package com.iqb.consumer.data.layer.dao.trafficmanage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;

public interface TrafficManageDao {

    /**
     * 
     * 车务管理订单查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafficManageOrderList(JSONObject objs);

    /**
     * 
     * 车务管理订单查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Integer countTrafficManageOrder(JSONObject objs);

    /**
     * 
     * 车务查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafficManageInfoList(JSONObject objs);

    /**
     * 
     * 车务查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Integer countTrafficManageInfo(JSONObject objs);

    /**
     * 
     * 车务详情
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Map<String, Object> getTrafficManageDetail(JSONObject objs);

    /**
     * 
     * 补充资料历史记录
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafManaAdditionalhistory(JSONObject objs);

    /**
     * 
     * 保存车务扩展信息
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int saveTrafficManageExtend(Map<String, Object> map);

    /**
     * 
     * 保存车务扩展信息-只保存一次
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int saveTrafficManageExtendOnlyOne(Map<String, Object> map);

    /**
     * 
     * 保存补充资料
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int saveTrafficManageAdditional(Map<String, Object> map);

    /**
     * 
     * 保存补充资料-一个订单同一时间只能有一个未审批完成的补充资料，且补充资料编号不可重复
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int saveTrafficManageAdditionalOnlyOne(Map<String, Object> map);

    /**
     * 
     * 修改补充资料状态
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int updateAdditionalStatus(Map<String, Object> map);

    /**
     * 
     * 更新补充资料仅可更新审核中
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int updateTrafficManageAdditional(Map<String, Object> map);

    /**
     * 
     * 启动补充资料工作流，调用此方法修改数据
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int startAdditionalWorkFlow(@Param("procBizId") String procBizId,
            @Param("orderId") String orderId, @Param("additionalNo") String additionalNo);

    /**
     * 
     * 根据订单号获取最大的流程业务ID
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public String selectOrderAdditionalMaxProcBizId(@Param("orderId") String orderId);

    /**
     * 
     * 根据条件count资料表记录数
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Integer countAdditionalByCondition(Map<String, Object> map);

    /**
     * 
     * 删除资料表记录
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int delAdditional(@Param("procBizId") String procBizId, @Param("orderId") String orderId
            , @Param("additionalNo") String additionalNo, @Param("checkStatus") Integer checkStatus);

}
