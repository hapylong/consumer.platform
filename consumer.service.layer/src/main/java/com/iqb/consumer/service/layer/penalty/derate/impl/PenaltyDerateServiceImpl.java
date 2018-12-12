package com.iqb.consumer.service.layer.penalty.derate.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.BizConstant.BizProcDefKeyConstant;
import com.iqb.consumer.common.constant.BizConstant.WFConst;
import com.iqb.consumer.common.constant.CommonConstant;
import com.iqb.consumer.common.constant.CommonConstant.PageConst;
import com.iqb.consumer.common.constant.CommonConstant.StatusConst;
import com.iqb.consumer.common.constant.CommonConstant.WFKeyConst;
import com.iqb.consumer.common.constant.FinanceConstant.ParameterConstant;
import com.iqb.consumer.common.constant.FinanceConstant.ResultConstant;
import com.iqb.consumer.common.exception.ConsumerReturnInfo;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.penalty.derate.PenaltyDerateBean;
import com.iqb.consumer.data.layer.bean.wf.SettleApplyBean;
import com.iqb.consumer.data.layer.biz.penalty.derate.PenaltyDerateBiz;
import com.iqb.consumer.data.layer.biz.wf.SettleApplyBeanBiz;
import com.iqb.consumer.service.layer.base.FinanceUtilService;
import com.iqb.consumer.service.layer.penalty.derate.IPenaltyDerateService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.redis.RedisPlatformDao;
import com.iqb.etep.common.utils.BeanUtil;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;

