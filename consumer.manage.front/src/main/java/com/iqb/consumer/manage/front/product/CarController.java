package com.iqb.consumer.manage.front.product;

import java.util.LinkedHashMap;
import java.util.Map;

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
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.project.ProjectBaseInfoBean;
import com.iqb.consumer.service.layer.product.service.IProBaseInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/car")
public class CarController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    private IProBaseInfoService proBaseInfoService;

    // @Autowired
    // private SysUserSession sysUserSession;

    /*
     * Description: 增加车系信息
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public Map addPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---新增车系{}信息...", objs);
            int res = proBaseInfoService.insertProBaseInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (res == 0) {
                linkedHashMap.put("result", "succ");
            } else {
                linkedHashMap.put("result", "fail");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 删除车系信息
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = {RequestMethod.POST, RequestMethod.GET})
    public Map delPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---删除车系信息...");
            proBaseInfoService.delProBaseInfoByID(objs);
            logger.info("IQB信息---删除车系信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "succ");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 修改车系信息
     */
    @ResponseBody
    @RequestMapping(value = "/mod", method = {RequestMethod.POST, RequestMethod.GET})
    public Map modPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---修改车系信息...");
            int res = proBaseInfoService.updateProBaseInfo(objs);
            logger.info("IQB信息---修改车系信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (res == 0) {
                linkedHashMap.put("result", "succ");
            } else {
                linkedHashMap.put("result", "fail");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 获取车系信息
     */
    @ResponseBody
    @RequestMapping(value = "/queryById", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getPlanInfoById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            String id = objs.getString("id");
            if (id == null || id.equals("")) {
                linkedHashMap.put("retCode", "fail");
            }
            ProjectBaseInfoBean lb = proBaseInfoService.getProBaseInfoById(objs);
            // 商户号(冗余返回，避免影响其他接口功能)
            lb.setMerchNames(lb.getMerchantNo());
            logger.info("IQB信息---获取车系信息...");

            linkedHashMap.put("result", lb);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 获取车系信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            // // 待定
            // String orgCode = sysUserSession.getOrgCode();
            // String merchantNo = objs.getString("merchantNo");
            // if (merchantNo == null || merchantNo.equals("") ||
            // "全部商户".equals(merchantNo)) {
            // if (orgCode == null || orgCode.equals("")) {
            // return null;
            // } else {
            // objs.put("merCodeDef", orgCode);
            // }
            // } else {
            // objs.put("merCodeSel", merchantNo);
            // }
            PageInfo<ProjectBaseInfoBean> list = proBaseInfoService.getProBaseInfoByMerNos(objs);
            logger.info("IQB信息---获取车系信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
