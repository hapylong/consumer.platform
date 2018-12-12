package com.iqb.consumer.manage.front.dealer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.exception.DealerManageReturnInfo;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.dealer.DealerManage;
import com.iqb.consumer.data.layer.biz.dealer.DealerManageBiz;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * 车商管理前端控制类
 * 
 * @author Yeoman zhuyaoming@aliyun.com
 * 
 */
@RestController
@RequestMapping("/dealer")
public class DealerManageRest extends BaseService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private DealerManageBiz dealerManageBiz;
    @Resource
    private EncryptUtils encryptUtils;
    @Resource
    private UrlConfig urlConfig;

    /**
     * 保存车管信息
     * 
     * @param obj
     * @param response
     * @return
     */
    @RequestMapping(value = "/saveDealerInfo", method = RequestMethod.POST)
    public Object saveDealerInfo(@RequestBody JSONObject obj, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = null;
        Map<String, Object> params = new HashMap<>();
        DealerManage dealerManage = null;
        try {
            request.setCharacterEncoding("UTF-8"); // 解决乱码问题
            logger.info("保存车商信息:{}", obj);
            dealerManage = JSONObject.toJavaObject(obj, DealerManage.class);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_FORMATERROR_10000000));
        }
        // 将车商信息保存数据库,首先判断车商信息是否存在，如果存在就需要对车商信息进行更新
        try {
            params.put("orderId", dealerManage.getOrderId());
            DealerManage dealerInfo = dealerManageBiz.getDealerInfo(params);
            if (dealerInfo == null) {
                // 新增
                dealerManageBiz.saveDealerManageInfo(dealerManage);
            } else {
                // 修改
                dealerManageBiz.updateDealerManageInfo(dealerManage);
            }
            linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_SAVEERROR_10000001));
        }

    }

    /**
     * 通过订单号修改相关信息
     * 
     * @param obj
     * @param response
     * @return
     */
    @RequestMapping(value = "/updateDealerInfo", method = RequestMethod.POST)
    public Object updateDealerInfo(@RequestBody JSONObject obj, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = null;
        DealerManage dealerManage = null;
        try {
            request.setCharacterEncoding("UTF-8");
            logger.info("保存车商信息:{}", obj);
            dealerManage = JSONObject.toJavaObject(obj, DealerManage.class);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_FORMATERROR_10000000));
        }
        // 通过订单号修改【地址，联系方式，名称】
        try {
            dealerManageBiz.updateDealerManageInfo(dealerManage);
            linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_UPDATEERROR_10000002));
        }
    }

    /**
     * 是否黑名单
     * 
     * @param obj
     * @param request
     * @return
     */
    @RequestMapping(value = "/judgeBlack", method = RequestMethod.POST)
    public Object judgeBlack(@RequestBody JSONObject obj, HttpServletRequest request) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        int judgeBlackInfo = dealerManageBiz.judgeBlackInfo(obj);
        if (judgeBlackInfo > 0) {
            // 黑名单
            linkedHashMap.put("result", "Y");
        } else {
            linkedHashMap.put("result", "N");
        }
        return super.returnSuccessInfo(linkedHashMap);
    }

    @RequestMapping(value = "/getDealerInfo", method = RequestMethod.POST)
    public Object getDealerInfo(@RequestBody JSONObject obj, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        LinkedHashMap<String, Object> linkedHashMap = null;
        DealerManage dealerManage = null;
        try {
            request.setCharacterEncoding("UTF-8");
            logger.info("保存车商信息:{}", obj);
            dealerManage = JSONObject.toJavaObject(obj, DealerManage.class);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_FORMATERROR_10000000));
        }
        // 通过订单号查询车商信息，在通过车商信息查询车商下的用户是否逾期
        try {
            params.put("orderId", dealerManage.getOrderId());
            DealerManage dealerInfo = dealerManageBiz.getDealerInfo(obj);
            if (dealerInfo != null) {
                // 根据车商查询接口判断是否存在逾期,调用账户系统接口返回OverdueFlag
                // 通过车商名称查询订单
                List<String> allOrderIds = dealerManageBiz.getAllOrderIds(dealerInfo.getSourceCarName());
                if (allOrderIds != null && allOrderIds.size() > 0) {
                    // 调用账户系统查询是否存在逾期
                    Map<String, Object> overdueFlag = getOverdueFlag(allOrderIds);
                    if (overdueFlag == null) {
                        dealerInfo.setOverdueFlag("N");
                    }
                    dealerInfo.setOverdueFlag(overdueFlag.get("result") + "");
                } else {
                    dealerInfo.setOverdueFlag("N");
                }
                linkedHashMap = new LinkedHashMap<String, Object>();
                linkedHashMap.put("result", dealerInfo);
                return super.returnSuccessInfo(linkedHashMap);
            } else {
                // 车商信息不存在
                linkedHashMap = new LinkedHashMap<String, Object>();
                linkedHashMap.put("result", dealerInfo);
                return super.returnSuccessInfo(linkedHashMap);
            }
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_QUERYERROR_10000004));
        }
    }

    /**
     * 通过订单列表查询账户系统是否逾期
     * 
     * @param allOrderIds
     * @return
     */
    private Map<String, Object> getOverdueFlag(List<String> allOrderIds) {
        JSONObject param = new JSONObject();
        param.put("orderIdList", allOrderIds);
        String resultStr;
        try {
            resultStr = SimpleHttpUtils.httpPost(urlConfig.getGetOverdueFlag(), encryptUtils.encrypt(param));
        } catch (Exception e) {
            logger.error("账户系统查询订单是否逾期发生异常", e);
            return null;
        }
        logger.info("账户系统查询订单是否逾期返回结果:{}", resultStr);
        if (StringUtils.isBlank(resultStr)) {
            return null;
        }
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        logger.info("转化Map返回结果:{}", retMap);
        return retMap;
    }

    /**
     * 加入/解除黑名单
     * 
     * @param objs
     * @param request
     * @return
     */
    @RequestMapping(value = "/{method}Black", method = RequestMethod.POST)
    public Object updateDealerInfoStatus(@PathVariable("method") String method, @RequestBody JSONObject objs,
            HttpServletResponse response) {
        LinkedHashMap<String, Object> linkedHashMap = null;
        // 通过ID修改状态 1,正常 2,黑名单
        try {
            logger.info("加入/解除黑名单方法:{} ,参数 {}", method, objs);

            if (method.equals("join")) {
                objs.put("status", CommonConstant.DelerStatus.DelerStatus_2);
            } else if (method.equals("remove")) {
                objs.put("status", CommonConstant.DelerStatus.DelerStatus_1);
            }
            dealerManageBiz.batchUpdateDealerManageInfoStatus(objs);
            linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", "success");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {
            return super.returnFailtrueInfo(new IqbException(DealerManageReturnInfo.DEALER_UPDATEERROR_10000002));
        }
    }

}
