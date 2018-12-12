package com.iqb.consumer.manage.front.crm;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import jodd.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.crm.customer.bean.pojo.CustomerInfoResponsePojo;
import com.iqb.consumer.crm.store.service.ICustomerStoreService;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;

/**
 * 
 * Description: 门店信息维护
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年10月12日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@RestController
@RequestMapping("/customerStore")
public class CustomerStoreController extends BasicService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CustomerRestController.class);
    private static final int SERVICE_CODE_UPDATE_CUSTOMER_STORE_INFO = 1;
    private static final int SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_CODE = 2;
    private static final int SERVICE_CODE_UPDATE_CUSTOMER_INFO = 3;
    private static final int SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_ORDERID = 4;

    @Autowired
    private ICustomerStoreService customerStoreServiceImpl;

    /**
     * 
     * Description: 查询门店信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午10:08:01
     */
    @ResponseBody
    @RequestMapping(value = "/getCustomerStoreInfoList", method = RequestMethod.POST)
    public Object getCustomerStoreInfoList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---查询门店信息" + JSONUtil.objToJson(objs));
            PageInfo pageInfo = this.customerStoreServiceImpl.getCustomerStoreInfoList(objs);
            logger.info("IQB调试信息---查询门店信息");
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
     * Description: 根据客户编码获取门店信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 上午11:31:53
     */
    @ResponseBody
    @RequestMapping(value = "/getCustomerStoreInfoByCode", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_CODE)
    public Object getCustomerStoreInfoByCode(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---根据客户编码获取门店信息" + JSONUtil.objToJson(objs));
            CustomerInfoResponsePojo cirp = this.customerStoreServiceImpl.getCustomerStoreInfoByCode(objs);
            logger.info("IQB调试信息---根据客户编码获取门店信息");
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", cirp);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_CODE);
        } catch (Throwable e) {// 系统发生异常
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_CODE);
        }
    }

    /**
     * 
     * Description: 更新客户门店信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午2:52:03
     * @UpdateAuthor Adam
     */
    @ResponseBody
    @RequestMapping(value = "/updateCustomerStoreInfo", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_UPDATE_CUSTOMER_STORE_INFO)
    public Object updateCustomerStoreInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            customerStoreServiceImpl.updateCustomerStoreInfo(objs);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CUSTOMER_STORE_INFO);
        } catch (Exception e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CUSTOMER_STORE_INFO);
        }
    }

    /**
     * 
     * Description: FINANCE-1204 质押车项目终审债权人信息可修改 ， 通过orderId 查找 债权人信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月12日 上午11:05:40
     */
    @ResponseBody
    @RequestMapping(value = "/get_csi_by_oid", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_ORDERID)
    public Object updateCustomerStoreInfoByOid(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            String orderId = requestMessage.getString(KEY_ORDER_ID);
            if (StringUtil.isEmpty(orderId)) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.CONTROLLER, Location.A);
            }
            CustomerInfoResponsePojo cirp = this.customerStoreServiceImpl.getCustomerStoreInfoByOid(orderId);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("result", cirp);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_CODE);
        } catch (Throwable e) {// 系统发生异常
            return generateFailResponseMessage(e, SERVICE_CODE_GET_CUSTOMER_STORE_INFO_BY_CODE);
        }
    }

    /**
     * 
     * Description: FINANCE-1204 质押车项目终审债权人信息可修改 FINANCE-1221 债权人修改
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月9日 下午6:37:27
     */
    @ResponseBody
    @RequestMapping(value = "/update_customer_info", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_UPDATE_CUSTOMER_INFO)
    public Object updateCustomerInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            customerStoreServiceImpl.updateCustomerInfo(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CUSTOMER_INFO);
        } catch (Exception e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CUSTOMER_INFO);
        }
    }

    /**
     * 
     * Description: 更新门店审核信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2016年10月12日 下午3:50:56
     */
    @ResponseBody
    @RequestMapping(value = "/updateCustomerStoreAuditInfo", method = RequestMethod.POST)
    public Object updateCustomerStoreAuditInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB调试信息---更新门店审核信息" + JSONUtil.objToJson(objs));
            this.customerStoreServiceImpl.updateCustomerStoreAuditInfo(objs);
            logger.info("IQB调试信息---更新门店审核信息");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_CUSTOMER_STORE_CENTER;
    }

}
