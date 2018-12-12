package com.iqb.consumer.manage.front.product;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.merchant.PayConfBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;
import com.iqb.consumer.data.layer.biz.sys.SysCoreDictItemBiz;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.consumer.service.layer.product.service.IProductService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/product")
public class ProductController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private IProductService productService;
    @Autowired
    private SysCoreDictItemBiz sysCoreDictItemBiz;
    @Resource
    private AdminService adminServiceImpl;

    /*
     * Description: 增加产品信息
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = {RequestMethod.POST, RequestMethod.GET})
    public Map addPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---新增产品信息{}...", objs);
            int res = productService.insertPlan(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (res == 0) {
                linkedHashMap.put("result", "succ");
            } else {
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
     * 通过字典表查询所有的账务分期方案
     * 
     * @param request
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/{type}/selDictItem", method = {RequestMethod.POST})
    public Object selDictItem(HttpServletRequest request, @PathVariable("type") String type) {
        try {
            logger.info("IQB查询分期计划方案:{}", type);
            List<SysDictItem> list = sysCoreDictItemBiz.selDictItem(type);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 删除产品信息
     */
    @ResponseBody
    @RequestMapping(value = "/del", method = {RequestMethod.POST, RequestMethod.GET})
    public Map delPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---删除产品信息...");
            productService.deletePlanByID(objs);
            logger.info("IQB信息---删除产品信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "succ");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 修改产品信息
     */
    @ResponseBody
    @RequestMapping(value = "/mod", method = {RequestMethod.POST, RequestMethod.GET})
    public Map modPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---修改产品信息...");
            int res = productService.updatePlan(objs);
            logger.info("IQB信息---修改产品信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (res == 0) {
                linkedHashMap.put("result", "succ");
            } else {
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

    /*
     * Description: 获取产品信息
     */
    @ResponseBody
    @RequestMapping(value = "/queryById", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getPlanInfoById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            logger.info("IQB信息---获取产品信息...");
            String id = objs.getString("id");
            if (id == null || id.equals("")) {
                linkedHashMap.put("retCode", "fail");
            }
            PlanBean pb = productService.getPlanByID(objs);
            logger.info("IQB信息---获取产品信息...");

            linkedHashMap.put("result", pb);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 获取产品信息列表
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getPlanInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("获取产品信息查询条件:{}", objs);
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
            PageInfo<PlanBean> list = productService.getPlanByMerNos(objs);
            logger.info("IQB信息---获取产品信息...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/{status}/update_status", method = {RequestMethod.POST})
    public Map updateStatus(@RequestBody JSONObject objs, @PathVariable("status") String status,
            HttpServletRequest request) {
        try {
            int result = productService.updateStatus(objs, status);
            if (result >= 0) {
                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
                linkedHashMap.put("result", "success");
                return super.returnSuccessInfo(linkedHashMap);
            } else {
                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
                linkedHashMap.put("result", "success");
                return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00090004));
            }
        } catch (Exception e) {
            logger.error("update_status error：", e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description:不同商户配置不同的支付主体,insert操作 2018年1月18日 10:34:23 chengzhen
     */
    @ResponseBody
    @RequestMapping(value = "/addPayConf", method = {RequestMethod.POST})
    public Map addPayConf(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("---新增PayConf信息{}...", objs);
            Set<String> objsKeys = objs.keySet();
            for (String key : objsKeys) {
                objs.put(key, objs.get(key).toString().trim());
            }
            int res = productService.addPayConf(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (res == 1) {
                linkedHashMap.put("result", "succ");
            } else {
                linkedHashMap.put("result", "该商户号已存在");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("update_status error：", e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description:不同商户配置不同的支付主体,delete操作 2018年1月18日 11:11:32 chengzhen
     */
    @ResponseBody
    @RequestMapping(value = "/delPayConf", method = {RequestMethod.POST, RequestMethod.GET})
    public Map delPayConf(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---删除PayConf信息...");
            productService.delPayConf(objs);
            logger.info("IQB信息---删除PayConf信息...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "succ");
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
     * Description:不同商户配置不同的支付主体,delete操作 2018年1月18日 11:18:04 chengzhen
     */
    @ResponseBody
    @RequestMapping(value = "/updatePayConf", method = {RequestMethod.POST})
    public Map updatePayConf(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            Set<String> objsKeys = objs.keySet();
            for (String key : objsKeys) {
                objs.put(key, objs.get(key).toString().trim());
            }
            productService.updatePayConf(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            logger.error("update_status error：", e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * Description:不同商户配置不同的支付主体,单查询 2018年1月18日 11:31:55 chengzhen
     */
    @ResponseBody
    @RequestMapping(value = "/getPayConfByMno", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getPayConfByMno(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            logger.info("IQB信息---获取单条payConf信息...");
            PayConfBean pc = productService.getPayConfByMno(objs);
            logger.info("IQB信息---获取单条payConf信息...");
            linkedHashMap.put("result", pc);
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
     * Description:不同商户配置不同的支付主体,列表查询 2018年1月18日 11:36:51 chengzhen
     */
    @ResponseBody
    @RequestMapping(value = "/queryPayConfList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map queryPayConfList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("获取PayConf信息查询条件:{}", objs);
            adminServiceImpl.getMerchantNos(objs);
            PageInfo<PayConfBean> list = productService.queryPayConfList(objs);
            logger.info("IQB信息---获取PayConf信息...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
