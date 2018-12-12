/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年8月16日下午1:47:27 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.manage.front.order;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseAssetBean;
import com.iqb.consumer.asset.allocation.assetinfo.service.HouseAssetService;
import com.iqb.consumer.data.layer.bean.reqmoney.RequestMoneyBean;
import com.iqb.consumer.service.layer.wfservice.CombinationQueryService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * @author haojinlong 房贷资产分配
 */
@Controller
@RequestMapping("/houseAsset")
public class HouseAssetController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(HouseAssetController.class);
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    @Autowired
    private HouseAssetService houseAssetServiceImpl;
    @Resource
    private CombinationQueryService combinationQueryService;

    /**
     * 
     * Description:查询房贷资产分配信息(带分页)
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectInstSettleConfigResultByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始查询房贷资产分配信息...");
            PageInfo<HouseAssetBean> list = houseAssetServiceImpl.selectHouseAssetByParams(objs);
            logger.debug("---查询房贷资产分配信息完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询房贷资产分配错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:查询房贷资产分配详情
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月16日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/queryDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectHouseAssetDetailByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始查询房贷资产分配详情信息...");
            HouseAssetBean bean = houseAssetServiceImpl.selectHouseAssetDetailByParams(objs);
            logger.debug("---查询房贷资产分配配详信息完成.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", bean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询房贷资产分配配详错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:房贷资产推送
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月22日
     */
    @ResponseBody
    @RequestMapping(value = {"/houseAssetAllot"}, method = {RequestMethod.GET, RequestMethod.POST})
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Throwable.class})
    public Object houseAssetAllot(@RequestBody JSONObject objs, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Integer applyTerm = objs.getInteger("applyTerm"); // 分配期数
            String orderId = objs.getString("orderNo"); // 订单id
            String fundSourceId = objs.getString("fundSourceId");// 资金来源ID
            String applyTime = objs.getString("plantime"); // 资产分配时间
            String desc = objs.getString("desc");// 备注
            String deadline = objs.getString("deadline");// 标的结束时间
            Integer isWithholding = objs.getInteger("isWithholding");
            Integer isPublic = objs.getInteger("isPublic");
            Integer isPushff = objs.getInteger("isPushff");
            String applyAmt = objs.getString("applyAmt");
            logger.info("分配期数:{},资金来源ID:{},资产分配时间:{},描述:{}", applyTerm, fundSourceId, applyTime, desc);

            RequestMoneyBean requestMoneyBean = new RequestMoneyBean();
            requestMoneyBean.setApplyItems(applyTerm);
            requestMoneyBean.setSourcesFunding(fundSourceId);
            requestMoneyBean.setApplyTime(applyTime);
            requestMoneyBean.setStatus(0);
            requestMoneyBean.setOrderId(orderId);
            requestMoneyBean.setRemark(desc);
            requestMoneyBean.setDeadline(deadline);
            requestMoneyBean.setIsWithholding(isWithholding);
            requestMoneyBean.setIsPublic(isPublic);
            requestMoneyBean.setIsPushff(isPushff);
            requestMoneyBean.setApplyAmt(applyAmt);
            long count = combinationQueryService.insertReqMoney(requestMoneyBean);
            if (count > 0) {
                houseAssetServiceImpl.houseAssetAllot(objs);
                result.put("retCode", SUCCESS);
                result.put("retMsg", "房贷资产分配成功");
            } else {
                result.put("retCode", ERROR);
                result.put("retMsg", "房贷资产分配失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("房贷资产推送异常:{}", e);
            result.put("retCode", ERROR);
        }
        return result;
    }
}
