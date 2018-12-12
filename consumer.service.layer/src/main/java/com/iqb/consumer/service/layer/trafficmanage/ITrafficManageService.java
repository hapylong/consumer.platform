package com.iqb.consumer.service.layer.trafficmanage;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

public interface ITrafficManageService {

    /**
     * 
     * 车务管理订单查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public PageInfo<Map<String, Object>> selectTrafficManageOrderList(JSONObject objs);

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
     * 车务查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public PageInfo<Map<String, Object>> selectTrafficManageInfoList(JSONObject objs);

    /**
     * 
     * 车务查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public String exportTrafficManageInfoList(JSONObject requestMessage,
            HttpServletResponse response);

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
     * 保存补充资料
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    public int saveTrafficManageAdditional(JSONObject objs);

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

}
