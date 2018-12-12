package com.iqb.consumer.data.layer.biz.trafficmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.data.layer.dao.trafficmanage.TrafficManageDao;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 车务Biz
 * 
 * @author chenyong
 * 
 */
@Repository
public class TrafficManageBiz extends BaseBiz {

    @Autowired
    private TrafficManageDao trafficManageDao;

    /**
     * 
     * 车务管理订单查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafficManageOrderList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return trafficManageDao.selectTrafficManageOrderList(objs);
    }

    /**
     * 
     * 车务管理订单查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Integer countTrafficManageOrder(JSONObject objs) {
        setDb(0, super.SLAVE);
        return trafficManageDao.countTrafficManageOrder(objs);
    }

    /**
     * 
     * 车务查询
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafficManageInfoList(JSONObject objs) {
        setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(objs));
        return trafficManageDao.selectTrafficManageInfoList(objs);
    }

    /**
     * 
     * 车务查询-不分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafficManageInfoListNoPage(JSONObject objs) {
        setDb(0, super.SLAVE);
        return trafficManageDao.selectTrafficManageInfoList(objs);
    }

    /**
     * 
     * 车务查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Integer countTrafficManageInfo(JSONObject objs) {
        setDb(0, super.SLAVE);
        return trafficManageDao.countTrafficManageInfo(objs);
    }

    /**
     * 
     * 车务详情
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Map<String, Object> getTrafficManageDetail(JSONObject objs) {
        setDb(0, super.SLAVE);
        return trafficManageDao.getTrafficManageDetail(objs);
    }

    /**
     * 
     * 补充资料历史记录
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public List<Map<String, Object>> selectTrafManaAdditionalhistory(JSONObject objs) {
        setDb(0, super.SLAVE);
        return trafficManageDao.selectTrafManaAdditionalhistory(objs);
    }

    /**
     * 
     * 保存补充资料
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int saveTrafficManageAdditional(Map<String, Object> map) {
        if (map == null || map.isEmpty() || map.get("orderId") == null)
            return 0;
        JSONObject parMap = new JSONObject();
        parMap.put("orderId", map.get("orderId"));
        parMap.put("checkStatus", 2);
        setDb(0, super.MASTER);
        Integer cnt = trafficManageDao.countAdditionalByCondition(parMap);
        if (cnt != null && cnt.intValue() > 0) {
            if (map.containsKey("registerDate"))
                map.remove("registerDate");
            if (map.containsKey("currentMaster"))
                map.remove("currentMaster");
            if (map.containsKey("color"))
                map.remove("color");
        }
        return trafficManageDao.saveTrafficManageAdditionalOnlyOne(map);
    }

    /**
     * 
     * 修改补充资料状态
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int updateAdditionalStatus(Map<String, Object> map) {

        if (map == null || map.isEmpty())
            return 0;

        String orderId = (String) map.get("orderId");
        String additionalNo = (String) map.get("additionalNo");
        String procBizId = (String) map.get("procBizId");

        if ((procBizId == null || procBizId.trim().equals("")) && (orderId == null || orderId.trim().equals("")
                || additionalNo == null || additionalNo.trim().equals("")))
            return 0;

        int checkStatus = 0;
        if (map.get("checkStatus") != null)
            try {
                checkStatus = Integer.parseInt(map.get("checkStatus").toString().trim());
            } catch (Exception e) {
                checkStatus = 0;
            }
        setDb(0, super.MASTER);
        int updRes = trafficManageDao.updateAdditionalStatus(map);
        if (checkStatus != 2)
            return updRes;

        JSONObject parMap = new JSONObject();
        if (procBizId == null || procBizId.trim().equals("")) {
            parMap.put("procBizId", map.get("procBizId"));
        } else {
            parMap.put("orderId", map.get("orderId"));
            parMap.put("additionalNo", map.get("additionalNo"));
        }
        Map<String, Object> detail = trafficManageDao.getTrafficManageDetail(parMap);
        if (detail != null && detail.get("checkStatus") != null
                && "2".equals(detail.get("checkStatus").toString().trim())
                && detail.get("procResult") != null && "1".equals(detail.get("procResult").toString().trim())) {
            Map<String, Object> mapTmp = new HashMap<String, Object>();
            mapTmp.put("orderId", detail.get("orderId"));
            mapTmp.put("color", detail.get("color"));
            mapTmp.put("registerDate", detail.get("registerDate"));
            mapTmp.put("currentMaster", detail.get("currentMaster"));
            trafficManageDao.saveTrafficManageExtendOnlyOne(mapTmp);
        }

        return updRes;
    }

    /**
     * 
     * 更新补充资料仅可更新审核中
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int updateTrafficManageAdditional(Map<String, Object> map) {
        setDb(0, super.MASTER);
        return trafficManageDao.updateTrafficManageAdditional(map);
    }

    /**
     * 
     * 启动补充资料工作流，调用此方法修改数据
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int startAdditionalWorkFlow(String procBizId, String orderId, String additionalNo) {
        if (orderId == null || orderId.trim().equals("")
                || additionalNo == null || additionalNo.trim().equals("")
                || procBizId == null || procBizId.trim().equals(""))
            return 0;
        setDb(0, super.MASTER);
        trafficManageDao.startAdditionalWorkFlow(procBizId, orderId, additionalNo);
        Map<String, Object> mapTmp = new HashMap<String, Object>();
        mapTmp.put("procBizId", procBizId);
        mapTmp.put("orderId", orderId);
        mapTmp.put("additionalNo", additionalNo);
        mapTmp.put("checkStatus", 1);
        Integer cnt = trafficManageDao.countAdditionalByCondition(mapTmp);
        if (cnt == null || cnt == 0)
            return 0;
        return 1;
    }

    /**
     * 
     * 根据订单号获取最大的流程业务ID
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public String selectOrderAdditionalMaxProcBizId(String orderId) {
        setDb(0, super.SLAVE);
        return trafficManageDao.selectOrderAdditionalMaxProcBizId(orderId);
    }

    /**
     * 
     * 删除资料表记录
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int delAdditional(String procBizId, String orderId
            , String additionalNo, Integer checkStatus) {
        setDb(0, super.MASTER);
        return trafficManageDao.delAdditional(procBizId, orderId, additionalNo, checkStatus);
    }

    /**
     * 
     * 根据条件count资料表记录数
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public Integer countAdditionalByCondition(Map<String, Object> map) {
        setDb(0, super.SLAVE);
        return trafficManageDao.countAdditionalByCondition(map);
    }

}
