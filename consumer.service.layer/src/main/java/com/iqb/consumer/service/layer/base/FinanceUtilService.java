package com.iqb.consumer.service.layer.base;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.FinanceConstant.ResultConstant;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.penalty.derate.PenaltyDerateBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.penalty.derate.PenaltyDerateBiz;
import com.iqb.consumer.service.layer.page.PageMsg;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * 
 * Description: 与账户系统交互时通用的方法
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@SuppressWarnings("unchecked")
@Component
public class FinanceUtilService {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(FinanceUtilService.class);

    @Autowired
    private MerchantBeanBiz merchantBiz;
    @Autowired
    private SysUserSession sysUserSession;
    @Resource
    private EncryptUtils encryptUtils;

    @Autowired
    private PenaltyDerateBiz penaltyDerateBiz;

    // 获取登录商户下所有子商户列表
    private List<MerchantBean> getAllMerListByOrgCode(JSONObject objs) {
        String orgCode = sysUserSession.getOrgCode() == null ? "1" : sysUserSession.getOrgCode();
        objs.put("id", orgCode);
        List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
        return list;
    }

    // 获取商户权限的Objs
    public JSONObject getMerchLimitObject(JSONObject objs) {
        // 权限树修改
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
        if (merchNames == null || "".equals(merchNames) || "全部商户".equals(merchNames)) {
            if ("1".equals(objs.getString("id"))) {
                objs.put("merList", null);
            } else {
                List<MerchantBean> merchList = getAllMerListByOrgCode(objs);
                if (merchList == null || merchList.size() == 0) {
                    return null;
                }
                objs.put("merList", merchList);
            }
        } else {
            List<MerchantBean> merchList = merchantBiz.getMerCodeByMerSortNameList(objs);
            objs.put("merList", merchList);
        }

        return objs;
    }

    /**
     * 
     * Description: 获取查询日期区间
     * 
     * @param
     * @return Map<String,String>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午4:27:19
     */
    public Map<String, String> getStartAndEndDate(JSONObject objs) {
        Map<String, String> result = new HashMap<String, String>();
        String startDate = objs.getString("startDate");
        String endDate = objs.getString("endDate");
        String curRepayDate = objs.getString("curRepayDate");
        if (startDate == null || "".equals(startDate)) {
            startDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        } else {
            Date startTime = DateUtil.parseDate(startDate, DateUtil.SHORT_DATE_FORMAT);
            startDate = DateUtil.getDateString(startTime, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (endDate == null || "".equals(endDate)) {
            endDate = DateUtil.getDateString(new Date(), DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        } else {
            Date endTime = DateUtil.parseDate(endDate, DateUtil.SHORT_DATE_FORMAT);
            endDate = DateUtil.getDateString(endTime, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        if (curRepayDate != null && !"".equals(curRepayDate)) {
            Date crd = DateUtil.parseDate(curRepayDate, DateUtil.SHORT_DATE_FORMAT);
            curRepayDate = DateUtil.getDateString(crd, DateUtil.SHORT_DATE_FORMAT_NO_DASH);
        }
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("curRepayDate", curRepayDate);
        return result;
    }

    /**
     * 
     * Description: 与账户系统进行交互
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午4:30:47
     */
    public String transferWithFinance(String url, String params) {
        logger.info("与账户系统进行交互发送数据请求地址：{}", url);
        logger.info("与账户系统进行交互发送数据：{}", JSONObject.toJSONString(params));
        String retStr = SimpleHttpUtils.httpPost(url, encryptUtils.encrypt(params));
        logger.info("与账户系统进行交互返回数据：{}", retStr);
        return retStr;
    }

    /**
     * 
     * Description: 判断账户系统是否返回成功
     * 
     * @param
     * @return Boolean
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午4:41:17
     */
    public Boolean isFinanceRetSucc(String retStr) {
        if (StringUtil.isEmpty(retStr)) {
            return false;
        }
        JSONObject objs = JSONObject.parseObject(retStr);
        if (ResultConstant.RETCODE_ERROR.equals(objs.getString(ResultConstant.RETCODE_KEY))) {
            return false;
        }
        return true;
    }

    /**
     * 
     * Description: 获取列表
     * 
     * @param
     * @return PageInfo<Map<String,Object>>
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午5:10:49
     */
    public PageInfo<Map<String, Object>> getFinanceRetList(String retStr) throws IqbException {
        logger.info("---调用账务系统账单查询接口返回数据---{}", retStr);
        JSONObject objs = JSONObject.parseObject(retStr);
        JSONObject resultMap = objs.getJSONObject(ResultConstant.RESULT_KEY);
        if (resultMap == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_FINANCE_02000007);
        }
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) resultMap.get(ResultConstant.RECORDLIST_KEY);

        Integer totalCount = resultMap.getInteger(ResultConstant.TOTALCOUNT_KEY);
        Integer numPerPage = resultMap.getInteger(ResultConstant.NUMPERPAGE_KEY);
        Integer currentPage = resultMap.getInteger(ResultConstant.CURRENTPAGE_KEY);
        // 如果查询单条，需要 查询 商户和 订单金额信息
        if (totalCount == 1) {
            String orderId = recordList.get(0).get("orderId").toString();
            if (!StringUtil.isEmpty(orderId)) {
                PenaltyDerateBean pdb = penaltyDerateBiz.getAmtAndMerName(orderId);
                if (pdb != null) {
                    recordList.get(0).put("merchantShortName", pdb.getMerchantShortName());
                    recordList.get(0).put("orderAmt", pdb.getOrderAmt());
                }
            }
        }
        PageInfo<Map<String, Object>> pageInfo =
                new PageMsg<Map<String, Object>>(recordList, 8, currentPage, totalCount, numPerPage);
        return pageInfo;
    }

}
