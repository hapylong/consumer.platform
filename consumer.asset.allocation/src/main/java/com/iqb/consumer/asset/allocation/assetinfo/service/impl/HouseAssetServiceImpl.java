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
 * 2017年8月16日下午2:11:27 crw   1.0        1.0 Version 
 * </pre>
 */
package com.iqb.consumer.asset.allocation.assetinfo.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException.Reason;
import com.iqb.consumer.asset.allocation.assetallocine.request.RARRequestMessage;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushRecord;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseAssetBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.HouseOrderBean;
import com.iqb.consumer.asset.allocation.assetinfo.biz.AssetInfoBiz;
import com.iqb.consumer.asset.allocation.assetinfo.biz.HouseAssetBiz;
import com.iqb.consumer.asset.allocation.assetinfo.service.HouseAssetService;
import com.iqb.consumer.asset.allocation.base.AssetAllocationAttr.AssetPushStatus;
import com.iqb.consumer.asset.allocation.base.AssetAllocationAttr.AssetTransferFFJFAttr;
import com.iqb.consumer.asset.allocation.base.config.AssetAllocationfig;
import com.iqb.consumer.asset.allocation.base.AssetAllocationReturnInfo;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.jys.JYSUserBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * @author haojinlong
 * 
 */
@Service
public class HouseAssetServiceImpl extends BaseService implements HouseAssetService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(HouseAssetServiceImpl.class);
    @Resource
    private HouseAssetBiz houseAssetBiz;
    @Autowired
    private AssetInfoBiz assetInfoBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private JysOrderBiz jysOrderBiz;
    @Resource
    private CalculateService calculateService;
    @Resource
    private UserBeanBiz userBiz;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;
    @Autowired
    private RedisPlatformDao redisPlatformDao;
    @Resource(name = "assetAllocationfig")
    private AssetAllocationfig assetAllocationfig;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月16日
     */
    @Override
    public PageInfo<HouseAssetBean> selectHouseAssetByParams(JSONObject objs) {
        logger.debug("---房贷资产分配查询开始---{}", objs);
        makeMerchantNo(objs);
        List<HouseAssetBean> list = houseAssetBiz.selectHouseAssetByParams(objs);
        return new PageInfo<>(list);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月16日
     */
    @Override
    public HouseAssetBean selectHouseAssetDetailByParams(JSONObject objs) {
        logger.debug("---房贷资产详情查询开始---{}", objs);
        return houseAssetBiz.selectHouseAssetDetailByParams(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年8月17日
     */
    @Override
    public Map<String, Object> houseAssetAllot(JSONObject objs) throws IqbException {

        synchronized (this) {
            if (objs.get("orderNo") == null || StringUtils.isEmpty((String) objs.get("orderNo"))) {
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030001);
            }
            HouseOrderBean houseOrderBean = houseAssetBiz.selectHouseOrderByParams(objs);

            AssetPushInfo assetPushInfo = new AssetPushInfo();
            assetPushInfo.setApplyAmt(new BigDecimal(objs.getString("applyAmt")));
            assetPushInfo.setProjectId(houseOrderBean.getProjectId());
            assetPushInfo.setProjectCode(houseOrderBean.getOrderNo());
            assetPushInfo.setProjectName(houseOrderBean.getProjectName());
            assetPushInfo.setApplyItems(houseOrderBean.getApplyTerm());
            assetPushInfo.setReceive(String.valueOf(houseOrderBean.getSource()));
            assetPushInfo.setProjectType("11");
            assetPushInfo.setOrderId(houseOrderBean.getOrderNo());
            assetPushInfo.setOrderName(houseOrderBean.getProjectName());
            assetPushInfo.setMerchantNo(houseOrderBean.getChannelCode());
            assetPushInfo.setBizType(houseOrderBean.getBizType());
            assetPushInfo.setWfStatus(Integer.parseInt(houseOrderBean.getWfStatus()));
            assetPushInfo.setRiskStatus(1);
            assetPushInfo.setPlanId(houseOrderBean.getPlanId());
            assetPushInfo.setDeadline(houseOrderBean.getDeadline());

            assetPushInfo.setGuarantee(houseOrderBean.getGuaranteeCorporationName());
            assetPushInfo.setGuaranteeName(houseOrderBean.getGuaranteeCorporationCorName());
            assetPushInfo.setCheckTime(houseOrderBean.getCheckTime());
            assetPushInfo.setDeadline(houseOrderBean.getDeadline());
            assetPushInfo.setLoanName(houseOrderBean.getUserName());
            assetPushInfo.setLoanIdcard(houseOrderBean.getIdNo());
            assetPushInfo.setCardNum(houseOrderBean.getBankCard());
            assetPushInfo.setBankName(houseOrderBean.getBankName());
            assetPushInfo.setPhone(houseOrderBean.getReserveMobile());
            assetPushInfo.setUserName(houseOrderBean.getUserName());
            assetPushInfo.setIdCard(houseOrderBean.getIdNo());
            assetPushInfo.setMerchantNo(houseOrderBean.getMerchantId());

            assetPushInfo.setSubBizType(houseOrderBean.getSubBizType());
            logger.info("推送至资金端信息：{}", JSONUtil.objToJson(assetPushInfo));
            this.pushAssetBean(assetPushInfo, objs);
        }
        return null;
    }

    /**
     * 房贷资产分配 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月18日
     */
    private void pushAssetBean(AssetPushInfo assetPushInfo, JSONObject objs) throws IqbException {
        AssetPushRecord recordBean = new AssetPushRecord();
        recordBean.setPushTime(DateTools.getCurrTime());
        recordBean.setProjectCode(assetPushInfo.getProjectCode());
        recordBean.setProjectName(assetPushInfo.getProjectName());
        recordBean.setApplyItems(assetPushInfo.getApplyItems());
        recordBean.setApplyAmt(assetPushInfo.getApplyAmt().multiply(new BigDecimal("10000")));
        recordBean.setReceiveType(assetPushInfo.getReceive());
        recordBean.setPushStatus(AssetPushStatus.ASSET_PUSH_STATUS_START);
        this.assetInfoBiz.insertAssetPushRecord(recordBean);
        try {
            if (AssetTransferFFJFAttr.PUSH_ASSET_TO_JYS.equals(assetPushInfo.getReceive())) {
                /** 更新房贷订单表资产分配金额字段 **/
                JSONObject json = new JSONObject();
                json.put("orderNo", assetPushInfo.getOrderId());
                json.put("allotAmt", assetPushInfo.getApplyAmt());
                houseAssetBiz.updateHouseOrderByOrderNo(json);
                /** 查询交易所用户 **/
                long userId = doInsertJysUser(assetPushInfo);
                assetPushInfo.setUserId(String.valueOf(userId));
                doInsertJysOrderInfo(assetPushInfo);
            } else if (AssetTransferFFJFAttr.PUSH_ASSET_TO_FTCG.equals(assetPushInfo.getReceive())) {
                /** 更新房贷订单表资产分配金额字段 **/
                JSONObject json = new JSONObject();
                json.put("orderNo", assetPushInfo.getOrderId());
                json.put("allotAmt", assetPushInfo.getApplyAmt());
                houseAssetBiz.updateHouseOrderByOrderNo(json);
                // 推送到饭团
                pushAssetInfoToFANT(assetPushInfo, objs);
            } else {
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030004);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("推送资金至" + assetPushInfo.getReceive() + "异常", e);
            recordBean.setPushTime(DateTools.getCurrTime());
            recordBean.setPushStatus(AssetPushStatus.ASSET_PUSH_STATUS_ERROE);
            this.assetInfoBiz.insertAssetPushRecord(recordBean);
            this.assetInfoBiz.deleteReqMoney(assetPushInfo.getOrderId());
            if (e instanceof IqbException) {
                throw e;
            }
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030003);
        }
        recordBean.setPushTime(DateTools.getCurrTime());
        recordBean.setPushStatus(AssetPushStatus.ASSET_PUSH_STATUS_END);
        this.assetInfoBiz.insertAssetPushRecord(recordBean);
    }

    /**
     * 
     * Description:保存交易所用户信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private long doInsertJysUser(AssetPushInfo assetPushInfo) {
        // 1.查询订单
        JSONObject params = new JSONObject();
        params.put("orderNo", assetPushInfo.getOrderId());
        HouseOrderBean houseOrderBean = houseAssetBiz.selectHouseOrderByParams(params);

        JYSUserBean ubean = new JYSUserBean();
        ubean.setRealName(houseOrderBean.getUserName());
        ubean.setRegId(houseOrderBean.getMobile());
        ubean.setSmsMobile(houseOrderBean.getMobile());
        ubean.setBankCardNo(houseOrderBean.getBankCard());
        ubean.setBankName(houseOrderBean.getBankName());
        ubean.setStatus("1");
        ubean.setIdNo(houseOrderBean.getIdNo());
        logger.info("推送至交易所-保存用户到jys_user 开始：{}", JSONUtil.objToJson(ubean));
        long userId = userBeanBiz.insertJysUser(ubean);
        logger.info("推送至交易所-保存用户到jys_user 结束：{}", userId);
        return userId;
    }

    /**
     * 
     * Description:执行交易所订单保存
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    private void doInsertJysOrderInfo(AssetPushInfo assetPushInfo) {
        int planId = assetPushInfo.getPlanId();
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(planId);

        JYSOrderBean job = new JYSOrderBean();

        // 1.查询订单
        JSONObject params = new JSONObject();
        params.put("orderNo", assetPushInfo.getOrderId());
        HouseOrderBean houseOrderBean = houseAssetBiz.selectHouseOrderByParams(params);

        try {
            job.setOrderId(assetPushInfo.getOrderId());
            job.setRegId(houseOrderBean.getMobile());
            job.setUserId(assetPushInfo.getUserId());
            job.setMerchantNo(assetPushInfo.getMerchantNo());
            job.setBizType(assetPushInfo.getBizType());
            job.setOrderName(assetPushInfo.getOrderName());
            job.setOrderAmt(String.valueOf(assetPushInfo.getApplyAmt().multiply(new BigDecimal("10000"))));
            job.setOrderItems(String.valueOf(assetPushInfo.getApplyItems()));
            job.setStatus("3");
            job.setWfStatus(assetPushInfo.getWfStatus());
            job.setRiskStatus(assetPushInfo.getRiskStatus());
            job.setPlanId(String.valueOf(planId));
            job.setExpireDate(formatA(assetPushInfo.getDeadline()));
            job.setFee(0);
            job.setPreAmt(String.valueOf(BigDecimal.ZERO));
            job.setMargin(String.valueOf(BigDecimal.ZERO));
            job.setDownPayment(String.valueOf(BigDecimal.ZERO));
            job.setServiceFee(String.valueOf(BigDecimal.ZERO));
            job.setTakePayment(BigDecimal.ZERO);
            job.setFeeYear(0);
            job.setFeeAmount(BigDecimal.ZERO);
            job.setMonthInterest(BigDecimal.ZERO);
            jysOrderBiz.insertJysOrder(job);
        } catch (Exception e) {
            logger.error("---插入交易所信息失败---{}", e);
        }
    }

    /**
     * 
     * Description:将long型时间转化为Date
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private static Date formatA(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time * 1000);
        return c.getTime();
    }

    /**
     * 
     * Description:房贷渠道处理
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年8月23日
     */
    private void makeMerchantNo(JSONObject objs) {
        String merchantNo = objs.getString("merchantNo");
        if (!StringUtil.isNull(merchantNo)) {
            String merchantNos[] = merchantNo.split(",");
            objs.put("merList", merchantNos);
        }
    }

    /**
     * 
     * Description:推送资产到饭团
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月16日
     */
    private int pushAssetInfoToFANT(AssetPushInfo assetPushInfo, JSONObject objs) throws IqbException {
        int result = 0;
        RARRequestMessage rm = new RARRequestMessage();
        Map<String, String> map = assetInfoBiz.selectCustomerBaseInfoById(assetPushInfo.getMerchantNo());
        Map<String, Object> houseMap = assetInfoBiz.getHouseInfoByOrderNo(assetPushInfo.getOrderId());
        logger.info("---houseMap--{}", JSONObject.toJSON(houseMap));
        rm.setProjectId(assetPushInfo.getProjectId());
        rm.setProjectCode(assetPushInfo.getProjectCode());
        rm.setProjectName(assetPushInfo.getProjectName());
        rm.setGuarantee(assetPushInfo.getGuarantee());
        rm.setGuaranteeName(assetPushInfo.getGuaranteeName());

        rm.setCheckTime(assetPushInfo.getCheckTime());
        rm.setProjectType("42");
        rm.setApplyamt(assetPushInfo.getApplyAmt());
        rm.setDeadline(assetPushInfo.getDeadline());
        rm.setLoanName(assetPushInfo.getLoanName());

        rm.setLoanIdcard(assetPushInfo.getLoanIdcard());
        rm.setRaiseTotalAmount(assetPushInfo.getApplyAmt());
        rm.setRaiseObj(assetPushInfo.getGuarantee());
        rm.setCardNum(assetPushInfo.getCardNum());
        rm.setBankName(assetPushInfo.getBankName());

        rm.setPhone(assetPushInfo.getPhone());
        rm.setUserName(assetPushInfo.getUserName());
        rm.setIdCard(assetPushInfo.getIdCard());
        rm.setChannal("1");
        rm.setLoanType(1);
        rm.setTrulyLoanType(1);

        if (!CollectionUtils.isEmpty(map)) {

            rm.setGuaCreditCode(map.get("socialCreditCode") != null ? map.get("socialCreditCode") : "");
            rm.setGuaranteeIdCard(map.get("guaranteeIdCard") != null ? map.get("guaranteeIdCard") : "");
            rm.setGuaCardNum(map.get("guaCardNum") != null ? map.get("guaCardNum") : "");
            rm.setGuaBankCity(map.get("guaBankCity") != null ? map.get("guaBankCity") : "");
            rm.setGuaBankName(map.get("guaBankName") != null ? map.get("guaBankName") : "");
        }
        if (!CollectionUtils.isEmpty(houseMap)) {
            if (houseMap.get("subBizType") != null
                    && (houseMap.get("subBizType").equals("8003") || houseMap.get("subBizType").equals("8004"))) {
                rm.setCreditType(1);
            } else {
                rm.setCreditType(2);
            }
            rm.setHouseAddr(houseMap.get("address").toString());
            rm.setHouseType(1);
            rm.setHouseArea((BigDecimal) houseMap.get("area"));
            rm.setHousePledged(houseMap.get("balance") != null ? 1 : 0);
            rm.setHousePledgedAmount(houseMap.get("balance") != null
                    ? (BigDecimal) houseMap.get("balance")
                    : BigDecimal.ZERO);
            rm.setHouseValuation((BigDecimal) houseMap.get("houseValuation"));
        }
        try {
            chatWithRARServiceInterval(rm);
        } catch (InvalidRARException e) {
            logger.error("---房贷资产推送到饭团异常---{}", e);
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030003);
        }
        return result;
    }

    /**
     * 
     * Description:将房贷信息放入redis
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月22日
     */
    private void chatWithRARServiceInterval(RARRequestMessage rm)
            throws InvalidRARException {
        try {
            Map<String, Object> map = rm.createMapString();
            logger.info("---推送给饭团数据为:---{}", JSONObject.toJSONString(map));
            redisPlatformDao.leftPush(assetAllocationfig.getRedisQueueAssetAllocationKey(), JSONUtil.objToJson(map));
        } catch (Throwable e) {
            throw new InvalidRARException(Reason.INVALID_RESPONSE);
        }
    }
}
