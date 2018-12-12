package com.iqb.consumer.service.layer.riskinfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderManageResult;
import com.iqb.consumer.data.layer.bean.riskinfo.LocalRiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskResultBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.user.pojo.GetUserRiskInfoRequestMessage;
import com.iqb.consumer.data.layer.bean.user.pojo.GetUserRiskInfoResponsePojo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.afterLoan.AfterLoanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.api.AssetApiService;
import com.iqb.consumer.service.layer.authoritycard.AuthorityCardService;
import com.iqb.consumer.service.layer.bill.BillInfoService;
import com.iqb.consumer.service.layer.business.service.IBankCardService;
import com.iqb.consumer.service.layer.domain.RiskCheckInfo;
import com.iqb.consumer.service.layer.domain.RiskOrderInfo;
import com.iqb.consumer.service.layer.domain.ToRiskCheckinfo;
import com.iqb.consumer.service.layer.domain.ToRiskCheckinfoForDandelion;
import com.iqb.consumer.service.layer.domain.ToRiskOrderinfo;
import com.iqb.consumer.service.layer.util.DesUtil;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

import jodd.util.StringUtil;

@Service
public class RiskInfoServiceImpl extends BaseService implements RiskInfoService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(RiskInfoServiceImpl.class);
    @Autowired
    private OrderBiz orderBiz;
    @Resource
    private BillInfoService billInfoService;
    @Autowired
    private UserBeanBiz userBeanBiz;
    @Resource
    private ConsumerConfig consumerConfig;
    @Autowired
    private RiskInfoBiz riskInfoBiz;
    @Resource
    private IBankCardService bankCardService;
    @Resource
    private AuthorityCardService authorityCardService;
    @Resource
    private AssetApiService assetApiService;
    @Autowired
    private AfterLoanBiz afterLoanBiz;

    @SuppressWarnings("rawtypes")
    @Override
    public GetUserRiskInfoResponsePojo getUserRiskInfoByRT(JSONObject requestMessage) throws GenerallyException {
        GetUserRiskInfoRequestMessage guri =
                JSONObject.toJavaObject(requestMessage, GetUserRiskInfoRequestMessage.class);
        if (guri == null || !guri.checkRequest()) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        RiskInfoBean rib = riskInfoBiz.getRiskInfoByRIAndRT(guri.getRegId(), guri.getRiskType());
        if (rib == null || StringUtil.isEmpty(rib.getCheckInfo())) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }
        GetUserRiskInfoResponsePojo result =
                JSONObject.parseObject(rib.getCheckInfo(), GetUserRiskInfoResponsePojo.class);
        if (result == null || !result.checkResponse()) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.B);
        }
        result.setIdCard(rib.getId()); // sql idNo 暂存 rib.id中
        return result;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月1日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Map<String, Object> send2Risk(Map<String, String> map) {
        try {
            String orderId = (String) map.get("orderId");
            Map params = new HashMap();
            params.put("orderId", orderId);
            OrderBean orderBean = orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo2Risk(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 发给读脉同时给风控发 修改 chengzhen 2018年3月2日 11:22:55
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private String sendCheckOrderInfo2Risk(OrderBean orderBean) {

        Map townOrderMap = new HashMap<>();
        String regId = orderBean.getRegId();

        townOrderMap.put("phone", regId);// 1.手机号
        townOrderMap.put("bizType", orderBean.getBizType());// 业务类型
        UserBean userBean = userBeanBiz.selectOne(regId);// 得到用户信息
        String riskType = "3";
        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        if ("2001".equals(orderBean.getBizType())) {
            orderinfo.setThetype("10106");// 爱享融（新车）
        } else if ("2002".equals(orderBean.getBizType())) {
            orderinfo.setThetype("10107");// 爱享融（二手车）
        } else if ("4001".equals(orderBean.getBizType())) {
            riskType = "4";
        }
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, riskType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfo checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo.class);
        // 通过用户查询查询最早的绑定的卡片
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealname(userBean.getRealName());
        checkinfo.setIdcard(userBean.getIdNo());
        checkinfo.setBankno(bankCardBean == null ? "" : bankCardBean.getBankCardNo());
        if (null != checkinfo.getAddprovince()) {
            checkinfo.setAdddetails(checkinfo.getAddprovince());
        }

        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        // 爱享融就是以租代售

        // 根据手机号查询比当前系统时间小于7天的风控信息
        List<RiskInfoBean> riskList = riskInfoBiz.getRiskListInfoByOrderId(userBean.getRegId(), orderBean.getBizType());
        JSONObject objs = new JSONObject();
        objs.put("regId", orderBean.getRegId());
        objs.put("bizType", orderBean.getBizType());
        // 获取当前用户(手机号)最近的风控报告获取时间
        List<LocalRiskInfoBean> localRiskInfoBeanList = afterLoanBiz.getReportListByRegId(objs);
        logger.info("---风控判断条件--localRiskInfoBeanList--{}--", localRiskInfoBeanList);
        long days = 10;
        if (!CollectionUtils.isEmpty(localRiskInfoBeanList)) {
            Date lastUpdateTime =
                    DateUtil.parseDate(localRiskInfoBeanList.get(0).getContentCreateTime1(),
                            DateUtil.SIMPLE_DATE_FORMAT);
            // 上一次风控报告获取时间与订单最后一次更新时间的差值(天数)
            days = daysBetween(orderBean.getUpdateTime(), lastUpdateTime);
        }
        logger.info("---风控判断条件--riskList--{}--days--{}", riskList, days);
        if (!CollectionUtils.isEmpty(riskList) || days <= 7) {
            return "存在7天内返回的风控信息";
        } else {
            townOrderMap.put("bizType", orderBean.getBizType());
            townOrderMap.put("name", userBean.getRealName());
            townOrderMap.put("idNo", userBean.getIdNo());
            townOrderMap.put("phone", userBean.getRegId());
            townOrderMap.put("orderId", orderBean.getOrderId());
            townOrderMap.put("bankNum", bankCardBean.getBankCardNo());
            townOrderMap.put("bankName", bankCardBean.getBankName());
            if (!StringUtils.isEmpty(checkinfo.getMarriedstatus())) {
                if (checkinfo.getMarriedstatus().equals("未婚")) {
                    townOrderMap.put("married", "0");// 婚姻状态
                } else {
                    townOrderMap.put("married", "1");// 婚姻状态
                }
            }
            townOrderMap.put("education", "");// 学历 中阁中没有
            townOrderMap.put("notifyUrl", consumerConfig.getRiskReturnCallbackUrl());// 通知回调地址
            townOrderMap.put("school", "");// 学校 中阁中没有
            townOrderMap.put("otherIncome", "");// 其他收入 中阁中没有
            townOrderMap.put("nativePlace", checkinfo.getAddprovince() != null ? checkinfo.getAddprovince() : "");// 地址

            Date createTime = orderBean.getCreateTime();
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            String format = sd.format(createTime);
            townOrderMap.put("loanDate", format);// 借款日期
            townOrderMap.put("loanAmount", "");// 固定收入
            townOrderMap.put("loanTerm", orderBean.getOrderItems());// 借款期限
            townOrderMap.put("instalmentTerm", orderBean.getOrderItems());// 分期期数
            townOrderMap.put("loanPurpose", "");// 借款用途
            // 客户来源（1：自有渠道:2：合作渠道；3：推广渠道）
            townOrderMap.put("customerSource", "");// 客户来源
            // 申请渠道 (1：web；2：wxWeb；3：wxApp；4：app)
            townOrderMap.put("applyChannel", "");// 客户来源
            townOrderMap.put("province", "");// 借款省份
            townOrderMap.put("city", "");// 借款城市
            townOrderMap.put("email", "");// 借款人邮箱
            townOrderMap.put("telephone", "");// 借款人座机
            townOrderMap.put("qq", "");// 借款人qq
            townOrderMap.put("wechat", "");// 借款人微信
            // 借款人性质（01：企业法人；02：企业非法人；03：个体工商户；04：事业单位；05：不在职；99：其他）
            townOrderMap.put("applyerType", "");// 借款人性质
            townOrderMap.put("company", "");// 借款人单位
            townOrderMap.put("companyAddr", "");// 借款人单位地址
            townOrderMap.put("companyType", "");// 借款人公司性质
            townOrderMap.put("companyPhone", "");// 单位电话
            /**
             * 所属行业(1:农、林、牧、渔业;2:采矿业;3:制造业;4:电力、热力、燃气及水的生产和供应业;5:环境和公共设施管理业;
             * 6:建筑业;7:交通运输、仓储业和邮政业;8:信息传输、计算机服务和软件业;9:批发和零售业;10:住宿、餐饮业;
             * 11:金融、保险业;12:房地产业;13:租赁和商务服务业;14:科学研究、技术服务和地质勘查业;
             * 15:水利、环境和公共设施管理业;16:居民服务和其他服务业;17:教育;18:卫生、社会保障和社会服务业;19:文化、体育、娱乐业;20:综
             */
            townOrderMap.put("industry", orderBean.getOrderItems());// 所属行业

            townOrderMap.put("workTime", "");// 工作年限
            // 职位（practice：见习人员；beginners：初级、助理人员；normal：普通员工；junior：基层管理人员；
            // middle：中层管理人员；senior：高层管理人员；advanced：高级资深人员；intermediates：中级技术人员；
            townOrderMap.put("occupation", "");
            townOrderMap.put("career", "");// 职业
            // 住房类型(1：有所有权无抵押；2：有所有权已抵押；3：公房租赁:4：自建房；5：父母房产；6：租房；99：其他)
            townOrderMap.put("housingType", "");// 住房类型
            townOrderMap.put("crossLoan", "");// 跨平台借款
            townOrderMap.put("contactName1", checkinfo.getContactname1() != null ? checkinfo.getContactname1() : "");// 第一联系人名称
            townOrderMap.put("contactIdNo1", "");// 第一联系人身份证
            townOrderMap
                    .put("contactMobile1", checkinfo.getContactphone1() != null ? checkinfo.getContactphone1() : "");// 第一联系人手机号
            townOrderMap.put("contactRelation1", checkinfo.getContactrelation1() != null
                    ? checkinfo.getContactrelation1()
                    : "");// 第一联系人社会关系
            townOrderMap.put("contactName2", checkinfo.getContactname2() != null ? checkinfo.getContactname2() : "");
            townOrderMap.put("contactIdNo2", "");
            townOrderMap
                    .put("contactMobile2", checkinfo.getContactphone2() != null ? checkinfo.getContactphone2() : "");
            townOrderMap.put("contactRelation2", checkinfo.getContactrelation2() != null
                    ? checkinfo.getContactrelation2()
                    : "");
            townOrderMap.put("contactName3", "");
            townOrderMap.put("contactIdNo3", "");
            townOrderMap.put("contactMobile3", "");
            townOrderMap.put("contactRelation3", "");
            townOrderMap.put("contactName4", "");
            townOrderMap.put("contactIdNo4", "");
            townOrderMap.put("contactMobile4", "");
            townOrderMap.put("contactRelation4", "");
            townOrderMap.put("contactName5", "");
            townOrderMap.put("contactIdNo5", "");
            townOrderMap.put("contactMobile5", "");
            townOrderMap.put("contactRelation5", "");
            /**
             * 
             */
            townOrderMap.put("coborrowerName1", "");
            townOrderMap.put("coborrowerIdNo1", "");
            townOrderMap.put("coborrowerMobile1", "");
            townOrderMap.put("coborrowerCompany1", "");
            townOrderMap.put("coborrowerCompanyAddr1", "");
            townOrderMap.put("coborrowerAddr1", "");
            townOrderMap.put("coborrowerName2", "");
            townOrderMap.put("coborrowerIdNo2", "");
            townOrderMap.put("coborrowerMobile2", "");
            townOrderMap.put("coborrowerCompany2", "");
            townOrderMap.put("coborrowerCompanyAddr2", "");
            townOrderMap.put("coborrowerAddr2", "");

            townOrderMap.put("suretyName", "");// 担保人
            townOrderMap.put("suretyIdNo", "");// 担保人身份证
            townOrderMap.put("suretyMobile", "");// 担保人手机
            townOrderMap.put("suretyCompany", "");// 担保人公司
            townOrderMap.put("suretyCompanyAddr", "");// 担保人公司地址
            townOrderMap.put("suretyHomeAddress", "");// 担保人家庭地址

            townOrderMap.put("ipAddress", "");// IP地址(用户侧公网IP地址)
            townOrderMap.put("trueIpAddress", "");
            townOrderMap.put("tokenInfo", "");// 指纹令牌

            // DES加密
            String jsonString2 = JSONUtils.toJSONString(townOrderMap);
            long time = System.currentTimeMillis();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(time);
            String format1 = df.format(date);// 当前时间年月日格式
            // 根据订单号的bizType判断是哪个商户根据根据商户获取key
            Map CodeAndKey = bankCardService.getCodeAndKeyByMerchanNo(orderBean.getMerchantNo());
            String strDec =
                    DesUtil.strEnc(jsonString2, CodeAndKey.get("keyCode").toString(), CodeAndKey.get("customerCode")
                            .toString(), format1);
            // 将加密后的数据转为json
            JSONObject json = new JSONObject();
            json.put("data", strDec);
            json.put("customerCode", CodeAndKey.get("customerCode").toString());
            json.put("sendTime", format1);
            json.put("orderId", orderBean.getOrderId());
            // 发往风控的
            RiskResultBean riskResultBeanTown = new RiskResultBean();
            riskResultBeanTown.setOrderId(orderBean.getOrderId());
            riskResultBeanTown.setContent(json.toJSONString());
            riskResultBeanTown.setSendUrl(consumerConfig.getLocalRiskcontrolCheckorderUrl());
            riskResultBeanTown.setType("1");
            riskResultBeanTown.setStatus(2);
            riskResultBeanTown.setFlag("2");

            // 将inst_local表中存在的订单号置为失效
            riskInfoBiz.updateLocalRiskInfo(orderBean);

            return riskInfoBiz.saveRiskResultInfo(riskResultBeanTown).toString();
        }
    }

    private String getTraceNo() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-");
        String traceNo = sdf.format(date) + (int) ((Math.random() * 9 + 1) * 100000);
        return traceNo;
    }

    private Object copyConvert(Object obj) {
        if (obj instanceof ToRiskCheckinfo) {
            ToRiskCheckinfo checkInfo = (ToRiskCheckinfo) obj;
            RiskCheckInfo returnOjb = new RiskCheckInfo();
            returnOjb.setRealName(checkInfo.getRealname());
            returnOjb.setIdCard(checkInfo.getIdcard());
            returnOjb.setAddDetails(checkInfo.getAdddetails());
            returnOjb.setAddProvince(checkInfo.getAddprovince());
            returnOjb.setMarriedStatus(checkInfo.getMarriedstatus());
            returnOjb.setBankName(checkInfo.getBankname());
            returnOjb.setBankNo(checkInfo.getBankno());
            returnOjb.setPhone(checkInfo.getPhone());
            returnOjb.setContactName1(checkInfo.getContactname1());
            returnOjb.setContactName2(checkInfo.getContactname2());
            returnOjb.setContactPhone1(checkInfo.getContactphone1());
            returnOjb.setContactPhone2(checkInfo.getContactphone2());
            return returnOjb;
        } else if (obj instanceof ToRiskOrderinfo) {
            ToRiskOrderinfo orderInfo = (ToRiskOrderinfo) obj;
            RiskOrderInfo returnOjb = new RiskOrderInfo();
            returnOjb.setAmount(orderInfo.getAmount());
            returnOjb.setAttribute1(orderInfo.getAttribute1());
            returnOjb.setAttribute2(orderInfo.getAttribute2());
            returnOjb.setEngine(orderInfo.getEngine());
            returnOjb.setInstalmentNo(orderInfo.getInstalmentno());
            returnOjb.setInstalmentTerms(orderInfo.getInstalmentterms());
            returnOjb.setOrdeInfo(orderInfo.getOrdeinfo());
            returnOjb.setOrderId(orderInfo.getOrderid());
            returnOjb.setOrganization(orderInfo.getOrganization());
            returnOjb.setPlate(orderInfo.getPlate());
            returnOjb.setPlateType(orderInfo.getPlateType());
            returnOjb.setThetype(orderInfo.getThetype());
            returnOjb.setVin(orderInfo.getVin());
            return returnOjb;
        }
        return null;
    }

    /**
     * 
     * Description:读脉风控生成随机结果 pro test_s test_f
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return
     */
    private static String getRandomString(String flag) {
        String base = CommonConstant.OrderHandelFlag.base;
        String orderFlag = CommonConstant.OrderHandelFlag.orderFlag;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        if (flag.equals(CommonConstant.OrderHandelFlag.pro)) {
            sb.append(CommonConstant.OrderHandelFlag.pro);
        } else {
            for (int i = 0; i < 1; i++) {
                int number = random.nextInt(orderFlag.length());
                sb.append(base);
                sb.append(orderFlag.charAt(number));
            }
        }

        return sb.toString();
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月1日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> send2Risk4Pledge(Map<String, String> map) {
        try {
            String orderId = (String) map.get("orderId");
            logger.debug("获取到orderId:{}", orderId);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", orderId);
            OrderBean orderBean = orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo2Risk4Pledge(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    private String sendCheckOrderInfo2Risk4Pledge(OrderBean orderBean) {
        Map<String, Object> checkOrderMap = new HashMap<String, Object>();
        String regId = orderBean.getRegId();
        checkOrderMap.put("regId", regId);
        checkOrderMap.put("appType", "weixin");
        checkOrderMap.put("order_handel_flag", getRandomString(consumerConfig.getEnvFlag()));
        checkOrderMap.put("website", "");
        checkOrderMap.put("traceNo", getTraceNo());
        checkOrderMap.put("version", "1.0.0");
        checkOrderMap.put("source", "huahuadata");// 來源
        checkOrderMap.put("noticeUrl", consumerConfig.getIdumai_riskcontrol_pledge_notice_url()); // 抵押车回調地址
        String merchType = "3";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, merchType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfo checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo.class);

        UserBean userBean = userBeanBiz.selectOne(regId);
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealname(userBean.getRealName());
        checkinfo.setIdcard(userBean.getIdNo());
        // 通过用户查询查询最早的绑定的卡片
        checkinfo.setBankno(bankCardBean.getBankCardNo());
        checkinfo.setAdddetails(checkinfo.getAddprovince());

        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        orderinfo.setOrderid(orderBean.getOrderId());
        orderinfo.setOrdeinfo(orderBean.getOrderName());
        orderinfo.setAmount(orderBean.getOrderAmt());
        orderinfo.setInstalmentterms(orderBean.getOrderItems());
        orderinfo.setInstalmentno("");
        // orderinfo.setThetype("10115");// 抵押车
        orderinfo.setThetype("10104");// 10104爱抵贷
        orderinfo.setAttribute1(orderBean.getOrderName());
        orderinfo.setAttribute2(orderBean.getOrderName());
        orderinfo.setOrganization("iqb");
        // 风控接口调整联调
        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        JSONObject objs = new JSONObject();
        objs.put("orderId", orderBean.getOrderId());
        AuthorityCardBean ac = authorityCardService.selectOneByOrderId(objs);
        orderinfo.setPlate(ac.getPlate());// 车牌号
        orderinfo.setPlateType(ac.getPlateType());// 车型号"（"01":"大型汽车"，"02":"小型汽车"，"15":"挂车"）
        orderinfo.setVin(ac.getCarNo());// 车架号
        orderinfo.setEngine(ac.getEngine());// 发动机号
        RiskCheckInfo riskCheckInfo = (RiskCheckInfo) copyConvert(checkinfo);
        RiskOrderInfo riskOrderInfo = (RiskOrderInfo) copyConvert(orderinfo);
        checkOrderMap.put("checkInfo", riskCheckInfo);
        checkOrderMap.put("orderInfo", riskOrderInfo);
        String jsonString = JSON.toJSONString(checkOrderMap);
        logger.info("send risk info :{}", jsonString);

        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setOrderId(orderBean.getOrderId());
        riskResultBean.setContent(jsonString);
        riskResultBean.setSendUrl(consumerConfig.getIdumai_riskcontrol_checkorder_url());
        riskResultBean.setType("1");
        riskResultBean.setStatus(2);

        return riskInfoBiz.saveRiskResultInfo(riskResultBean).toString();
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年7月1日
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> send2RiskForDandelion(Map<String, String> map) {
        try {
            String orderId = (String) map.get("orderId");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("orderId", orderId);
            OrderManageResult orderBean = (OrderManageResult) orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo2RiskForDandelion(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @SuppressWarnings("rawtypes")
    private String sendCheckOrderInfo2RiskForDandelion(OrderManageResult orderBean) {
        Map<String, Object> checkOrderMap = new HashMap<String, Object>();
        String regId = orderBean.getRegId();
        checkOrderMap.put("regId", regId);
        checkOrderMap.put("appType", "weixin");
        checkOrderMap.put("order_handel_flag", getRandomString(consumerConfig.getEnvFlag()));
        checkOrderMap.put("website", "");
        checkOrderMap.put("traceNo", getTraceNo());
        checkOrderMap.put("version", "1.0.0");
        checkOrderMap.put("source", "huahuadata");// 來源
        UserBean userBean = userBeanBiz.selectOne(regId);
        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        if ("3001".equals(orderBean.getBizType())) {
            checkOrderMap.put("noticeUrl", consumerConfig.getPgyh_riskcontrol_notice_url()); // 回調地址
            orderinfo.setThetype("10109");// 蒲公英行(现金贷)
        }
        String riskType = "4";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, riskType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfoForDandelion checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfoForDandelion.class);
        // 通过用户查询查询最早的绑定的卡片
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealName(userBean.getRealName());
        checkinfo.setIdCard(userBean.getIdNo());
        checkinfo.setBankCard(bankCardBean == null ? "" : bankCardBean.getBankCardNo());
        checkinfo.setServiceOrganization(orderBean.getMerchName());

        orderinfo.setOrderid(orderBean.getOrderId());
        orderinfo.setOrdeinfo(orderBean.getOrderName());
        orderinfo.setAmount(orderBean.getOrderAmt());
        orderinfo.setInstalmentterms(orderBean.getOrderItems());
        orderinfo.setInstalmentno("");

        orderinfo.setAttribute1(orderBean.getOrderName());
        orderinfo.setAttribute2(orderBean.getOrderName());
        orderinfo.setOrganization("iqb");
        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        // 爱享融就是以租代售
        /*
         * JSONObject objs = new JSONObject(); objs.put("orderId", orderBean.getOrderId());
         * AuthorityCardBean ac = authorityCardService.selectOneByOrderId(objs);
         * orderinfo.setPlate(ac.getPlate());// 车牌号 orderinfo.setPlateType(ac.getPlateType());//
         * 车型号"（"01":"大型汽车"，"02":"小型汽车"，"15":"挂车"） orderinfo.setVin(ac.getCarNo());// 车架号
         * orderinfo.setEngine(ac.getEngine());// 发动机号
         */RiskCheckInfo riskCheckInfo = (RiskCheckInfo) copyConvertForDandelion(checkinfo);
        RiskOrderInfo riskOrderInfo = (RiskOrderInfo) copyConvert(orderinfo);
        checkOrderMap.put("checkInfo", riskCheckInfo);
        checkOrderMap.put("orderInfo", riskOrderInfo);
        String jsonString = JSON.toJSONString(checkOrderMap);
        logger.info("send risk info :{}", jsonString);

        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setOrderId(orderBean.getOrderId());
        riskResultBean.setContent(jsonString);
        riskResultBean.setSendUrl(consumerConfig.getIdumai_riskcontrol_checkorder_url());
        riskResultBean.setType("1");
        riskResultBean.setStatus(2);

        return riskInfoBiz.saveRiskResultInfo(riskResultBean).toString();
    }

    /**
     * 蒲公英风控信息转换方法
     * 
     * @param obj
     * @return Object
     */
    private Object copyConvertForDandelion(Object obj) {
        if (obj instanceof ToRiskCheckinfoForDandelion) {
            ToRiskCheckinfoForDandelion checkInfo = (ToRiskCheckinfoForDandelion) obj;
            RiskCheckInfo returnOjb = new RiskCheckInfo();

            returnOjb.setRealName(checkInfo.getRealName());
            returnOjb.setIdCard(checkInfo.getIdCard());
            returnOjb.setAddDetails(checkInfo.getAddress());
            returnOjb.setAddProvince(checkInfo.getAddress());
            returnOjb.setMarriedStatus("");

            returnOjb.setBankName("");
            returnOjb.setBankNo(checkInfo.getBankCard());
            returnOjb.setJob("");
            returnOjb.setIncome("0");
            returnOjb.setIncomepic("");

            returnOjb.setOtherincome("0");
            returnOjb.setEducation("");
            returnOjb.setPhone(checkInfo.getRegId());
            returnOjb.setServerpwd("");
            returnOjb.setInsuranceid(checkInfo.getCreditNo());

            returnOjb.setInsurancepwd(checkInfo.getCreditPasswd());
            returnOjb.setFundid("");
            returnOjb.setFundpwd("");

            returnOjb.setContactrelation1(checkInfo.getRelation1());
            returnOjb.setContactName1("");
            returnOjb.setContactPhone1(checkInfo.getPhone1());
            returnOjb.setContactSex1(checkInfo.getSex1());

            returnOjb.setContactrelation2(checkInfo.getRelation2());
            returnOjb.setContactName2("");
            returnOjb.setContactPhone2(checkInfo.getPhone2());
            returnOjb.setContactSex2(checkInfo.getSex2());

            returnOjb.setContactrelation3(checkInfo.getRelation3());
            returnOjb.setContactName3("");
            returnOjb.setContactPhone3(checkInfo.getPhone3());
            returnOjb.setContactSex3(checkInfo.getSex3());

            returnOjb.setCompany(checkInfo.getCompanyName());
            returnOjb.setCompanyAddress(checkInfo.getCompanyAddress());
            returnOjb.setCompanyPhone(checkInfo.getCompanyPhone());

            returnOjb.setColleagueName1(checkInfo.getColleagues1());
            returnOjb.setColleaguePhone1("");
            returnOjb.setColleagueName2(checkInfo.getColleagues2());
            returnOjb.setColleaguePhone2("");
            returnOjb.setServiceOrganization(checkInfo.getServiceOrganization());

            returnOjb.setContactrelation4(checkInfo.getRelation4());
            returnOjb.setContactName4(checkInfo.getrName4());
            returnOjb.setContactPhone4(checkInfo.getPhone4());

            return returnOjb;
        }
        return null;
    }

    /**
     * 
     * Description:车主贷-发送风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Map<String, Object> send3Risk(Map<String, String> map) {
        try {
            String orderId = (String) map.get("orderId");
            Map params = new HashMap();
            params.put("orderId", orderId);
            OrderManageResult orderBean = (OrderManageResult) orderBiz.selectOne(params);
            logger.debug("开始发送给风控");
            String result = sendCheckOrderInfo3Risk(orderBean);
            logger.debug("发送风控完成.结果：{}", result);
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:发送车主贷风控信息到读脉
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月14日
     */
    @SuppressWarnings("rawtypes")
    private String sendCheckOrderInfo3Risk(OrderManageResult orderBean) {
        logger.debug("---发送车主贷风控信息到读脉开始---{}", JSONObject.toJSON(orderBean));
        Map<String, Object> checkOrderMap = new HashMap<String, Object>();
        String regId = orderBean.getRegId();
        checkOrderMap.put("regId", regId);
        checkOrderMap.put("appType", "weixin");
        checkOrderMap.put("order_handel_flag", getRandomString(consumerConfig.getEnvFlag()));
        checkOrderMap.put("website", "");
        checkOrderMap.put("traceNo", getTraceNo());
        checkOrderMap.put("version", "1.0.0");
        checkOrderMap.put("source", "huahuadata");// 來源
        UserBean userBean = userBeanBiz.selectOne(regId);
        // 设置订单信息
        ToRiskOrderinfo orderinfo = new ToRiskOrderinfo();
        if ("2400".equals(orderBean.getBizType())) {
            checkOrderMap.put("noticeUrl", consumerConfig.getOwnerLoanWfreturnUrl()); // 回調地址
            orderinfo.setThetype("10109");// 车主贷
        }
        String riskType = "4";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, riskType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfoForDandelion checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfoForDandelion.class);
        // 通过用户查询查询最早的绑定的卡片
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealName(userBean.getRealName());
        checkinfo.setIdCard(userBean.getIdNo());
        checkinfo.setBankCard(bankCardBean == null ? "" : bankCardBean.getBankCardNo());
        checkinfo.setServiceOrganization(orderBean.getMerchName());

        orderinfo.setOrderid(orderBean.getOrderId());
        orderinfo.setOrdeinfo(orderBean.getOrderName());
        orderinfo.setAmount(orderBean.getOrderAmt());
        orderinfo.setInstalmentterms(orderBean.getOrderItems());
        orderinfo.setInstalmentno("");

        orderinfo.setAttribute1(orderBean.getOrderName());
        orderinfo.setAttribute2(orderBean.getOrderName());
        orderinfo.setOrganization("iqb");

        RiskCheckInfo riskCheckInfo = (RiskCheckInfo) copyConvertForDandelion(checkinfo);
        RiskOrderInfo riskOrderInfo = (RiskOrderInfo) copyConvert(orderinfo);
        checkOrderMap.put("checkInfo", riskCheckInfo);
        checkOrderMap.put("orderInfo", riskOrderInfo);
        String jsonString = JSON.toJSONString(checkOrderMap);
        logger.info("send risk info :{}", jsonString);

        RiskResultBean riskResultBean = new RiskResultBean();
        riskResultBean.setOrderId(orderBean.getOrderId());
        riskResultBean.setContent(jsonString);
        riskResultBean.setSendUrl(consumerConfig.getIdumai_riskcontrol_checkorder_url());
        riskResultBean.setType("1");
        riskResultBean.setStatus(2);
        logger.debug("---发送车主贷风控信息到读脉完成---");
        return riskInfoBiz.saveRiskResultInfo(riskResultBean).toString();
    }

    /**
     * 
     * Description:发送风控信息到风控系统
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月21日
     */
    @SuppressWarnings({"rawtypes", "unused"})
    private String sendCheckOrderInfo2Risk1(OrderBean orderBean) {

        Map<String, Object> townOrderMap = new HashMap<>();
        String regId = orderBean.getRegId();
        UserBean userBean = userBeanBiz.selectOne(regId);// 得到用户信息
        // 设置订单信息
        String riskType = "4";
        RiskInfoBean riskInfoBean = riskInfoBiz.getRiskInfoByRegId(regId, riskType);
        String ciJsonStr = riskInfoBean.getCheckInfo();
        ToRiskCheckinfo checkinfo = JSONObject.parseObject(ciJsonStr, ToRiskCheckinfo.class);
        // 通过用户查询查询最早的绑定的卡片
        BankCardBean bankCardBean = bankCardService.getOpenBankCardByRegId(userBean.getId() + "");
        checkinfo.setRealname(userBean.getRealName());
        checkinfo.setIdcard(userBean.getIdNo());
        checkinfo.setBankno(bankCardBean == null ? "" : bankCardBean.getBankCardNo());
        checkinfo.setAdddetails(checkinfo.getAddprovince());

        // （10101质押车改为）10101爱质贷、
        // （10103美容改为）10103医美分期、
        // （10104二手车抵押改为）10104爱抵贷、
        // （新增）10105滴滴车分期、
        // （新增）10106爱享融（新车）
        // （新增）10107爱享融（二手车）
        // 爱享融就是以租代售

        // 这两个参数需要入库
        townOrderMap.put("phone", regId);// 1.手机号
        townOrderMap.put("bizType", orderBean.getBizType());// 业务类型
        townOrderMap.put("bizType", orderBean.getBizType());
        townOrderMap.put("name", userBean.getRealName());
        townOrderMap.put("idNo", userBean.getIdNo());
        townOrderMap.put("phone", userBean.getRegId());
        townOrderMap.put("orderId", orderBean.getOrderId());
        townOrderMap.put("bankNum", bankCardBean.getBankCardNo());
        townOrderMap.put("bankName", bankCardBean.getBankName());
        if (!StringUtils.isEmpty(checkinfo.getMarriedstatus())) {
            if (checkinfo.getMarriedstatus().equals("未婚")) {
                townOrderMap.put("married", "0");// 婚姻状态
            } else {
                townOrderMap.put("married", "1");// 婚姻状态
            }
        }
        // 学历 中阁中没有
        townOrderMap.put("education", "");
        // 通知回调地址
        townOrderMap.put("notifyUrl", consumerConfig.getRiskReturnCallbackUrl());
        // 学校 中阁中没有
        townOrderMap.put("school", "");
        // 其他收入 中阁中没有
        townOrderMap.put("otherIncome", "");
        townOrderMap.put("nativePlace", checkinfo.getAddprovince());// 地址

        Date createTime = orderBean.getCreateTime();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        String format = sd.format(createTime);
        townOrderMap.put("loanDate", format);// 借款日期
        townOrderMap.put("loanAmount", "");// 固定收入
        townOrderMap.put("loanTerm", orderBean.getOrderItems());// 借款期限
        townOrderMap.put("instalmentTerm", orderBean.getOrderItems());// 分期期数
        townOrderMap.put("loanPurpose", "");// 借款用途
        // 客户来源（1：自有渠道:2：合作渠道；3：推广渠道）
        townOrderMap.put("customerSource", "");// 客户来源
        // 申请渠道 (1：web；2：wxWeb；3：wxApp；4：app)
        townOrderMap.put("applyChannel", "");// 客户来源
        townOrderMap.put("province", "");// 借款省份
        townOrderMap.put("city", "");// 借款城市
        townOrderMap.put("email", "");// 借款人邮箱
        townOrderMap.put("telephone", "");// 借款人座机
        townOrderMap.put("qq", "");// 借款人qq
        townOrderMap.put("wechat", "");// 借款人微信
        // 借款人性质（01：企业法人；02：企业非法人；03：个体工商户；04：事业单位；05：不在职；99：其他）
        townOrderMap.put("applyerType", "");// 借款人性质
        townOrderMap.put("company", "");// 借款人单位
        townOrderMap.put("companyAddr", "");// 借款人单位地址
        townOrderMap.put("companyType", "");// 借款人公司性质
        townOrderMap.put("companyPhone", "");// 单位电话
        /**
         * 所属行业(1:农、林、牧、渔业;2:采矿业;3:制造业;4:电力、热力、燃气及水的生产和供应业;5:环境和公共设施管理业;
         * 6:建筑业;7:交通运输、仓储业和邮政业;8:信息传输、计算机服务和软件业;9:批发和零售业;10:住宿、餐饮业;
         * 11:金融、保险业;12:房地产业;13:租赁和商务服务业;14:科学研究、技术服务和地质勘查业;
         * 15:水利、环境和公共设施管理业;16:居民服务和其他服务业;17:教育;18:卫生、社会保障和社会服务业;19:文化、体育、娱乐业;20:综
         */
        townOrderMap.put("industry", orderBean.getOrderItems());// 所属行业
        // 工作年限
        townOrderMap.put("workTime", "");
        // 职位（practice：见习人员；beginners：初级、助理人员；normal：普通员工；junior：基层管理人员；
        // middle：中层管理人员；senior：高层管理人员；advanced：高级资深人员；intermediates：中级技术人员；
        townOrderMap.put("occupation", "");
        townOrderMap.put("career", "");// 职业
        // 住房类型(1：有所有权无抵押；2：有所有权已抵押；3：公房租赁:4：自建房；5：父母房产；6：租房；99：其他)
        townOrderMap.put("housingType", "");// 住房类型
        townOrderMap.put("crossLoan", "");// 跨平台借款
        townOrderMap.put("contactName1", checkinfo.getContactname1());// 第一联系人名称
        townOrderMap.put("contactIdNo1", "");// 第一联系人身份证
        townOrderMap.put("contactMobile1", checkinfo.getContactphone1());// 第一联系人手机号
        townOrderMap.put("contactRelation1", checkinfo.getContactrelation1());// 第一联系人社会关系
        townOrderMap.put("contactName2", checkinfo.getContactname2());
        townOrderMap.put("contactIdNo2", "");
        townOrderMap.put("contactMobile2", checkinfo.getContactphone2());
        townOrderMap.put("contactRelation2", checkinfo.getContactrelation2());
        townOrderMap.put("contactName3", "");
        townOrderMap.put("contactIdNo3", "");
        townOrderMap.put("contactMobile3", "");
        townOrderMap.put("contactRelation3", "");
        townOrderMap.put("contactName4", "");
        townOrderMap.put("contactIdNo4", "");
        townOrderMap.put("contactMobile4", "");
        townOrderMap.put("contactRelation4", "");
        townOrderMap.put("contactName5", "");
        townOrderMap.put("contactIdNo5", "");
        townOrderMap.put("contactMobile5", "");
        townOrderMap.put("contactRelation5", "");
        townOrderMap.put("coborrowerName1", "");
        townOrderMap.put("coborrowerIdNo1", "");
        townOrderMap.put("coborrowerMobile1", "");
        townOrderMap.put("coborrowerCompany1", "");
        townOrderMap.put("coborrowerCompanyAddr1", "");
        townOrderMap.put("coborrowerAddr1", "");
        townOrderMap.put("coborrowerName2", "");
        townOrderMap.put("coborrowerIdNo2", "");
        townOrderMap.put("coborrowerMobile2", "");
        townOrderMap.put("coborrowerCompany2", "");
        townOrderMap.put("coborrowerCompanyAddr2", "");
        townOrderMap.put("coborrowerAddr2", "");

        townOrderMap.put("suretyName", "");// 担保人
        townOrderMap.put("suretyIdNo", "");// 担保人身份证
        townOrderMap.put("suretyMobile", "");// 担保人手机
        townOrderMap.put("suretyCompany", "");// 担保人公司
        townOrderMap.put("suretyCompanyAddr", "");// 担保人公司地址
        townOrderMap.put("suretyHomeAddress", "");// 担保人家庭地址

        townOrderMap.put("ipAddress", "");// IP地址(用户侧公网IP地址)
        townOrderMap.put("trueIpAddress", "");
        townOrderMap.put("tokenInfo", "");// 指纹令牌

        // DES加密
        String jsonString2 = JSONUtils.toJSONString(townOrderMap);
        long time = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(time);
        String format1 = df.format(date);// 当前时间年月日格式
        // 根据订单号的bizType判断是哪个商户根据根据商户获取key
        Map CodeAndKey = bankCardService.getCodeAndKeyByMerchanNo(orderBean.getMerchantNo());
        String strDec =
                DesUtil.strEnc(jsonString2, CodeAndKey.get("keyCode").toString(), CodeAndKey.get("customerCode")
                        .toString(), format1);
        // 将加密后的数据转为json
        JSONObject json = new JSONObject();
        json.put("data", strDec);
        json.put("customerCode", CodeAndKey.get("customerCode").toString());
        json.put("sendTime", format1);
        json.put("orderId", orderBean.getOrderId());
        // 发往风控的
        RiskResultBean riskResultBeanTown = new RiskResultBean();
        riskResultBeanTown.setOrderId(orderBean.getOrderId());
        riskResultBeanTown.setContent(json.toJSONString());
        riskResultBeanTown.setSendUrl(consumerConfig.getLocalRiskcontrolCheckorderUrl());
        riskResultBeanTown.setType("1");
        riskResultBeanTown.setStatus(2);
        riskResultBeanTown.setFlag("2");

        // 将inst_local表中存在的订单号置为失效
        riskInfoBiz.updateLocalRiskInfo(orderBean);
        return riskInfoBiz.saveRiskResultInfo(riskResultBeanTown).toString();
    }

    /**
     * 
     * 返回两个时间的相差的天数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月3日
     */
    private static long daysBetween(Date endDate, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startDay = cal.get(Calendar.DAY_OF_YEAR);
        cal.setTime(endDate);
        int endDay = cal.get(Calendar.DAY_OF_YEAR);
        return (endDay - startDay) + 1;
    }
}
