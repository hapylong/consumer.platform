package com.iqb.consumer.service.layer.dict;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.CommonConstant.DictTypeCodeEnum;
import com.iqb.consumer.common.constant.DictConvertCenter;
import com.iqb.consumer.common.constant.DictConvertCenter.DICT_TYPE_CODE;
import com.iqb.consumer.common.constant.DictConvertCenter.TARGET_COLUMN;
import com.iqb.consumer.common.constant.DictConvertCenter.WHERE_COLUMN;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.order.house.HouseOrderEntity;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.dict.DictManager;
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.consumer.data.layer.biz.sys.SysCoreDictItemBiz;

import jodd.util.StringUtil;

/**
 * 
 * Description: 字典表操作中心
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年6月19日    adam       1.0        1.0 Version 
 * </pre>
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private SysCoreDictItemBiz sysCoreDictItemBiz;

    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private JysOrderBiz jysOrderBiz;

    @Autowired
    private DictManager dictManager;

    /** 地址占位符 车类 Car 花花Huahua **/
    private static final String URI_PATH_ALL = "All";// 所有类占位符
    private static final String URI_PATH_CAR = "Car";// 车类占位符
    private static final String URI_PATH_HUAHUA = "Huahua";// 车类占位符
    private static final String URI_PATH_HOUSE = "House";// 车类占位符
    private static final String OPENID_ALL = "1";// 全部
    private static final String OPENID_CAR = "101";// 车贷
    private static final String OPENID_HUAHUA = "102";// 花花类
    private static final String OPENID_HOUSE = "103";// 房贷类

    @Override
    public List<SysDictItem> getDictListByDTC(DictTypeCodeEnum dtce) {
        if (dtce != null) {
            return sysCoreDictItemBiz.selDictItem(dtce.toString());
        } else {
            return null;
        }
    }

    @Override
    public SysDictItem getDictByDTCAndDC(DictTypeCodeEnum dtc, String dc) {
        return sysCoreDictItemBiz.getDictByDTCAndDC(dtc, dc);
    }

    @Override
    public String getOpenIdByBizType(String bizType) {
        SysDictItem sdi = sysCoreDictItemBiz.getDictByDTCAndDC(DictTypeCodeEnum.bizType2OpenId, bizType);
        if (sdi == null) {
            throw new RuntimeException("业务类型不存在");
        }
        return sdi.getDictValue();
    }

    @Override
    public String getOpenIdByOrderId(String orderId) {
        if (StringUtil.isEmpty(orderId)) {
            throw new RuntimeException("invalid request.");
        }
        InstOrderInfoEntity ioie = orderBiz.getIOIEByOid(orderId);
        if (ioie == null) {
            throw new RuntimeException("订单不存在");
        }
        String bizType = ioie.getBizType();
        return getOpenIdByBizType(bizType);
    }

    @Override
    public String getJysOpenIdByOrderId(String orderId) {
        JSONObject objs = new JSONObject();
        objs.put("orderId", orderId);
        JYSOrderBean job = jysOrderBiz.getSingleOrderInfo(objs);
        String bizType = job.getBizType();
        return getOpenIdByBizType(bizType);
    }

    @Override
    public String getOpenIdByPath(String path) {
        switch (path) {
            case URI_PATH_CAR:
                return OPENID_CAR;
            case URI_PATH_ALL:
                return OPENID_ALL;
            case URI_PATH_HUAHUA:
                return OPENID_HUAHUA;
            case URI_PATH_HOUSE:
                return OPENID_HOUSE;
            default:
                return null;
        }
    }

    private static Map<String, Object> DICT_CACHE_MAP = new HashMap<>();

    @Override
    @SuppressWarnings("rawtypes")
    public <T> List<T> changeDictValue(List<T> list) throws Exception {
        if (list == null || list.isEmpty()) {
            return null;
        }

        for (T t : list) {
            Class c = t.getClass();
            Field[] fields = c.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                DictConvertCenter dcc = fields[i].getAnnotation(DictConvertCenter.class);
                if (dcc == null) {
                    continue;
                }
                WHERE_COLUMN from = dcc.from();
                TARGET_COLUMN to = dcc.to();
                DICT_TYPE_CODE type = dcc.type();

                PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), t.getClass());
                Method mget = pd.getReadMethod();// 获得写方法
                Object value = mget.invoke(t);
                String CACHE_KEY = generateDictCacheKey(type, from, to, value);
                if (DICT_CACHE_MAP.get(CACHE_KEY) != null) {
                    Method mset = pd.getWriteMethod();// 获得写方法
                    mset.invoke(t, DICT_CACHE_MAP.get(CACHE_KEY));
                    continue;
                }
                if (value != null) {
                    String value2 = dictManager.replace(type, from, to, value);
                    Method mset = pd.getWriteMethod();// 获得写方法
                    mset.invoke(t, value2);
                    DICT_CACHE_MAP.put(CACHE_KEY, value2);
                }
            }
        }
        return null;

    }

    private String generateDictCacheKey(DICT_TYPE_CODE type, WHERE_COLUMN from, TARGET_COLUMN to, Object value) {
        return from + ":" + to + ":" + type + ":" + value;
    }

    @Override
    public String getOpenIdByHoId(String hoId) {
        if (StringUtil.isEmpty(hoId)) {
            throw new RuntimeException("invalid request.");
        }
        HouseOrderEntity hoe = orderBiz.queryHOEByOid(hoId);
        if (hoe == null) {
            throw new RuntimeException("订单不存在");
        }
        String bizType = hoe.getBusinessType();
        return getOpenIdByBizType(bizType);
    }

    @Override
    public SysDictItem getDictByDTCAndDN(DictTypeCodeEnum dtc, String dName) {
        return sysCoreDictItemBiz.getDictByDTCAndDN(dtc, dName);
    }
}