/**
 * 
 * Description: 逾期罚息减免服务实现
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
@SuppressWarnings({"unchecked"})
@Service
public class PenaltyDerateServiceImpl extends FinanceUtilService implements IPenaltyDerateService {

    /** 分期明细id **/
    private static final String INSTALLDETAILID_KEY = "installDetailId";

    /** 主键id **/
    private static final String ID_KEY = "id";

    /** uuid **/
    private static final String UUID_KEY = "uuid";

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(PenaltyDerateServiceImpl.class);

    /** 商户列表key **/
    private static final String MERLIST_KEY = "merList";

    /** 商户列表key **/
    private static final String MERCHANTNOS_KEY = "merchantNos";

    @Autowired
    private PenaltyDerateBiz penaltyDerateBiz;

    @Resource
    private ConsumerConfig consumerConfig;

    @Resource
    private SysUserSession sysUserSession;
    @Resource
    private SettleApplyBeanBiz settleApplyBeanBiz;

    /** 序号格式 **/
    private static final String STR_FORMAT = "000";
    /** 初始序号 **/
    private String INITIAL_SEQ = "1";
    @Autowired
    private RedisPlatformDao redisPlatformDao;

    @Override
    public PageInfo<PenaltyDerateBean> listPenaltyDerateApply(JSONObject objs) throws IqbException {
        /** 获取商户列表 **/
        List<String> merList = this.getMerchantList(objs);
        objs.put(MERCHANTNOS_KEY, merList);
        objs.remove(MERLIST_KEY);

        return new PageInfo<PenaltyDerateBean>(this.penaltyDerateBiz.listPenaltyDerateApply(objs));
    }

    @Override
    public PageInfo<Map<String, Object>> listPenaltyDeratable(JSONObject objs) throws IqbException {
        logger.info("获取罚息列表入参:{}", JSONObject.toJSONString(objs));
        /** 获取商户列表 **/
        List<String> merList = this.getMerchantList(objs);
        objs.put(MERCHANTNOS_KEY, merList);
        objs.remove(MERLIST_KEY);

        /** 调用账户系统 **/
        /** 封装请求数据 **/
        Map<String, Object> financeReqMap = this.packageListPenaltyDeratableParam(objs);
        /** 解析返回数据 **/
        String retStr =
                super.transferWithFinance(consumerConfig.getFinanceBillQueryUrl(),
                        JSONObject.toJSONString(financeReqMap));
        if (!super.isFinanceRetSucc(retStr)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_FINANCE_02000006);
        }
        return super.getFinanceRetList(retStr);
    }

    /**
     * 
     * Description: 封装向账户系统交互传递的参数
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午4:20:47
     */
    private Map<String, Object> packageListPenaltyDeratableParam(JSONObject objs) {
        objs.put(
                ResultConstant.NUMPERPAGE_KEY,
                objs.get(PageConst.PAGESIZE_KEY) == null ? Integer.parseInt(PageConst.PAGESIZE_DEFAULT_VALUE) : objs
                        .get(PageConst.PAGESIZE_KEY));
        objs.put(
                PageConst.PAGENUM_KEY,
                objs.get(PageConst.PAGENUM_KEY) == null ? Integer.parseInt(PageConst.PAGENUM_DEFAULT_VALUE) : objs
                        .get(PageConst.PAGENUM_KEY));
        objs.put(ParameterConstant.GETOVERDUEBILL_KEY, Integer.parseInt(ParameterConstant.GETOVERDUEBILL_DEFAULT_VALUE));
        objs.put(ParameterConstant.STATUS_KEY, ParameterConstant.STATUS_OVERDUE_VALUE);
        return objs;
    }

    /**
     * 
     * Description: 获取商户列表
     * 
     * @param
     * @return List<String>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午4:09:50
     */
    private List<String> getMerchantList(JSONObject objs) {
        objs = super.getMerchLimitObject(objs);
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get(MERLIST_KEY);
        List<String> merList = new ArrayList<String>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        return merList;
    }

    @Override
    public Integer savePenaltyDeratableAndStartWF(JSONObject objs) throws IqbException {
        /** 校验保存请求参数数据 **/
        this.validateSaveReq(objs);
        /** 校验本期申请是否被允许 **/
        this.validatePenaltyExist(objs);

        PenaltyDerateBean penaltyDerateBean = BeanUtil.mapToBean(objs, PenaltyDerateBean.class);
        penaltyDerateBean.setUuid(UUID.randomUUID().toString().replace("-", ""));

        /** 启动工作流 **/
        this.startWF(penaltyDerateBean);

        /** 设置默认值 **/
        this.setDefaultPenaltyDerateInfo(penaltyDerateBean);;

        return this.penaltyDerateBiz.savePenaltyDeratable(penaltyDerateBean);
    }

    /**
     * 
     * Description: 启动工作流
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午5:06:50
     */
    private void startWF(PenaltyDerateBean penaltyDerateBean) throws IqbException {
        /** 流程数据 **/
        Map<String, Object> procDataMap = this.getProcData();
        /** 流程参数数据 **/
        Map<String, Object> variableDataMap = this.getVariableData();
        /** 流程认证数据 **/
        Map<String, Object> authDataMap = this.getAuthData();
        /** 流程业务数据 **/
        Map<String, Object> bizDataMap = this.getbizData(penaltyDerateBean);

        /** 封装传输数据 **/
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put(WFKeyConst.PROCDATA_KEY, procDataMap);
        reqMap.put(WFKeyConst.VARIABLEDATA_KEY, variableDataMap);
        reqMap.put(WFKeyConst.AUTHDATA_KEY, authDataMap);
        reqMap.put(WFKeyConst.BIZDATA_KEY, bizDataMap);

        /** 启动工作流请求地址 **/
        String wfStartReqUrl = consumerConfig.getWfReqEnvBaseUrl() + consumerConfig.getWfStartandcommitprocessUrl();

        /** 调用工作流接口 **/
        String wfRes = this.transferWithWF(wfStartReqUrl, reqMap);

        /** 解析返回数据 **/
        String procInstId = this.getProcInstId(wfRes);

        penaltyDerateBean.setProcInstId(procInstId);
    }

    /**
     * 
     * Description: 解析返回数据中的流程id
     * 
     * @param
     * @return String
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午6:35:34
     */
    private String getProcInstId(String wfRes) throws IqbException {
        if (StringUtil.isEmpty(wfRes)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_WF_03000001);
        }
        JSONObject wfResObjs = JSONObject.parseObject(wfRes);
        this.isWFRetSucc(wfResObjs);
        String procInstId = wfResObjs.getString(WFConst.ProcInstIdKey);
        if (StringUtil.isEmpty(procInstId)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_WF_03000003);
        }
        return procInstId;
    }

    /**
     * 
     * Description: 判断工作流返回成功还是失败
     * 
     * @param
     * @return boolean
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午6:33:47
     */
    private void isWFRetSucc(JSONObject objs) throws IqbException {
        if (objs == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_WF_03000001);
        }
        if (WFConst.WFInterRetSucc.equals(objs.getString(WFConst.WFInterRetSuccKey))) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_WF_03000002);
        }
    }

    /**
     * 
     * Description: 调用工作流接口
     * 
     * @param
     * @return String
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午6:27:55
     */
    private String transferWithWF(String url, Map<String, Object> params) {

        logger.info("调用启动工作流请求地址：{}", url);
        logger.info("启动工作流发送数据：{}", JSONObject.toJSONString(params));
        String retStr = SimpleHttpUtils.httpPost(url, params);
        logger.info("启动工作流返回数据：{}", retStr);

        return retStr;
    }

    /**
     * 
     * Description: 获取流程数据map
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午5:41:12
     */
    private Map<String, Object> getProcData() {
        Map<String, Object> procDataMap = new HashMap<String, Object>();
        procDataMap.put(WFKeyConst.PROCDEFKEY_KEY, BizProcDefKeyConstant.PENALTYDERATE_KEY);
        return procDataMap;
    }

    /**
     * 
     * Description: 获取流程参数map
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午5:42:36
     */
    private Map<String, Object> getVariableData() {
        Map<String, Object> variableDataMap = new HashMap<String, Object>();
        variableDataMap.put(WFKeyConst.PROCAUTHTYPE_KEY, WFKeyConst.PROCAUTHTYPE_SESSION);
        return variableDataMap;
    }

    /**
     * 
     * Description: 获取流程认证数据map
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午5:48:09
     */
    private Map<String, Object> getAuthData() {
        Map<String, Object> authDataMap = new HashMap<String, Object>();
        authDataMap.put(WFKeyConst.PROCAUTHTYPE_KEY, WFKeyConst.PROCAUTHTYPE_SESSION);
        return authDataMap;
    }

    /**
     * 
     * Description: 获取流程业务数据map
     * 
     * @param
     * @return Map<String,Object>
     * @throws
     * @Author wangxinbang Create Date: 2017年3月13日 下午5:48:22
     */
    private Map<String, Object> getbizData(PenaltyDerateBean penaltyDerateBean) {
        Map<String, Object> bizDataMap = new HashMap<String, Object>();
        bizDataMap.put(WFKeyConst.PROCBIZID_KEY, penaltyDerateBean.getUuid());
        bizDataMap.put(WFKeyConst.PROCORGCODE_KEY, sysUserSession.getOrgCode());
        bizDataMap.put(WFKeyConst.PROCBIZMEMO_KEY, penaltyDerateBean.getRealName());
        return bizDataMap;
    }

    /**
     * 
     * Description: 设置罚息申请默认信息
     * 
     * @param
     * @return void
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午3:51:53
     */
    private void setDefaultPenaltyDerateInfo(PenaltyDerateBean penaltyDerateBean) {
        penaltyDerateBean.setApplyStatus(StatusConst.PENALTY_DERATE_APPLY_STATUS_START);
        penaltyDerateBean.setVersion(CommonConstant.DEFAULT_VERSION);
        penaltyDerateBean.setApplyDate(new Date());
    }

    /**
     * 
     * Description: 校验本期申请是否被允许
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午3:02:13
     */
    private void validatePenaltyExist(JSONObject objs) throws IqbException {
        PenaltyDerateBean penaltyDerateBean =
                penaltyDerateBiz.getPenaltyDerateByInstallDetailId(objs.getString(INSTALLDETAILID_KEY));
        if (penaltyDerateBean != null
                && StatusConst.PENALTY_DERATE_APPLY_STATUS_START.equals(penaltyDerateBean.getApplyStatus())) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_PENALTY_DERATE_02000001);
        }
    }

    /**
     * 
     * Description: 校验保存请求参数数据
     * 
     * @param
     * @return void
     * @throws IqbException
     * @throws
     * @Author wangxinbang Create Date: 2017年3月10日 下午2:54:04
     */
    private void validateSaveReq(JSONObject objs) throws IqbException {
        if (objs == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        if (objs.getString(INSTALLDETAILID_KEY) == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000005);
        }
    }

    @Override
    public Integer savePenaltyDeratable(JSONObject objs) throws IqbException {
        /** 校验保存请求参数数据 **/
        this.validateSaveReq(objs);
        /** 校验本期申请是否被允许 **/
        this.validatePenaltyExist(objs);

        PenaltyDerateBean penaltyDerateBean = BeanUtil.mapToBean(objs, PenaltyDerateBean.class);

        /** 设置默认值 **/
        this.setDefaultPenaltyDerateInfo(penaltyDerateBean);;

        return this.penaltyDerateBiz.savePenaltyDeratable(penaltyDerateBean);
    }

    @Override
    public Integer updatePenaltyDeratableApplyStatus(PenaltyDerateBean penaltyDerateBean) throws IqbException {
        if (penaltyDerateBean == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        return this.penaltyDerateBiz.updatePenaltyDeratableApplyStatus(penaltyDerateBean);
    }

    @Override
    public Integer doPenaltyDerate(PenaltyDerateBean penaltyDerateBean) throws IqbException {
        if (penaltyDerateBean == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        PenaltyDerateBean penaltyDerateBeanDB =
                this.penaltyDerateBiz.getPenaltyDerateByUUId(penaltyDerateBean.getUuid());
        /** 调用账户系统 **/
        /** 解析返回数据 **/
        String retStr =
                super.transferWithFinance(consumerConfig.getFinanceBillPenaltyDerataUrl(),
                        JSONObject.toJSONString(penaltyDerateBeanDB));
        if (!super.isFinanceRetSucc(retStr)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_FINANCE_02000008);
        }
        penaltyDerateBean.setApplySuccDate(new Date());
        return this.penaltyDerateBiz.updatePenaltyDeratableApplyStatus(penaltyDerateBean);
    }

    @Override
    public PenaltyDerateBean getPenaltyDerateById(JSONObject objs) throws IqbException {
        if (objs == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        String id = objs.getString(ID_KEY);
        if (StringUtil.isEmpty(id)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000005);
        }
        return this.penaltyDerateBiz.getPenaltyDerateById(id);
    }

    @Override
    public PenaltyDerateBean getPenaltyDerateByUUId(JSONObject objs) throws IqbException {
        if (objs == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        String uuid = objs.getString(UUID_KEY);
        if (StringUtil.isEmpty(uuid)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000005);
        }
        return this.penaltyDerateBiz.getPenaltyDerateByUUId(uuid);
    }

    @Override
    public PenaltyDerateBean getPenaltyDerateByInstallDetailId(JSONObject objs) throws IqbException {
        if (objs == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000001);
        }
        String installDetailId = objs.getString(INSTALLDETAILID_KEY);
        if (StringUtil.isEmpty(installDetailId)) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000005);
        }
        /**
         * 罚息减免流程发起前判断是否发起提前结清流程
         */
        SettleApplyBean settleApplyBean =
                settleApplyBeanBiz.selectSettleBeanByOrderIdForValidate(objs.getString("orderId"));
        if (settleApplyBean != null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_PENALTY_DERATE_02000002);
        }

        PenaltyDerateBean penaltyDerateBean = this.penaltyDerateBiz.getPenaltyDerateByInstallDetailId(installDetailId);
        if (penaltyDerateBean == null
                || StatusConst.PENALTY_DERATE_APPLY_STATUS_FAIL.equals(penaltyDerateBean.getApplyStatus())
                || StatusConst.PENALTY_DERATE_APPLY_STATUS_INTERRUPT.equals(penaltyDerateBean.getApplyStatus())) {
            return penaltyDerateBean;
        } else {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_PENALTY_DERATE_02000001);
        }
    }

    @Override
    public void retrievePenaltyDerate(String uuid) throws IqbException {
        PenaltyDerateBean pdb = penaltyDerateBiz.getByUUid(uuid);
        if (pdb == null) {
            throw new IqbException(ConsumerReturnInfo.CONSUMER_COMMON_01000004);
        }

        pdb.setApplyStatus("3"); // 退回
        penaltyDerateBiz.updatePenaltyDeratableApplyStatus(pdb);
    }

    /**
     * 
     * Description:提前申请流程校验订单在罚息减免流程中是否发起
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月29日
     */
    public PenaltyDerateBean getPenaltyDerateBeanByOrderId(String orderId) {
        PenaltyDerateBean pdb = penaltyDerateBiz.getPenaltyDerateBeanByOrderId(orderId);
        return pdb;
    }

    /**
     * 
     * Description:按格式生成自增序列
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月10日
     */
    public synchronized String getSeqFromRedis(String key, boolean isSub) {
        /** 数字格式化 **/
        DecimalFormat df = new DecimalFormat(STR_FORMAT);
        /** 从redis中取值 **/
        String val = this.redisPlatformDao.getValueByKey(key);
        if (StringUtil.isEmpty(val)) {
            this.redisPlatformDao.setKeyAndValue(key, this.INITIAL_SEQ);
            return df.format(Integer.parseInt(this.INITIAL_SEQ));
        }
        Integer seq = Integer.parseInt(val);

        /** 判断是否进行减法操作 **/
        if (isSub) {
            seq = seq - 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        } else {
            seq = seq + 1;
            this.redisPlatformDao.setKeyAndValue(key, seq.toString());
        }
        return df.format(seq);
    }
}
