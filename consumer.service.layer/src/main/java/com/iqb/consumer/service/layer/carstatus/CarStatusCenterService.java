package com.iqb.consumer.service.layer.carstatus;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.GPSInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;

public interface CarStatusCenterService {
    /**
     * 
     * Description: FINANCE-1439 车辆状态查询FINANCE-1472 车辆状态跟踪/贷后处置/车辆出库/车辆状态
     * 
     * @param
     * @return CgetCarStatusInfoResponseMessage
     * @throws GenerallyException
     * @throws @Author adam Create Date: 2017年7月10日 下午2:31:33
     */
    PageInfo<CgetCarStatusInfoResponseMessage> cgetInfoList(JSONObject requestMessage) throws GenerallyException;

    CgetCarStatusInfoResponseMessage cgetInfo(JSONObject requestMessage) throws GenerallyException;

    void updateInfo(JSONObject requestMessage) throws GenerallyException;

    void persisitImci(String orderId) throws GenerallyException;

    /**
     * 
     * Description:根据订单号修改inst_managercar_info 信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    int updateManagerCarInfoByOrderId(JSONObject requestMessage);

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
     * @return 2018年1月15日 11:31:59
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
    PageInfo<GPSInfo> selectGPSInfoListByOrderId(JSONObject requestMessage);

    /**
     * 
     * Description:车辆状态跟踪模块列表与查询接口 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 17:02:31
     */
    PageInfo<CgetCarStatusInfoResponseMessage> selectCarToGPSList(JSONObject requestMessage);

    /**
     * 
     * Description:FINANCE-3031 车辆状态查询页面增加【导出】功能，导出列表页的所有信息；FINANCE-3057车辆状态查询页面增加【导出】功能，导出列表页的所有信息
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return chengzhen 2018年3月7日 14:51:27
     */
    String exportXlsxCgetinfolist(JSONObject requestMessage, HttpServletResponse response);

    /**
     * 
     * Description:根据订单号获取应还金额
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    Map<String, BigDecimal> getCurrentBalance(JSONObject objs);

    /**
     * 
     * Description:验证订单是否存在车秒贷订单
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年10月22日
     */
    Map<String, String> validateOrder(JSONObject objs);
}
