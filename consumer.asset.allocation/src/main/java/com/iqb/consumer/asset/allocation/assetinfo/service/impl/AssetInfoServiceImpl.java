package com.iqb.consumer.asset.allocation.assetinfo.service.impl;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException;
import com.iqb.consumer.asset.allocation.assetallocine.exception.InvalidRARException.Reason;
import com.iqb.consumer.asset.allocation.assetallocine.request.RARRequestMessage;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetInfoFormBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetObjectInfoBean;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushInfo;
import com.iqb.consumer.asset.allocation.assetinfo.bean.AssetPushRecord;
import com.iqb.consumer.asset.allocation.assetinfo.bean.ImageBean;
import com.iqb.consumer.asset.allocation.assetinfo.biz.AssetInfoBiz;
import com.iqb.consumer.asset.allocation.assetinfo.service.IAssetInfoService;
import com.iqb.consumer.asset.allocation.base.AssetAllocationAttr;
import com.iqb.consumer.asset.allocation.base.AssetAllocationAttr.AssetPushStatus;
import com.iqb.consumer.asset.allocation.base.AssetAllocationAttr.AssetTransferFFJFAttr;
import com.iqb.consumer.asset.allocation.base.AssetAllocationAttr.AssetTransferIQBAttr;
import com.iqb.consumer.asset.allocation.base.AssetAllocationReturnInfo;
import com.iqb.consumer.asset.allocation.base.config.AssetAllocationfig;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.jys.JYSCreditInfoBean;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.jys.JYSUserBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.order.JysCreditInfoBiz;
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.Cryptography;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.https.HttpsClientUtil;

/**
 * 
 * Description: 资产分配实现类
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月29日    wangxinbang       1.0        1.0 Version
 *          </pre>
 */
@SuppressWarnings("rawtypes")
@Service
public class AssetInfoServiceImpl extends BaseService implements IAssetInfoService {

    private static Logger logger = LoggerFactory.getLogger(AssetInfoServiceImpl.class);

    private static final String CUSTOMERCODE_KEY = "customerCode";

    private static final String RECEIVE_KEY = "receive";

    private static final String RECEIVE_VAL = "1";

    @Autowired
    private AssetInfoBiz assetInfoBiz;

    @Resource(name = "assetAllocationfig")
    private AssetAllocationfig assetAllocationfig;
    @Autowired
    private RedisPlatformDao redisPlatformDao;
    @Autowired
    private CustomerBiz customerBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private JysOrderBiz jysOrderBiz;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private UserBeanBiz userBiz;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;
    @Resource
    private CalculateService calculateService;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private JysCreditInfoBiz jysCreditInfoBiz;
    @Resource
    private ConsumerConfig consumerConfig;

