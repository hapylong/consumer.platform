package com.iqb.consumer.data.layer.biz.cheegu;

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
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;
import com.iqb.consumer.data.layer.dao.cheegu.InstCarPeccancyDao;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.exception.IqbException;

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
 * 2018年5月28日上午10:56:48 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
public class InstCarPeccancyBiz extends BaseBiz {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(InstCarPeccancyBiz.class);
    @Autowired
    private InstCarPeccancyDao instCarPeccancyDao;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;

    /**
     * 
     * Description:根据条件查询车辆违章信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public List<InstCarPeccancyBean> selectInstCarPeccancyList(JSONObject objs) {
        super.setDb(0, super.SLAVE);
        if (objs.get("isPageHelper") == null) {
            PageHelper.startPage(getPagePara(objs));
        }
        List<InstCarPeccancyBean> list = instCarPeccancyDao.selectInstCarPeccancyList(objs);
        List<String> orderList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (InstCarPeccancyBean carPeccancyBean : list) {
                orderList.add(carPeccancyBean.getOrderId());
            }
        }
        JSONObject json = new JSONObject();
        json.put("orderList", orderList);
        List<Map<String, Object>> billList;
        try {
            billList = getBillStatusByOrderIds(json);
            logger.info("----账务系统返回账单状态列表---{}", billList);
            if (!CollectionUtils.isEmpty(billList)) {
                for (InstCarPeccancyBean carPeccancyBean : list) {
                    for (Map<String, Object> map : billList) {
                        String orderId = (String) map.get("orderId");
                        if (carPeccancyBean.getOrderId().equals(orderId)) {
                            carPeccancyBean.setBillStatus(String.valueOf((int) map.get("billStatus")));
                            carPeccancyBean.setCurItems((int) map.get("curItems"));
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
     * Description:调用账务系统查询账单状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月8日
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getBillStatusByOrderIds(JSONObject jsonObject) throws IqbException {
        logger.info("---根据订单号查询账务系统账单状态---{}", jsonObject,
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
     * Description:根据订单号 车架号修改违章信息表处理状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int updateInstCarPeccancyStatusByOrderId(JSONObject objs) {
        super.setDb(0, super.MASTER);
        return instCarPeccancyDao.updateInstCarPeccancyStatusByOrderId(objs);
    }

    /**
     * 
     * Description:根据订单号车架号查询违章车辆信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public InstCarPeccancyBean selectInstCarPeccancyInfo(JSONObject objs) {
        super.setDb(1, super.MASTER);
        return instCarPeccancyDao.selectInstCarPeccancyInfo(objs);
    }

    /**
     * 
     * Description:插入车辆违章信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    public int insertInstCarPeccancy(InstCarPeccancyBean instCarPeccancyBean) {
        super.setDb(0, super.MASTER);
        return instCarPeccancyDao.insertInstCarPeccancy(instCarPeccancyBean);
    }

    /**
     * 
     * Description:根据订单号查询违章信息列表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月4日
     */
    public List<InstCarPeccancyBean> selectInstCarPeccancyListByOrderIds(List<InstCarPeccancyBean> instCarPeccancyList) {
        super.setDb(1, super.MASTER);
        return instCarPeccancyDao.selectInstCarPeccancyListByOrderIds(instCarPeccancyList);
    }
}
