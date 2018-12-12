package com.iqb.consumer.data.layer.biz.carstatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.carstatus.entity.InstManageCarInfoEntity;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.GPSInfo;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.dao.carstatus.CarStatusRepository;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;

@Component
public class CarStatusManager extends BaseBiz {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CarStatusManager.class);
    @Autowired
    private CarStatusRepository carStatusRepository;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;

    /**
     * 
     * Description:FINANCE-1439 车辆状态查询FINANCE-1472 车辆状态跟踪
     * 
     * @param
     * @return CgetCarStatusInfoResponseMessage
     * @throws @Author adam Create Date: 2017年7月10日 下午3:45:23
     */
    public List<CgetCarStatusInfoResponseMessage> cgetInfoList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        List<CgetCarStatusInfoResponseMessage> list = carStatusRepository.cgetInfo(requestMessage);

        List<String> orderList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (CgetCarStatusInfoResponseMessage carStatus : list) {
                orderList.add(carStatus.getOrderId());
            }
        }
        JSONObject objs = new JSONObject();
        objs.put("orderList", orderList);
        List<Map<String, Object>> billList;
        try {
            billList = getBillStatusByOrderIds(objs);
            if (!CollectionUtils.isEmpty(billList)) {
                for (CgetCarStatusInfoResponseMessage carStatusBean : list) {
                    for (Map<String, Object> map : billList) {
                        String orderId = (String) map.get("orderId");
                        if (carStatusBean.getOrderId().equals(orderId)) {
                            carStatusBean.setBillStatus(String.valueOf((int) map.get("billStatus")));
                            carStatusBean.setCurItems((int) map.get("curItems"));
                        }
                    }
                }
            }
        } catch (IqbException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 
     * Description:根据订单号修改inst_managercar_info 信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    public int updateManagerCarInfoByOrderId(Map<String, Object> params) {
        super.setDb(0, super.SLAVE);
        return carStatusRepository.updateManagerCarInfoByOrderId(params);
    }

    /**
     * 
     * Description:根据订单号查询车辆状态跟踪回显信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月10日
     */
    public ManageCarInfoBean selectOrderInfoByOrderId(JSONObject requestMessage) {
        super.setDb(1, super.SLAVE);
        return carStatusRepository.selectOrderInfoByOrderId(requestMessage);
    }

    /**
     * 
     * Description:根据订单号查询承租人回显信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月11日
     */
    public ManageCarInfoBean selectSubleaseInfoByOrderId(JSONObject requestMessage) {
        super.setDb(1, super.SLAVE);
        return carStatusRepository.selectSubleaseInfoByOrderId(requestMessage);
    }

    /**
     * 
     * Description: FINANCE-1439 车辆状态查询FINANCE-1472 贷后处置/车辆出库/车辆状态
     * 
     * @param
     * @return CgetCarStatusInfoResponseMessage
     * @throws @Author adam Create Date: 2017年7月10日 下午3:45:50
     */
    public List<CgetCarStatusInfoResponseMessage> cgetInfo2List(JSONObject requestMessage, boolean isPageHelper) {
        super.setDb(0, super.SLAVE);
        if (isPageHelper) {
            PageHelper.startPage(getPagePara(requestMessage));
        }
        List<CgetCarStatusInfoResponseMessage> list = carStatusRepository.cgetInfo2(requestMessage);

        List<String> orderList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (CgetCarStatusInfoResponseMessage carStatus : list) {
                orderList.add(carStatus.getOrderId());
            }
        }
        JSONObject objs = new JSONObject();
        objs.put("orderList", orderList);
        List<Map<String, Object>> billList;
        try {
            billList = getBillStatusByOrderIds(objs);
            logger.info("----账务系统返回账单状态列表---{}", billList);
            if (!CollectionUtils.isEmpty(billList)) {
                for (CgetCarStatusInfoResponseMessage carStatusBean : list) {
                    for (Map<String, Object> map : billList) {
                        String orderId = (String) map.get("orderId");
                        if (carStatusBean.getOrderId().equals(orderId)) {
                            carStatusBean.setBillStatus(String.valueOf((int) map.get("billStatus")));
                            carStatusBean.setCurItems((int) map.get("curItems"));
                        }
                    }
                }
            }
        } catch (IqbException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateIMCI(InstManageCarInfoEntity imci) {
        super.setDb(0, super.MASTER);
        carStatusRepository.updateIMCI(imci);
    }

    /**
     * 
     * Description: 标记该订单为 已处理订单（车辆状态跟踪）
     * 
     * @param
     * @return int
     * @throws @Author adam Create Date: 2017年7月10日 下午6:45:10
     */
    public int markIOIEByCarstatus(String orderId) {
        super.setDb(0, super.MASTER);
        return carStatusRepository.markIOIEByCarstatus(orderId);
    }

    public void persisitImci(String orderId) {
        super.setDb(0, super.MASTER);
        carStatusRepository.persisitImci(orderId);
    }

    /**
     * 
     * Description:根据订单号修改状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月12日
     */
    public int updateStatusByOrderId(Map<String, String> params) {
        super.setDb(0, super.SLAVE);
        return carStatusRepository.updateStatusByOrderId(params);
    }

    /**
     * 通过订单号查询状态
     * 
     * @param orderId
     * @return
     */
    public int getCarStatusByOrderId(String orderId) {
        super.setDb(0, super.SLAVE);
        return carStatusRepository.getCarStatusByOrderId(orderId);
    }

    /**
     * 通过订单号和procKey查询审批页面
     * 
     * @param orderId
     * @param procKey
     * @return
     */
    public Map<String, String> getProcHtml(String orderId, String procKey) {
        super.setDb(0, super.SLAVE);
        return carStatusRepository.getProcHtml(orderId, procKey);
    }

    /**
     * 
     * Description:根据订单号查询以及GPS状态回显信息 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 11:31:59
     */
    public CgetCarStatusInfoResponseMessage getOrderInfoAndGPSInfoByOrderId(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        return carStatusRepository.getOrderInfoAndGPSInfoByOrderId(requestMessage);
    }

    /**
     * 
     * Description:保存GPS状态到inst_gpsinfo表 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 14:47:18
     */
    public void saveGPSInfo(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        carStatusRepository.saveGPSInfo(requestMessage);
    }

    /**
     * 
     * Description:根据orderID查询展示inst_gpsinfo表数据 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 15:10:18
     */
    public List<GPSInfo> selectGPSInfoListByOrderId(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return carStatusRepository.selectGPSInfoListByOrderId(requestMessage);
    }

    /**
     * 
     * Description:车辆状态跟踪模块列表与查询接口 FINANCE-2838 车辆状态跟踪：GPS状态更新FINANCE-2859车辆状态跟踪
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年1月15日 17:02:31
     */
    public List<CgetCarStatusInfoResponseMessage> selectCarToGPSList(JSONObject requestMessage) {
        super.setDb(0, super.SLAVE);
        PageHelper.startPage(getPagePara(requestMessage));
        return carStatusRepository.selectCarToGPSList(requestMessage);
    }

    /**
     * 
     * Description:调用账务系统查询账单状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月8日
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getBillStatusByOrderIds(JSONObject jsonObject) throws IqbException {
        logger.info("---根据订单号查询账务系统账单状态---{},---账务系统接口地址---{}", jsonObject,
                consumerConfig.getFinanceBillCurrentBillInfoUrl());
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> listMap;
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillCurrentBillInfoUrl(),
                encryptUtils.encrypt(jsonObject));
        if (resultStr != null) {
            result = JSONObject.parseObject(resultStr);

            listMap = (List<Map<String, Object>>) result.get("result");
            if (listMap == null) {
                throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000003);
            }
        } else {
            throw new IqbException(ApiReturnInfo.API_GET_FAIL_80000002);
        }
        return listMap;
    }

    /**
     * 
     * Description:根据订单号查询贷后车辆 信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    public InstManageCarInfoEntity getCarStatusInfoByOrderId(String orderId) {
        super.setDb(1, super.SLAVE);
        return carStatusRepository.getCarStatusInfoByOrderId(orderId);
    }

    public void updateRemindPhone(String orderId) {
        super.setDb(0, super.SLAVE);
        carStatusRepository.updateRemindPhone(orderId);

    }
}
