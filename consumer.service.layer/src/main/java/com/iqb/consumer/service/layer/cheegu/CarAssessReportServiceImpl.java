package com.iqb.consumer.service.layer.cheegu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarReportImageBean;
import com.iqb.consumer.data.layer.biz.cheegu.InstCarReportBiz;
import com.iqb.etep.common.utils.StringUtil;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年5月23日下午5:32:18 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class CarAssessReportServiceImpl implements CarAssessReportService {
    protected static final Logger logger = LoggerFactory.getLogger(CarAssessReportServiceImpl.class);
    @Resource
    private ConsumerConfig consumerConfig;
    @Autowired
    private InstCarReportBiz instCarReportBiz;

    private String msg;

    /**
     * 获取车辆评估报告并将报告信息入库
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月23日
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public Map<String, Object> getCarAssessReport(String orderId, String vin) {
        Map<String, Object> resultMap = new HashMap<>();
        InstCarReportBean instCarReportBean = cheeguForReport(vin);
        if (instCarReportBean != null) {
            instCarReportBean.setOrderId(orderId);
            // 保存车辆评估报告
            instCarReportBiz.insertInstCarReport(instCarReportBean);

            List<InstCarReportImageBean> imageList = instCarReportBean.getImageList();
            if (!CollectionUtils.isEmpty(imageList)) {
                for (InstCarReportImageBean imageBean : imageList) {
                    imageBean.setOrderId(orderId);
                    imageBean.setVin(vin);
                }
            }
            // 保存车辆评估图片信息
            instCarReportBiz.batchInsertCarReportImage(imageList);

            // 将图片与视频链接分类存储
            List<InstCarReportImageBean> templist = new ArrayList<>();
            List<InstCarReportImageBean> videolist = new ArrayList<>();
            if (!CollectionUtils.isEmpty(imageList)) {
                for (InstCarReportImageBean imageBean : imageList) {
                    String regEx = "AVI|WMV|RM|RMVB|MPEG1|MPEG2|MPEG4|MP4";
                    String url = imageBean.getUrl();
                    Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                    url = url.substring(url.lastIndexOf('.') + 1);
                    if (pattern.matcher(url).matches()) {
                        videolist.add(imageBean);
                    } else {
                        templist.add(imageBean);
                    }
                }
            }
            resultMap.put("msg", "success");
            resultMap.put("refuseMsg", instCarReportBean.getRefuseMsg());
            resultMap.put("carReportUrl", instCarReportBean.getReportUrl());
            resultMap.put("imageList", templist);
            resultMap.put("videolist", videolist);
        } else {
            resultMap.put("msg", msg);
            resultMap.put("refuseMsg", "");
            resultMap.put("carReportUrl", "");
            resultMap.put("imageList", new ArrayList<InstCarReportImageBean>());
            resultMap.put("videolist", new ArrayList<InstCarReportImageBean>());
        }

        return resultMap;
    }

    /**
     * 
     * Description:车易估车辆评估接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月23日
     */
    public InstCarReportBean cheeguForReport(String vin) {
        InstCarReportBean bean = null;
        Map<String, String> params = new HashMap<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        // 数据组装
        params.put("vin", vin);
        params.put("username", consumerConfig.getCheeguUserName());
        params.put("password", consumerConfig.getCheeguPassWord());
        params.put("token", consumerConfig.getCheeguToken());
        String result = null;
        logger.info("---根据车架号获取车辆评估报告开始----{}", params);
        try {
            result = SimpleHttpUtils.httpRequest(consumerConfig.getCheeguUrl(), params, "post", "UTF-8", headers);
        } catch (Exception e) {
            logger.error("---调用车易估车辆评估接口报错---{}", e);
        }
        logger.info("---根据车架号获取车辆评估报告结束----{}", result);
        if (!StringUtil.isNull(result)) {
            JSONObject json = JSONObject.parseObject(result);
            int resultValue = json.getInteger("success");
            if (resultValue == 1) {
                bean = JSONObject.toJavaObject(json, InstCarReportBean.class);
            }
            msg = json.getString("msg");
        }
        return bean;
    }
}
