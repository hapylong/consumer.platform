/**
 * @Copyright (c) www.iqb.com All rights reserved.
 * @Description: TODO
 * @date 2017年3月6日 下午3:09:56
 * @version V1.0
 */
package com.iqb.consumer.manage.front.workflow;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.eatep.ec.apiinterface.EcApiInter;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class EcEnterImpl implements EcApiInter {

    @Autowired
    private UserBeanBiz userBeanBiz;

    @Override
    public Map<String, Object> getEcSignerInfo(String arg0) {
        UserBean userBean = userBeanBiz.selectAll(arg0);
        Map<String, Object> result = transBean2Map(userBean);
        JSONObject jsonObject = JSONObject.parseObject(userBean.getCheckInfo());
        result.put("ecSignerCode", result.get("regId"));// 手机号
        result.put("ecSignerName", result.get("realName"));// 姓名
        result.put("ecSignerType", "s");
        result.put("ecSignerCertType", 0);// 证件类型(0-居民身份证 E-户口簿 F-临时居民身份证)
        result.put("ecSignerCertNo", result.get("idNo"));// 证件号
        result.put("ecSignerEmail", result.get("regId"));// 邮箱
        result.put("ecSignerAddress", jsonObject.get("addprovince"));// 地址
        result.put("ecSignerProvince", "测试");// 省
        result.put("ecSignerCity", "测试");// 市
        result.put("ecSignerPhone", result.get("regId"));
        result.put("emergencyContact", jsonObject.get("contactname1"));// 紧急联系人
        result.put("emergencyContactInfo", jsonObject.get("contactphone1"));// 紧急联系电话
        // result.put("addprovince", jsonObject.get("addprovince"));// 实际借款人地址
        return result;
    }

    /**
     * 
     * @return
     */
    public String random(int n) {
        String array = "";
        for (int i = 1; i <= n; i++) {
            array += (int) (Math.random() * n) + "";
        }
        return array;
    }

    public static Map<String, Object> transBean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }

    public static void main(String[] args) {
        // UserBean userBean = new UserBean();
        // userBean.setAutoLogin(1);
        // userBean.setIdNo("12312312");
        // System.out.println(transBean2Map(userBean));
    }

}
