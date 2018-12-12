package com.iqb.consumer.data.layer.dao.carstatus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.carstatus.entity.InstManageCarInfoEntity;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.GPSInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;

public interface CarStatusRepository {

    List<CgetCarStatusInfoResponseMessage> cgetInfo(JSONObject requestMessage);

    List<CgetCarStatusInfoResponseMessage> cgetInfo2(JSONObject requestMessage);

    void updateIMCI(InstManageCarInfoEntity imci);

    int markIOIEByCarstatus(String orderId);

    void persisitImci(String orderId);

    /**
     * 
     * Description:根据订单号修改inst_managercar_info 信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    int updateManagerCarInfoByOrderId(Map<String, Object> params);

    /**
     * 
     * Description:根据订单号查询车辆状态跟踪回显信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    ManageCarInfoBean selectOrderInfoByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:根据订单号查询承租人回显信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月11日
     */
    ManageCarInfoBean selectSubleaseInfoByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:根据订单号修改状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月12日
     */
    int updateStatusByOrderId(Map<String, String> params);

    /**
     * 通过订单号查询状态
     * 
     * @param orderId
     * @return
     */
    int getCarStatusByOrderId(@Param("orderId") String orderId);

    /**
     * 通过订单号和procKey查询审批页面
     * 
     * @param orderId
     * @param procKey
     * @return
     */
    Map<String, String> getProcHtml(@Param("orderId") String orderId, @Param("procKey") String procKey);

    /**
     * 
     * Description:根据订单号查询以及GPS状态回显信息 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 11:31:59
     */
    CgetCarStatusInfoResponseMessage getOrderInfoAndGPSInfoByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:保存GPS状态到inst_gpsinfo表 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 14:47:18
     */
    void saveGPSInfo(JSONObject requestMessage);

    /**
     * 
     * Description:根据orderID查询展示inst_gpsinfo表数据 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 15:10:18
     */
    List<GPSInfo> selectGPSInfoListByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:车辆状态跟踪模块列表与查询接口 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 17:02:31
     */
    List<CgetCarStatusInfoResponseMessage> selectCarToGPSList(JSONObject requestMessage);

    /**
     * 
     * Description:根据订单号查询贷后车辆 信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    InstManageCarInfoEntity getCarStatusInfoByOrderId(String orderId);

    void updateRemindPhone(String orderId);
}