    @Override
    public PageInfo<AssetInfoBean> getAssetInfoList(JSONObject objs) throws IqbException {
        this.convertQueryTime(objs);
        try {
            JSONUtil.formatDateTime(objs);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new PageInfo<AssetInfoBean>(this.assetInfoBiz.getAssetInfoList(objs));
    }

    /**
     * 
     * Description: 日期查询预处理
     * 
     * @param
     * @return void
     * @throws @Author wangxinbang Create Date: 2016年9月30日 上午10:35:10
     */
    private void convertQueryTime(JSONObject objs) {
        if (CollectionUtils.isEmpty(objs)) {
            return;
        }
        if (StringUtil.isNotEmpty(objs.getString("queryStartOrderTime"))) {
            objs.put("queryStartOrderTime", objs.getString("queryStartOrderTime") + " 00:00:00");
        }
        if (StringUtil.isNotEmpty(objs.getString("queryEndOrderTime"))) {
            objs.put("queryEndOrderTime", objs.getString("queryEndOrderTime") + " 23:59:59");
        }
    }

    /**
     * Description:查看资产详细信息
     */
    @Override
    public AssetInfoFormBean getAssetInfoDetails(String orderId) throws IqbException {
        return this.assetInfoBiz.getAssetInfoDetails(orderId);
    }

    /**
     * Description:校验申请期数
     */
    @Override
    public Map<Integer, Integer> validataRequesttimes(String orderId) throws IqbException {
        return this.assetInfoBiz.validataRequesttimes(orderId);
    }

    /**
     * Description: 推送资产
     */
    @Override
    public void pushAssetInfoToPlatform(Map beanMap, JSONObject objs) throws IqbException {
        synchronized (this) {
            if (beanMap.get("orderId") == null || StringUtils.isEmpty((String) beanMap.get("orderId"))) {
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030001);
            }
            AssetPushInfo assetPushInfo = this.assetInfoBiz.getPushAssetInfo(beanMap);

            /** 校验返回信息 **/
            if (assetPushInfo == null || StringUtil.isEmpty(assetPushInfo.getProjectCode())) {
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030002);
            }
            this.pushAssetBean(assetPushInfo, objs);
        }
    }

    private void pushAssetBean(AssetPushInfo assetPushInfo, JSONObject objs) throws IqbException {
        AssetPushRecord recordBean = new AssetPushRecord();
        recordBean.setProjectId(assetPushInfo.getProjectId());
        recordBean.setPushTime(DateTools.getCurrTime());
        recordBean.setProjectCode(assetPushInfo.getProjectCode());
        recordBean.setProjectName(assetPushInfo.getProjectName());
        recordBean.setApplyItems(assetPushInfo.getApplyItems());
        recordBean.setApplyAmt(assetPushInfo.getApplyAmt());
        recordBean.setReceiveType(assetPushInfo.getReceive());
        recordBean.setPushStatus(AssetPushStatus.ASSET_PUSH_STATUS_START);
        this.assetInfoBiz.insertAssetPushRecord(recordBean);
        try {
            if (AssetTransferFFJFAttr.PUSH_ASSET_TO_FFJS.equals(assetPushInfo.getReceive())) {
                logger.info("推送资金开始，资金信息" + JSONUtil.objToJson(recordBean));
                Map<String, String> mapBean = this.serialFFJFMap(assetPushInfo);
                mapBean.put("msgType", "asset_allocation");
                redisPlatformDao.leftPush(assetAllocationfig.getRedisQueueAssetAllocationKey(),
                        JSONUtil.objToJson(mapBean));
            } else if (AssetTransferFFJFAttr.PUSH_ASSET_TO_IQB.equals(assetPushInfo.getReceive())) {
                /** 执行用户注册 **/
                this.doIQBReg(assetPushInfo);
                /** 执行资产推送 **/
                this.doAssertPushToIQB(assetPushInfo);
            } else if (AssetTransferFFJFAttr.PUSH_ASSET_TO_JYS.equals(assetPushInfo.getReceive())) {
                /** 插入交易所用户 **/
                long userId = doInsertJysUser(assetPushInfo);
                assetPushInfo.setUserId(String.valueOf(userId));

                /** 插入交易所债权人信息 **/
                long creditorId = insertJysCreditInfo(objs);

                /** 插入交易所订单信息 **/
                assetPushInfo.setCreditorId(creditorId);
                doInsertJysOrderInfo(assetPushInfo);

            } else if (AssetTransferFFJFAttr.PUSH_ASSET_TO_FTCG.equals(assetPushInfo.getReceive())) {
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
     * Description: 执行资产推送
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月5日 下午5:07:00
     */
    private void doAssertPushToIQB(AssetPushInfo assetPushInfo) throws IqbException {
        Map<String, Object> mapBean = this.serialIQBMap(assetPushInfo);
        logger.info("资产推送数据信息:{}", JSONObject.toJSONString(mapBean));
        mapBean.put("msgType", "asset_allocation");
        mapBean.put(RECEIVE_KEY, RECEIVE_VAL);
        JSONObject objs = JSONObject.parseObject(JSONObject.toJSONString(mapBean));
        logger.info("资产推送-调用爱钱帮资产推送接口请求信息：{}", JSONObject.toJSONString(objs));
        redisPlatformDao.leftPush(assetAllocationfig.getRedisQueueAssetAllocationKey(), JSONUtil.objToJson(objs));
    }

    /**
     * 
     * Description: 序列化爱钱帮信息map
     * 
     * @param
     * @return Map<String,String>
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 上午11:23:18
     */
    private Map<String, Object> serialIQBMap(AssetPushInfo assetPushInfo) throws IqbException {
        /** 从数据库中查询出资产信息 **/
        Map<String, Object> assetInfoMap = this.assetInfoBiz.getAssertInfoForIQB(assetPushInfo.getOrderId());
        logger.info("从数据库中查询出资产信息:{}", JSONObject.toJSONString(assetInfoMap));
        if (assetInfoMap == null) {
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030002);
        }

        /** 获取调用爱钱帮接口的请求信息map **/
        Map<String, Object> reqMap = this.getIqbReqMap(assetInfoMap);
        logger.info("调用爱钱帮接口的请求信息map:{}", JSONObject.toJSONString(reqMap));

        return reqMap;
    }

    /**
     * 
     * Description: 获取调用爱钱帮接口的请求信息map
     * 
     * @param
     * @return Map<String,Object>
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午4:13:47
     */
    private Map<String, Object> getIqbReqMap(Map<String, Object> assetInfoMap) throws IqbException {
        /** 发送爱钱帮请求信息 **/
        Map<String, Object> reqMap = new HashMap<String, Object>();
        Map<String, Object> baseDataMap = new HashMap<String, Object>();

        /** 借款信息 **/
        Map<String, Object> borrowInfo = new HashMap<String, Object>();
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_ORDER_ID, assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_ORDER_ID));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_BORROW_NAME,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_NAME));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_BORROW_CODE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_CODE));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_BUSINESS_TYPES,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BUSINESS_TYPES));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY,
                ObjectUtils.toString(assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY)));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_BORROW_DURATION,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_DURATION));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_REPAYMENT_TYPE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_REPAYMENT_TYPE));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_REPAYMENT_DAY,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_REPAYMENT_DAY));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_IS_WITHHOLDING,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_IS_WITHHOLDING));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_PLATFORM_CHARGE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_PLATFORM_CHARGE));
        borrowInfo.put(AssetTransferIQBAttr.IQB_KEY_TOP_UP_RATE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_TOP_UP_RATE));
        borrowInfo
                .put(AssetTransferIQBAttr.IQB_KEY_IS_PUBLIC, assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_IS_PUBLIC));
        borrowInfo
                .put(AssetTransferIQBAttr.IQB_KEY_IS_PUSHFF, assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_IS_PUSHFF));
        borrowInfo
                .put(AssetTransferIQBAttr.IQB_KEY_REAL_REPAYMENT_DATE,
                        assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_REAL_REPAYMENT_DATE));
        /** 校验借款信息 **/
        this.validataMapExitEmpty(borrowInfo);

        baseDataMap.put(AssetTransferIQBAttr.IQB_KEY_BORROWINFO, borrowInfo);

        /** 个人信息 **/
        Map<String, Object> borrower = new HashMap<String, Object>();
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MODEL,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_MODEL));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_REALNAME,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_REALNAME));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_IDCARD,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_IDCARD));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_PHONE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_PHONE));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_BINDCARDID,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_BINDCARDID));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_NAME,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_NAME));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_IDCARD,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_IDCARD));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_PHONE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_PHONE));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_SEX, this.IdNOToSex(ObjectUtils.toString(assetInfoMap
                .get(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_IDCARD))));
        borrower.put(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_AGE, this.IdNOToAge(ObjectUtils.toString(assetInfoMap
                .get(AssetTransferIQBAttr.IQB_KEY_BORROW_MONEY_IDCARD))));
        /** 校验借款信息 **/
        this.validataMapExitEmpty(borrower);

        baseDataMap.put(AssetTransferIQBAttr.IQB_KEY_BORROWUSER, borrower);

        /** 抵押物信息 **/
        Map<String, Object> collateralInformation = new HashMap<String, Object>();
        String orderName = ObjectUtils.toString(assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_ORDER_NAME));
        if (StringUtil.isNotEmpty(orderName)) {
            String[] orderNames = orderName.split("-");
            String borrowBrand = orderNames[0];
            String borrowCarkinds = orderNames[1];
            collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_BORROW_BRAND, borrowBrand);
            collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_BORROW_CARKINDS, borrowCarkinds);
        }
        collateralInformation
                .put(AssetTransferIQBAttr.IQB_KEY_REGISTDATE,
                        assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_REGISTDATE));
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_REGISTDATE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_REGISTDATE));
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_CARNO,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_CARNO));
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_MILEAGE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_MILEAGE));
        String borrowReferenceprice = ObjectUtils.toString(assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_ASSESSPRICE));
        collateralInformation
                .put(AssetTransferIQBAttr.IQB_KEY_ASSESSPRICE,
                        new BigDecimal(borrowReferenceprice == null ? "0" : borrowReferenceprice)
                                .divide(new BigDecimal(10000)));
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_IS_EXIT_LOAN,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_IS_EXIT_LOAN));
        List<ImageBean> imgList =
                this.assetInfoBiz.getImgList(ObjectUtils.toString(assetInfoMap
                        .get(AssetTransferIQBAttr.IQB_KEY_ORDER_ID)));
        logger.info("根据订单id获取图片列表:{}", JSONObject.toJSONString(imgList));
        Map<Integer, Object> imgMap = this.getImgMapFromList(imgList);
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_VEHICLE_PROCEDURE, this.getVehicleProcedureImg(imgMap));
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_IMG_LOGO, this.getLogoImg(imgMap));
        collateralInformation.put(AssetTransferIQBAttr.IQB_KEY_IMG_PHYSICAL, this.getPhysicalImg(imgMap));
        /** 校验借款信息 **/
        this.validataMapExitEmpty(collateralInformation);

        baseDataMap.put(AssetTransferIQBAttr.IQB_KEY_COLLATERALINFORMATION, collateralInformation);

        /** 风控信息 **/
        Map<String, Object> riskInformation = new HashMap<String, Object>();
        riskInformation.put(
                AssetTransferIQBAttr.IQB_KEY_IQB_INFO,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_IQB_INFO) == null ? "借款用途" : assetInfoMap
                        .get(AssetTransferIQBAttr.IQB_KEY_IQB_INFO));
        /** 校验借款信息 **/
        this.validataMapExitEmpty(riskInformation);

        baseDataMap.put(AssetTransferIQBAttr.IQB_KEY_RISKINFORMATION, riskInformation);

        /** 担保公司信息 **/
        Map<String, Object> guaranteeCompanyInformation = new HashMap<String, Object>();
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_NAME,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_NAME));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_SIMPLE_NAME,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_SIMPLE_NAME));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_SOCIALCREDITCODE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_SOCIALCREDITCODE));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_ICREGCODE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_ICREGCODE));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_ORGANIZATIONCODE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_ORGANIZATIONCODE));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_TAXCERTIFICATECODE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_TAXCERTIFICATECODE));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_PHONE,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_PHONE));
        guaranteeCompanyInformation.put(AssetTransferIQBAttr.IQB_KEY_GR_INTRODUCTION,
                assetInfoMap.get(AssetTransferIQBAttr.IQB_KEY_GR_INTRODUCTION));
        /** 校验借款信息 **/
        this.validataMapExitEmpty(guaranteeCompanyInformation);

        baseDataMap.put(AssetTransferIQBAttr.IQB_KEY_GUARANTEECOMPANYINFORMATION, guaranteeCompanyInformation);

        /** 加密运算 **/
        JSONObject objs = JSONObject.parseObject(JSONObject.toJSONString(baseDataMap));
        String sign = Cryptography.encrypt(JSONObject.toJSONString(objs));
        reqMap.put(AssetTransferIQBAttr.IQB_KEY_SIGN, sign);
        reqMap.put(AssetTransferIQBAttr.IQB_KEY_BASEDATA, baseDataMap);

        return reqMap;
    }

    /**
     * 
     * Description: 校验请求map是否有字段为空
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月13日 上午9:53:45
     */
    private void validataMapExitEmpty(Map<String, Object> reqMap) throws IqbException {
        Set<String> keys = reqMap.keySet();
        for (String key : keys) {
            if (reqMap.get(key) == null) {
                logger.info("资产分配推送爱钱帮请求字段：" + key + "为空，资产分配信息为：" + JSONObject.toJSONString(reqMap));
                logger.error("资产分配推送爱钱帮请求字段：" + key + "为空，资产分配信息为：" + JSONObject.toJSONString(reqMap));
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030012);
            }
        }
    }

    /**
     * 
     * Description: 从图片列表中获取图片map
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月7日 上午11:10:25
     */
    private Map<Integer, Object> getImgMapFromList(List<ImageBean> imgList) {
        Map<Integer, Object> imgMap = new HashMap<Integer, Object>();
        for (ImageBean imageBean : imgList) {
            /** 获取图片路径,并判断其是否已经存在 **/
            String imgPath = ObjectUtils.toString(imgMap.get(imageBean.getImgType()));
            if (StringUtil.isEmpty(imgPath)) {
                imgMap.put(imageBean.getImgType(), AssetTransferIQBAttr.IQB_KEY_UPLOADDATA + imageBean.getImgPath());
                continue;
            }
            imgMap.put(imageBean.getImgType(),
                    imgPath + "," + AssetTransferIQBAttr.IQB_KEY_UPLOADDATA + imageBean.getImgPath());
        }
        return imgMap;
    }

    /**
     * 
     * Description: 获取车辆手续照片
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午5:55:28
     */
    private Map<Integer, Object> getVehicleProcedureImg(Map<Integer, Object> imgMap) {
        Integer[] vehicleProcedureImgArr = {15, 11, 10, 19, 9, 20, 14, 7, 999, 16};
        Map<Integer, Object> vehicleProcedureImgMap = new HashMap<Integer, Object>();
        for (Integer vehicleProcedureImg : vehicleProcedureImgArr) {
            Object obj = imgMap.get(vehicleProcedureImg);
            if (obj == null) {
                continue;
            }
            switch (vehicleProcedureImg) {
                case 15:
                    vehicleProcedureImg = 1;
                    break;
                case 11:
                    vehicleProcedureImg = 2;
                    break;
                case 10:
                    vehicleProcedureImg = 3;
                    break;
                case 19:
                    vehicleProcedureImg = 4;
                    break;
                case 9:
                    vehicleProcedureImg = 5;
                    break;
                case 20:
                    vehicleProcedureImg = 6;
                    break;
                case 14:
                    vehicleProcedureImg = 7;
                    break;
                case 7:
                    vehicleProcedureImg = 8;
                    break;
                case 16:
                    vehicleProcedureImg = 10;
                    break;
                default:
                    break;
            }
            vehicleProcedureImgMap.put(vehicleProcedureImg, obj);
        }
        return vehicleProcedureImgMap;
    }

    /**
     * 
     * Description: 获取项目logo图片
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午6:15:35
     */
    private Map<Integer, Object> getLogoImg(Map<Integer, Object> imgMap) {
        Integer[] logoImgArr = {28};
        Map<Integer, Object> logoImgMap = new HashMap<Integer, Object>();
        for (Integer logoImg : logoImgArr) {
            logoImgMap.put(1, imgMap.get(logoImg));
        }
        return logoImgMap;
    }

    /**
     * 
     * Description: 获取实物图信息
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午6:15:52
     */
    private Map<Integer, Object> getPhysicalImg(Map<Integer, Object> imgMap) {
        Integer[] physicalImgArr = {0, 2, 3, 4, 5, 6};
        Map<Integer, Object> physicalImgMap = new HashMap<Integer, Object>();
        for (Integer physicalImg : physicalImgArr) {
            Object obj = imgMap.get(physicalImg);
            if (obj == null) {
                continue;
            }
            switch (physicalImg) {
                case 0:
                    physicalImg = 1;
                    break;
                case 2:
                    physicalImg = 2;
                    break;
                case 3:
                    physicalImg = 3;
                    break;
                case 4:
                    physicalImg = 4;
                    break;
                case 5:
                    physicalImg = 5;
                    break;
                case 6:
                    physicalImg = 6;
                    break;
                default:
                    break;
            }
            physicalImgMap.put(physicalImg, obj);
        }
        return physicalImgMap;
    }

    /**
     * 
     * Description: 根据身份证获取年龄
     * 
     * @param
     * @return int
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午5:04:41
     */
    private int IdNOToAge(String IdNO) {
        int leh = IdNO.length();
        String dates = "";
        if (leh == 18) {
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int u = Integer.parseInt(year) - Integer.parseInt(dates);
            return u;
        } else {
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }

    }

    /**
     * 
     * Description: 根据身份证获取性别
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年4月6日 下午5:21:29
     */
    private String IdNOToSex(String IdNO) {
        int leh = IdNO.length();
        String sexNum = IdNO.substring(leh - 2, leh - 1);
        if (Integer.parseInt(sexNum) % 2 == 0) {
            return "女";
        } else {
            return "男";
        }
    }

    /**
     * 
     * Description: 执行爱钱帮的用户注册
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月5日 下午1:59:01
     */
    private void doIQBReg(AssetPushInfo assetPushInfo) throws IqbException {
        /** 获取担保人信息map **/
        Map<String, Object> guarantorMap = this.getGuarantorMap(assetPushInfo);
        /** 获取借款人信息map **/
        Map<String, Object> onlionBorrowerMap = this.getOnlionBorrowerMap(assetPushInfo);
        /** 请求map **/
        Map<String, Object> basedataMap = new HashMap<String, Object>();
        basedataMap.put(AssetTransferIQBAttr.IQB_KEY_BORROWUSER, onlionBorrowerMap);
        basedataMap.put(AssetTransferIQBAttr.IQB_KEY_GUARANTEEUSER, guarantorMap);
        /** 加密运算 **/
        String sign = Cryptography.encrypt(JSONObject.toJSONString(basedataMap));
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put(AssetTransferIQBAttr.IQB_KEY_BASEDATA, basedataMap);
        reqMap.put(AssetTransferIQBAttr.IQB_KEY_SIGN, sign);
        logger.info("资产推送-调用爱钱帮注册接口请求信息：{}", JSONObject.toJSONString(reqMap));
        String result =
                HttpsClientUtil.getInstance().doPost(this.assetAllocationfig.getUrlOfIQBReg(),
                        JSONObject.toJSONString(reqMap), "utf-8");
        logger.info("资产推送-调用爱钱帮注册接口返回信息：{}", result);
        this.dealIQBRetInfo(result);
    }

    /**
     * 
     * Description: 处理爱钱帮返回信息
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月5日 下午4:11:05
     */
    private void dealIQBRetInfo(String result) throws IqbException {
        /** 判断返回结果是否为空 **/
        if (StringUtil.isEmpty(result)) {
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030008);
        }
        JSONObject objs = JSONObject.parseObject(result);
        /** 判断爱钱帮返回结果 **/
        if (!AssetTransferIQBAttr.IQB_VALUE_SUCCESS.equals(objs.getString(AssetTransferIQBAttr.IQB_KEY_SUCCESS))) {
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030009);
        }
    }

    /**
     * 
     * Description: 获取担保人信息
     * 
     * @param
     * @return Map<String,Object>
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年4月5日 下午3:27:40
     */
    private Map<String, Object> getGuarantorMap(AssetPushInfo assetPushInfo) throws IqbException {
        Map<String, Object> guarantorMap = new HashMap<String, Object>();
        /** 获取商户号，并进行数据校验 **/
        String merchantNo = assetPushInfo.getMerchantNo();
        if (StringUtil.isEmpty(merchantNo)) {
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030010);
        }
        /** 封装客户信息查询参数 **/
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(CUSTOMERCODE_KEY, merchantNo);
        CustomerBean customerBean = customerBiz.getCustomerInfoByCustmerCode(map);
        guarantorMap.put(AssetTransferIQBAttr.IQB_KEY_GR_NAME, assetPushInfo.getGuarantee());
        guarantorMap.put(AssetTransferIQBAttr.IQB_KEY_GR_SOCIALCREDITCODE, customerBean.getSocialCreditCode());
        guarantorMap.put(AssetTransferIQBAttr.IQB_KEY_GR_ICREGCODE, customerBean.getIcRegCode());
        guarantorMap.put(AssetTransferIQBAttr.IQB_KEY_GR_ORGANIZATIONCODE, customerBean.getOrganizationCode());
        guarantorMap.put(AssetTransferIQBAttr.IQB_KEY_GR_TAXCERTIFICATECODE, customerBean.getTaxCertificateCode());
        return guarantorMap;
    }

    /**
     * 
     * Description: 获取借款人信息
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月5日 下午3:27:40
     */
    private Map<String, Object> getOnlionBorrowerMap(AssetPushInfo assetPushInfo) {
        Map<String, Object> onlionBorrowerMap = new HashMap<String, Object>();
        onlionBorrowerMap.put(AssetTransferIQBAttr.IQB_KEY_BORROW_REALNAME, assetPushInfo.getLoanName());
        onlionBorrowerMap.put(AssetTransferIQBAttr.IQB_KEY_BORROW_IDCARD, assetPushInfo.getLoanIdcard());
        onlionBorrowerMap.put(AssetTransferIQBAttr.IQB_KEY_BORROW_PHONE, assetPushInfo.getPhone());
        onlionBorrowerMap.put(AssetTransferIQBAttr.IQB_KEY_BORROW_BINDCARDID, assetPushInfo.getCardNum());
        return onlionBorrowerMap;
    }

    /**
     * 
     * Description: 格式化推送饭饭接口数据
     * 
     * @param
     * @return Map<String,String>
     * @throws
     * @Author wangxinbang Create Date: 2017年4月5日 上午11:26:48
     */
    private Map<String, String> serialFFJFMap(AssetPushInfo assetPushInfo) {
        Map<String, String> retMap = new HashMap<String, String>();
        retMap.put("projectId", String.valueOf(assetPushInfo.getProjectId()));
        retMap.put("projectCode", assetPushInfo.getProjectCode());
        retMap.put("projectName", assetPushInfo.getProjectName());
        retMap.put("userName", assetPushInfo.getUserName());
        retMap.put("idCard", assetPushInfo.getIdCard());
        retMap.put("guarantee", assetPushInfo.getGuarantee());
        retMap.put("guaranteeName", assetPushInfo.getGuaranteeName());
        retMap.put("loanName", assetPushInfo.getLoanName());
        retMap.put("loanIdcard", assetPushInfo.getLoanIdcard());
        retMap.put("checkTime", String.valueOf(assetPushInfo.getCheckTime()));
        retMap.put("applyamt", String.valueOf(assetPushInfo.getApplyAmt()));
        retMap.put("projectType", assetPushInfo.getProjectType());
        retMap.put("deadline", String.valueOf(assetPushInfo.getDeadline()));
        retMap.put("cardNum", String.valueOf(assetPushInfo.getCardNum()));
        retMap.put("bankName", String.valueOf(assetPushInfo.getBankName()));
        retMap.put("phone", String.valueOf(assetPushInfo.getPhone()));
        retMap.put("receive", String.valueOf(assetPushInfo.getReceive()));
        return retMap;
    }

    @Override
    public void pushAssetIntoToRedis(Map beanMap, JSONObject objs) throws IqbException {
        synchronized (this) {
            if (beanMap.get("orderId") == null || StringUtils.isEmpty((String) beanMap.get("orderId"))) {
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030001);
            }
            AssetPushInfo assetPushInfo = this.assetInfoBiz.getPushAssetInfo(beanMap);
            if (beanMap.containsKey("bizType") && beanMap.get("bizType").equals("3001")) { // 蒲公英
                assetPushInfo.setProjectType("11");
            }
            /** 校验返回信息 **/
            if (assetPushInfo == null || StringUtil.isEmpty(assetPushInfo.getProjectCode())) {
                throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030002);
            }
            logger.info("推送至资金端信息：{}", JSONUtil.objToJson(assetPushInfo));
            /**
             * FINANCE-2292 资产分配页面，债权人信息可下拉选择，并将修改后的债权人信息推送到资金端
             */
            assetPushInfo.setLoanName(objs.getString("creditName"));
            assetPushInfo.setLoanIdcard(objs.getString("creditCardNo"));
            assetPushInfo.setCardNum(objs.getString("creditBankCard"));
            assetPushInfo.setBankName(objs.getString("creditBank"));
            assetPushInfo.setPhone(objs.getString("creditPhone"));
            assetPushInfo.setCf_requestmoney_id(beanMap.get("requestId").toString());
            this.pushAssetBean(assetPushInfo, objs);
        }
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
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orderId", assetPushInfo.getOrderId());
        OrderBean orderBean = orderBiz.selectOne(params);
        // 2.查询用户信息
        UserBean userBean = userBiz.getUserInfo(Long.parseLong(orderBean.getUserId()));
        // 3.查询银行卡信息
        BankCardBean bankCardBean = bankCardBeanBiz.getOpenBankCardByRegId(String.valueOf(userBean.getId()));
        JYSUserBean ubean = new JYSUserBean();
        ubean.setRealName(userBean.getRealName());
        ubean.setRegId(userBean.getRegId());
        ubean.setSmsMobile(userBean.getSmsMobile());
        ubean.setBankCardNo(bankCardBean.getBankCardNo());
        ubean.setBankName(bankCardBean.getBankName());
        ubean.setStatus("1");
        ubean.setIdNo(userBean.getIdNo());
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
    private long doInsertJysOrderInfo(AssetPushInfo assetPushInfo) {
        int planId = assetPushInfo.getPlanId();
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(planId);
        Map<String, BigDecimal> detail = calculateService.calculateAmt(planBean, assetPushInfo.getApplyAmt());// getDetail(assetPushInfo.getApplyAmt(),
                                                                                                              // planBean);
        JYSOrderBean job = new JYSOrderBean();
        if (detail != null) {
            // 1.查询订单
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", assetPushInfo.getOrderId());
            OrderBean orderBean = orderBiz.selectOne(params);

            job.setOrderId(assetPushInfo.getOrderId());
            job.setRegId(orderBean.getRegId());
            job.setUserId(assetPushInfo.getUserId());
            job.setMerchantNo(assetPushInfo.getMerchantNoS());
            job.setBizType(assetPushInfo.getBizType());
            job.setOrderName(assetPushInfo.getOrderName());
            job.setOrderAmt(String.valueOf(assetPushInfo.getApplyAmt()));
            job.setOrderItems(String.valueOf(assetPushInfo.getApplyItems()));
            job.setStatus("3");
            job.setWfStatus(assetPushInfo.getWfStatus());
            job.setRiskStatus(assetPushInfo.getRiskStatus());
            job.setOrderName(assetPushInfo.getOrderName());
            job.setBizType(assetPushInfo.getBizType());
            job.setFee(planBean.getFeeRatio());
            job.setPreAmt(String.valueOf(detail.get("preAmount")));
            job.setMargin(String.valueOf(detail.get("margin")));
            job.setDownPayment(String.valueOf(detail.get("downPayment")));
            job.setServiceFee(String.valueOf(detail.get("serviceFee")));
            job.setPlanId(String.valueOf(planId));
            job.setTakePayment(detail.get("monthAmount"));
            job.setFeeYear(planBean.getFeeYear());
            job.setFeeAmount(detail.get("feeAmount"));
            job.setMonthInterest(detail.get("monthMake"));
            job.setExpireDate(formatA(assetPushInfo.getDeadline()));
            job.setCreditorId(assetPushInfo.getCreditorId());
            job.setCfRequestMoneyId(assetPushInfo.getCf_requestmoney_id());
        }
        return jysOrderBiz.insertJysOrder(job);
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
     * 根据订单号校验商户是否属于蒲公英行,且资金来源是否属于爱钱帮,或者虚拟资金渠道不推送
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月20日
     */
    @Override
    public boolean validatePGYMerchant(String fundSourceId, String orderId) throws IqbException {
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        // 虚拟资金渠道
        // FINANCE-2839 增加资金来源：京金所
        if ("4".equalsIgnoreCase(fundSourceId) || fundSourceId.startsWith("4")) {
            logger.info("---------虚拟商户:{}", fundSourceId);
            return true;
        }
        if (fundSourceId.equals(AssetAllocationAttr.FUND_SOURCE_IQB) && orderBean != null) {
            String merchantNo = orderBean.getMerchantNo();
            if (merchantNo.contains(AssetAllocationAttr.PGY_MERCHANT_NO)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 插入交易所债权人信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月3日
     */
    @Override
    public long insertJysCreditInfo(JSONObject objs) {
        JYSCreditInfoBean bean = JSON.toJavaObject(objs, JYSCreditInfoBean.class);
        return jysCreditInfoBiz.insertJysCreditInfo(bean);
    }

    /**
     * 根据手机号码获取系统用户id
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年11月17日
     */
    @Override
    public UserBean getSysUserByRegId(Map<String, Object> params) {
        return userBeanBiz.getSysUserByRegId(params);
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
    public int pushAssetInfoToFANT(AssetPushInfo assetPushInfo, JSONObject objs) throws IqbException {
        int result = 0;
        RARRequestMessage rm = new RARRequestMessage();
        Map<String, String> map = assetInfoBiz.selectCustomerBaseInfoById(assetPushInfo.getMerchantNo());
        Map<String, String> carMap = assetInfoBiz.getCarInfoByOrderId(assetPushInfo.getOrderId());

        JSONObject m = new JSONObject();
        m.put("customerCode", assetPushInfo.getMerchantNo());
        CustomerBean customerBean = customerBiz.getCustomerStoreInfoByCode(m);

        com.iqb.consumer.asset.allocation.assetinfo.bean.BankCardBean bankCardBean =
                assetInfoBiz.selectCardInfoByOrderId(assetPushInfo.getOrderId());
        rm.setProjectId(assetPushInfo.getProjectId());
        rm.setProjectCode(assetPushInfo.getProjectCode());
        rm.setProjectName(assetPushInfo.getProjectName());
        rm.setGuarantee(assetPushInfo.getGuarantee());
        rm.setGuaranteeName(StringUtil.isNull(assetPushInfo.getGuaranteeName())
                ? customerBean.getCorporateName()
                : assetPushInfo.getGuaranteeName());

        rm.setCheckTime(assetPushInfo.getCheckTime());
        rm.setProjectType(assetPushInfo.getBizType());
        rm.setApplyamt(assetPushInfo.getApplyAmt());
        rm.setDeadline(assetPushInfo.getDeadline());
        rm.setLoanName(assetPushInfo.getUserName());

        rm.setLoanIdcard(assetPushInfo.getIdCard());
        rm.setRaiseTotalAmount(assetPushInfo.getApplyAmt());
        rm.setRaiseObj(assetPushInfo.getUserName());

        if (bankCardBean != null) {
            rm.setCardNum(bankCardBean.getBankCardNo());
            rm.setBankName(bankCardBean.getBankName());
            rm.setPhone(bankCardBean.getBankMobile());
            rm.setBankNo(bankCardBean.getBankCode());
        } else {
            throw new IqbException(AssetAllocationReturnInfo.ASSET_PUSH_01030003);
        }

        rm.setUserName(assetPushInfo.getUserName());
        rm.setIdCard(assetPushInfo.getIdCard());
        rm.setChannal("1");

        if (!CollectionUtils.isEmpty(map)) {

            rm.setGuaCreditCode(map.get("socialCreditCode"));
            rm.setGuaranteeIdCard(map.get("guaranteeIdCard"));
            rm.setGuaCardNum(map.get("guaCardNum"));
            rm.setGuaBankCity(map.get("guaBankCity"));
            rm.setGuaBankName(map.get("guaBankName"));
        }
        if (!CollectionUtils.isEmpty(carMap)) {
            rm.setVehicleType(carMap.get("orderName"));
            rm.setVehicleIdNo(carMap.get("carNo"));
            rm.setVehiclePlate(carMap.get("plate"));
        }
        logger.info("---推送到饭团资产信息--{}", JSONObject.toJSONString(rm));
        try {
            chatWithRARServiceInterval(rm);
        } catch (InvalidRARException e) {
            logger.error("---资产推送到饭团异常---{}", e);
            e.printStackTrace();
        }
        return result;
    }

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

    /**
     * 根据条件查询资产分配标的信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年8月3日
     */
    @Override
    public PageInfo<AssetObjectInfoBean> selectAssetObjectInfo(JSONObject objs) {
        getMerchLimitObject(objs);
        return new PageInfo<>(assetInfoBiz.selectAssetObjectInfo(objs));
    }

    /**
     * 导出标的明细信息
     * 
     * @param objs
     * @param response
     * @return
     */
    @Override
    public String exportAssetObjectInfo(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户列表
            objs = super.getMerchLimitObject(objs);
            objs.put("isPageHelper", true);
            List<AssetObjectInfoBean> assetObjectInfoBeanList = assetInfoBiz.selectAssetObjectInfo(objs);

            // 2.导出excel表格
            HSSFWorkbook workbook = this.exportStockStatisticsList(assetObjectInfoBeanList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=AssetObjectInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出标的明细信息出现异常:{}", e);
            return "fail";
        }
        return "success";
    }

    private HSSFWorkbook exportStockStatisticsList(List<AssetObjectInfoBean> assetObjectInfoBeanList) {
        String[] excelHeader =
        {"序号", "订单号", "项目名称", "机构名称", "姓名", "手机号", "订单金额", "上标金额", "总期数", "本次请款期数",
                "债权人", "预计放款时间", "标的结束时间", "资产到期日", "资金来源", "资产分配时间", "剩余期数", "放款主体", "分配人", "备注", "推标方式"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("标的信息报表页");
        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < assetObjectInfoBeanList.size(); i++) {
            row = sheet.createRow(i + 1);
            AssetObjectInfoBean ssd = assetObjectInfoBeanList.get(i);
            row.createCell(0).setCellValue(i + 1);// 序号
            row.createCell(1).setCellValue(ObjectUtils.toString(ssd.getOrderId(), ""));// 订单号
            row.createCell(2).setCellValue(ObjectUtils.toString(ssd.getProjectName(), ""));// 项目名称
            row.createCell(3).setCellValue(ObjectUtils.toString(ssd.getMerchantName(), ""));// 机构名称
            row.createCell(4).setCellValue(ObjectUtils.toString(ssd.getRealName(), ""));// 姓名
            row.createCell(5).setCellValue(ObjectUtils.toString(ssd.getRegId(), ""));// 手机号
            row.createCell(6).setCellValue(
                    ssd.getOrderAmt() == null ? 0 : ssd.getOrderAmt().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .doubleValue());// 订单金额
            row.createCell(7).setCellValue(
                    ssd.getApplyAmt() == null ? 0 : ssd.getApplyAmt().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .doubleValue());// 上标金额
            row.createCell(8).setCellValue(ObjectUtils.toString(ssd.getOrderItems(), ""));// 总期数
            row.createCell(9).setCellValue(ObjectUtils.toString(ssd.getApplyItems(), ""));// 本次请款期数
            row.createCell(10).setCellValue(ObjectUtils.toString(ssd.getCreditName(), ""));// 债权人
            row.createCell(11).setCellValue(ObjectUtils.toString(ssd.getPlanLendingTime(), ""));// 预计放款时间

            row.createCell(12).setCellValue(ObjectUtils.toString(ssd.getDeadLine(), ""));// 标的结束时间
            row.createCell(13).setCellValue(ObjectUtils.toString(ssd.getAssetDueDateStr(), ""));// 资产到期日
            row.createCell(14).setCellValue(ObjectUtils.toString(ssd.getSourcesFundingStr(), ""));// 资金来源
            row.createCell(15).setCellValue(ObjectUtils.toString(ssd.getApplyTime(), "")); // 资产分配时间
            row.createCell(16).setCellValue(ObjectUtils.toString(ssd.getLeftItems(), "")); // 剩余期数
            row.createCell(17).setCellValue(ObjectUtils.toString(ssd.getLendersSubjectStr(), "")); // 放款主体
            row.createCell(18).setCellValue(ObjectUtils.toString(ssd.getAllotRealName(), "")); // 分配人
            row.createCell(19).setCellValue(ObjectUtils.toString(ssd.getRemark(), "")); // 备注

            row.createCell(20).setCellValue(pushModeFormat(ssd.getPushMode())); // 推标方式
        }
        // 设置列宽
        for (int j = 0; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    /**
     * Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年9月17日
     */
    private String pushModeFormat(int pushMode) {
        String pushModeStr = "";
        switch (pushMode) {
            case 1:
                pushModeStr = "按订单金额";
                break;
            case 2:
                pushModeStr = "按剩余未还本金";
                break;
            default:
                break;
        }
        return pushModeStr;
    }

    /**
     * 根据订单号获、预计放款时间取当前期数以及剩余本金
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年9月17日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getAssetInfo(JSONObject objs) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> returnResult = new HashMap<>();
        String resultStr = "";
        try {
            resultStr = SimpleHttpUtils.httpPost(
                    consumerConfig.getQueryAssetRemainPrincipalByOrderId(),
                    SignUtil.chatEncode(objs.toJSONString(), consumerConfig.getCommonPrivateKey()));
        } catch (Exception e) {
            logger.error("---根据订单号获、预计放款时间取当前期数以及剩余本金报错---{}", e);
        }
        if (!StringUtil.isNull(resultStr)) {
            result = JSONObject.parseObject(resultStr);
            returnResult = (Map) result.get("result");
        }
        return returnResult;
    }
}
