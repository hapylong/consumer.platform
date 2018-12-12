/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: 加盟商信息维护
 * @date 20160907
 * @version V1.0
 */
package com.iqb.consumer.manage.front.crm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.service.layer.CRM.CRMService;
import com.iqb.consumer.service.layer.merchant.service.IMerchantService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * @author gxy
 */
@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/merchant")
public class MerchantController extends BaseService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Resource
    private IMerchantService merchantService;
    @Resource
    private CRMService CRMService;
    @Autowired
    private SysUserSession sysUserSession;

    /*
     * Description: 获取商户列表
     */
    @ResponseBody
    @RequestMapping(value = "/getMerList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getMerList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orgCode = sysUserSession.getOrgCode();
            if (orgCode == null || orgCode.equals("")) {
                return null;
            } else {
                objs.put("id", orgCode);
            }
            List<MerchantBean> list = merchantService.getAllMerByID(objs);
            logger.info("IQB信息---获取商户列表信息...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 新增商户
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-add", method = {RequestMethod.POST, RequestMethod.GET})
    public Map addMerchant(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            logger.info("IQB信息---新增商户信息...");
            int result = CRMService.addMerchant(objs);
            logger.info("IQB信息---新增商户信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (result == 1) {
                linkedHashMap.put("result", "succ");
            } else if (result == -1) {
                logger.error("insertMerchantInfo err!");
                linkedHashMap.put("result", "fail");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 根据merchantName 查询商户对应的ID
     * 
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMerByMerNo", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getMerByMerNo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            logger.info("IQB信息---根据商户简称查询商户id 开始...");
            String merchantName = objs.getString("merchantName");
            MerchantBean bean = merchantService.getMerByMerName(merchantName);
            logger.info("IQB信息---根据商户简称查询商户id 结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (bean != null) {
                linkedHashMap.put("result", bean.getId());
            } else {
                logger.error("selectMerchantInfo err!");
                linkedHashMap.put("result", "fail");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:获取商户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月1日
     */
    @ResponseBody
    @RequestMapping(value = "/getMerListForFound", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getMerListForFound(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            String orgCode = sysUserSession.getOrgCode();
            if (orgCode == null || orgCode.equals("")) {
                return null;
            } else {
                objs.put("id", orgCode);
            }
            List<Map<String, String>> returnList = new ArrayList<>();
            List<MerchantBean> list = merchantService.getAllMerByID(objs);
            Map<String, String> map = null;
            for (MerchantBean bean : list) {
                map = new HashMap<>();
                map.put("id", bean.getMerchantNo());
                map.put("text", bean.getMerchantShortName());
                returnList.add(map);
            }
            logger.info("IQB信息---获取商户列表信息...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", returnList);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
