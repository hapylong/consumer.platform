package com.iqb.consumer.manage.front.asset.allocation;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoFormBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetObjectInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.service.IAssetInfoService;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.service.ICustomerService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

/**
 * 
 * Description: 资产分配服务接口
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("/assetAllocation")
public class AssetAllocationRestController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(AssetAllocationRestController.class);

    static {
        logger.info("资产分配服务接口");
    }

    @Autowired
    private IAssetInfoService assetInfoServiceImpl;
    @Autowired
    private ICustomerService customerServiceImpl;

    /**
     * 
     * Description: 获取资产分配列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年9月29日 下午4:25:38
     */
    @ResponseBody
    @RequestMapping(value = "/getAssetAllocationList", method = RequestMethod.POST)
    public Object getAssetAllocationList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---获取资产分配列表开始" + JSONUtil.objToJson(objs));
            PageInfo<AssetInfoBean> pageInfo = this.assetInfoServiceImpl.getAssetInfoList(objs);
            logger.info("IQB调试信息---获取资产分配列表结束");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", pageInfo);
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
     * 
     * Description:查询订单信息对应的债权人信息
     * 
     * @author haojinlong
     * @param objsO
     * @param request
     * @return 2017年11月3日
     */
    @ResponseBody
    @RequestMapping(value = "/getAssetDetails", method = {RequestMethod.GET, RequestMethod.POST})
    public Map getAssetAllocationDetails(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---获取资产分配详细信息开始" + JSONUtil.objToJson(objs));
            Map<Integer, Object> creditorMap = new HashMap<>();
            String orderId = objs.getString("orderId");
            AssetInfoFormBean formBean = this.assetInfoServiceImpl.getAssetInfoDetails(orderId);
            if (formBean != null) {
                String customerCode = String.valueOf(formBean.getMerchantId());
                CustomerBean customerBean = customerServiceImpl.getCustomerStoreInfoListByCode(customerCode);
                String creditorOtherInfo = customerBean.getCreditorOtherInfo();

                customerBean.setFlagId(0);
                creditorMap.put(0, customerBean);
                /**
                 * 获取债权人信息
                 */
                getCreditInfo(creditorMap, creditorOtherInfo);

            }
            logger.info("IQB调试信息---获取资产分配详细信息结束");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", formBean);
            linkedHashMap.put("customerInfo", creditorMap);
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
     * Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月7日
     */
    private void getCreditInfo(Map<Integer, Object> creditorMap, String creditorOtherInfo) {
        CustomerBean customerBean;
        if (!StringUtil.isNull(creditorOtherInfo)) {
            JSONObject json = JSONObject.parseObject(creditorOtherInfo);
            JSONArray jsonArray = json.getJSONArray("requestMessage");
            for (int i = 0; i < jsonArray.size(); i++) {
                customerBean = new CustomerBean();
                JSONObject jsonCred = (JSONObject) jsonArray.get(i);

                customerBean.setCreditorName(jsonCred.getString("creditorName"));
                customerBean.setCreditorIdNo(jsonCred.getString("creditorIdNo"));
                customerBean.setCreditorPhone(jsonCred.getString("creditorPhone"));
                customerBean.setCreditorBankNo(jsonCred.getString("creditorBankNo"));
                customerBean.setCreditorBankName(jsonCred.getString("creditorBankName"));
                customerBean.setFlagId(i + 1);
                creditorMap.put(i + 1, customerBean);
            }
        }
    }

    /**
     * 
     * 根据条件查询资产分配标的信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月3日
     */
    @ResponseBody
    @RequestMapping(value = "/selectAssetObjectInfo", method = RequestMethod.POST)
    public Object selectAssetObjectInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---根据条件查询资产分配标的信息开始--{}", objs);
            PageInfo<AssetObjectInfoBean> pageInfo = assetInfoServiceImpl.selectAssetObjectInfo(objs);
            logger.info("IQB调试信息---根据条件查询资产分配标的信息结束");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", pageInfo);
            linkedHashMap.put("totalCount", pageInfo.getTotal());
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
     * 标的明细导出功能
     * 
     * @param objs
     * @param request
     * @return 2018年8月7日
     * @author haojinlong
     */
    @ResponseBody
    @RequestMapping(value = {"/exportAssetObjectInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportAssetObjectInfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.debug("开始导出标的明细报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para;
                para = request.getParameter(paraName);

                data.put(paraName, para.trim());
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = assetInfoServiceImpl.exportAssetObjectInfo(objs, response);
            logger.debug("导出标的明细报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据订单号获、预计放款时间取当前期数以及剩余本金
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    @ResponseBody
    @RequestMapping(value = "/getAssetInfo", method = RequestMethod.POST)
    public Object getAssetInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {

            logger.info("IQB调试信息---根据订单号获、预计放款时间取当前期数以及剩余本金开始--{}", objs);
            Map<String, String> resultMap = new HashMap<>();
            Map<String, Object> assetInfo = assetInfoServiceImpl.getAssetInfo(objs);
            logger.info("---assetInfo--{}", assetInfo);
            // FINANCE-3712 资产分配页面，当选择剩余未还推标时，推标金额保留整数
            if (!CollectionUtils.isEmpty(assetInfo) && assetInfo.get("sbAmt") != null) {
                BigDecimal sbAmt = (BigDecimal) assetInfo.get("sbAmt");
                sbAmt = sbAmt.setScale(0, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                resultMap.put("sbAmt", sbAmt.toString());
                resultMap.put("repayNo", String.valueOf((int) assetInfo.get("repayNo")));
            }
            logger.info("IQB调试信息---根据订单号获、预计放款时间取当前期数以及剩余本金结束");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", resultMap);
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
