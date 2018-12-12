package com.iqb.consumer.service.layer.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.IsEmptyCheck.CheckGroup;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerBasicInfo;
import com.iqb.consumer.data.layer.bean.carowner.CarOwnerCheckInfo;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.bean.user.UserRiskBaseInfo;
import com.iqb.consumer.data.layer.bean.user.UserRiskInfo;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.RiskInfoBiz;
import com.iqb.consumer.data.layer.biz.account.AccountManager;
import com.iqb.consumer.data.layer.biz.authoritycard.AuthorityCardBiz;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserRiskBiz;
import com.iqb.consumer.service.layer.admin.AdminServiceImpl;
import com.iqb.consumer.service.layer.api.AssetApiService;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.exception.IqbSqlException;

import jodd.util.StringUtil;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Autowired
    UserRiskBiz userRiskBiz;

    @Autowired
    AccountManager accountManager;

    @Autowired
    private OrderBiz orderBiz;

    @Autowired
    private RiskInfoBiz riskInfoBiz;

    @Autowired
    private BankCardBeanBiz bankCardBeanBiz;

    @Autowired
    private AuthorityCardBiz authorityCardBiz;

    @Autowired
    private WFConfigBiz wfConfigBiz;

    @Autowired
    private MerchantBeanBiz merchantBeanBiz;

    @Autowired
    private AssetApiService assetApiServiceImpl;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int updateUserInfo(JSONObject objs) throws IqbException, IqbSqlException {
        return userRiskBiz.updateUserInfo((Map) objs);
    }

    @Override
    public void deleteUserByID(JSONObject objs) throws IqbException, IqbSqlException {
        userRiskBiz.deleteUserByID(objs.getString("regId"));
    }

    @Override
    public UserRiskInfo getUserById(JSONObject objs) throws IqbException, IqbSqlException {
        UserRiskBaseInfo uRBI = userRiskBiz.getUserById(objs.getString("reg_id"));
        UserRiskInfo uRI = new UserRiskInfo();
        uRI.setReg_id(uRBI.getRegId());
        uRI.setReal_name(uRBI.getRealName());
        uRI.setId_card(uRBI.getIdNo());
        uRI.setPay_account(uRBI.getBankCard());

        /**
         * 重新组装list FINANCE-2882 个人客户信息管理中银行卡更新为客户最新还款的信息
         */
        BankCardBean bankCardBean =
                bankCardBeanBiz.selectBankCardInfoByUserId(String.valueOf(uRBI.getUserId()), "3");
        if (bankCardBean != null) {
            uRI.setPay_account(bankCardBean.getBankCardNo());
        } else {
            bankCardBean = bankCardBeanBiz.selectBankCardInfoByUserId(String.valueOf(uRBI.getUserId()), "2");
            if (bankCardBean != null) {
                uRI.setPay_account(bankCardBean.getBankCardNo());
            }
        }

        JSONObject js = JSONObject.parseObject(uRBI.getCheckInfo());
        if (null != js) {
            uRI.setLink_man_first(js.getString("contactname1"));
            uRI.setLink_man_second(js.getString("contactname2"));
            uRI.setLink_man_first_phone(js.getString("contactphone1"));
            uRI.setLink_man_second_phone(js.getString("contactphone2"));
            uRI.setMatital_status(js.getString("marriedstatus"));
            uRI.setPermanent_address(js.getString("addprovince"));
        }
        return uRI;
    }

    @Override
    public PageInfo<UserRiskInfo> getUserByMerNos(JSONObject objs) throws IqbException, IqbSqlException {
        List<UserRiskBaseInfo> list = userRiskBiz.getUserByMerNos(objs);
        List<UserRiskInfo> newList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(list)) {
            for (UserRiskBaseInfo uRBI : list) {
                UserRiskInfo uRI = new UserRiskInfo();

                uRI.setReg_id(uRBI.getRegId());
                uRI.setReal_name(uRBI.getRealName());
                uRI.setId_card(uRBI.getIdNo());
                uRI.setPay_account(uRBI.getBankCard());
                uRI.setRisk_type(uRBI.getRiskType());
                uRI.setSmsMobile(uRBI.getSmsMobile());
                /**
                 * 重新组装list FINANCE-2882 个人客户信息管理中银行卡更新为客户最新还款的信息
                 */
                BankCardBean bankCardBean =
                        bankCardBeanBiz.selectBankCardInfoByUserId(String.valueOf(uRBI.getUserId()), "3");
                if (bankCardBean != null) {
                    uRI.setPay_account(bankCardBean.getBankCardNo());
                    uRI.setBankName(bankCardBean.getBankName());
                    uRI.setBankMobile(bankCardBean.getBankMobile());
                } else {
                    bankCardBean = bankCardBeanBiz.selectBankCardInfoByUserId(String.valueOf(uRBI.getUserId()), "2");
                    if (bankCardBean != null) {
                        uRI.setPay_account(bankCardBean.getBankCardNo());
                        uRI.setBankName(bankCardBean.getBankName());
                        uRI.setBankMobile(bankCardBean.getBankMobile());
                    }
                }

                JSONObject js = JSONObject.parseObject(uRBI.getCheckInfo());
                if (null != js) {
                    uRI.setLink_man_first(js.getString("contactname1"));
                    uRI.setLink_man_second(js.getString("contactname2"));
                    uRI.setLink_man_first_phone(js.getString("contactphone1"));
                    uRI.setLink_man_second_phone(js.getString("contactphone2"));
                    uRI.setMatital_status(js.getString("marriedstatus"));
                    uRI.setPermanent_address(js.getString("addprovince"));
                }
                newList.add(uRI);
            }
        }
        PageInfo<UserRiskBaseInfo> urbp = new PageInfo<>(list);
        PageInfo<UserRiskInfo> urp = new PageInfo<>(newList);
        urp.setEndRow(urbp.getEndRow());
        urp.setFirstPage(urbp.getFirstPage());
        urp.setHasNextPage(urbp.isHasNextPage());
        urp.setHasPreviousPage(urbp.isHasPreviousPage());
        urp.setIsFirstPage(urbp.isIsFirstPage());
        urp.setIsLastPage(urbp.isIsLastPage());
        urp.setNavigatepageNums(urbp.getNavigatepageNums());
        urp.setNextPage(urbp.getNextPage());
        urp.setOrderBy(urbp.getOrderBy());
        urp.setPageNum(urbp.getPageNum());
        urp.setPages(urbp.getPages());
        urp.setPageSize(urbp.getPageSize());
        urp.setPrePage(urbp.getPrePage());
        urp.setSize(urbp.getSize());
        urp.setStartRow(urbp.getStartRow());
        urp.setTotal(urbp.getTotal());
        return urp;
    }

    @Override
    public Long saveOrUpdateUserInfo(JSONObject requestMessage) throws GenerallyException, DevDefineErrorMsgException {
        UserBean ub = JSONObject.toJavaObject(requestMessage, UserBean.class);
        if (StringUtil.isEmpty(ub.getRegId()) && StringUtil.isEmpty(ub.getIdNo())) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        return accountManager.saveOrUpdateUserInfo(ub);
    }

    @Override
    @Transactional
    public void carOwnerBasicInfo(JSONObject requestMessage) throws Exception {

        CarOwnerBasicInfo cobi = JSONObject.toJavaObject(requestMessage, CarOwnerBasicInfo.class);
        if (cobi == null) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        /** step 1 保存用户信息(手机号和身份证确认唯一，存在就更新。) 姓名，手机号，身份证 保存inst_user 生成ID **/

        UserBean ub = cobi.getUb();
        if (ub == null || StringUtil.isEmpty(ub.getRegId()) && StringUtil.isEmpty(ub.getIdNo())) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        ub.setSmsMobile(ub.getRegId());
        accountManager.saveOrUpdateUserInfo(ub);
        Long userId = ub.getId();
        /** step 2 保存订单信息 生成订单号，用户userId，申请金额，申请期限, 订单名称：品牌-型号 **/

        InstOrderInfoEntity ioie = cobi.getIoie();
        if (ioie == null || !ioie.check(CheckGroup.A)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.B);
        }
        ioie.setUserId(userId);
        String orderNo = requestMessage.getString("uuid");
        String orderId = assetApiServiceImpl.generateOrderId(ioie.getMerchantNo(), ioie.getBizType());
        ioie.setOrderId(orderId);
        ioie.setOrderNo(orderNo);// 第一节点图片对应的uuid
        orderBiz.createIOIE(ioie);

        /** step 3 银行信息 银行卡号，userid，手机号，银行卡开户行(可空) inst_bankcard **/
        BankCardBean bcb = cobi.getBcb();
        if (bcb == null || !bcb.check(CheckGroup.A)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.C);
        }
        bcb.setUserId(userId);
        bankCardBeanBiz.saveBankCard(bcb);

        /** step 4 风控信息 **/
        RiskInfoBean<CarOwnerCheckInfo> rib = cobi.getRib(orderId, ioie.getMerchantNo());
        riskInfoBiz.saveRiskInfo(rib);
        /** step 5 保存inst_authoritycard 信息 订单号，车牌号，车龄(年) **/
        AuthorityCardBean acb = cobi.getAcb();
        if (acb == null || !acb.check(CheckGroup.A)) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.D);
        }
        acb.setOrderId(orderId);
        authorityCardBiz.insertAuthorityCard(acb);

        MerchantBean mb = merchantBeanBiz.getMerByMerNo(ioie.getMerchantNo());
        // 流程摘要：姓名+门店简称+车牌号
        String digest =
                ub.getRealName() + ";" + mb.getMerchantShortName() + ";" + acb.getPlate();
        wfAndProcId(mb, digest, ioie);

    }

    private void wfAndProcId(MerchantBean mb, String desc, InstOrderInfoEntity ioie) throws IqbException {
        // 启动工作流
        try {
            WFConfig wfConfig = wfConfigBiz.getConfigByBizType(ioie.getBizType(), ioie.getWfStatus());
            LinkedHashMap linkedHashMap =
                    startWF(wfConfig, ioie.getRegId(), desc, ioie.getOrderId(), mb.getId());
            if (linkedHashMap != null && "1".equals((linkedHashMap.get("success") + ""))) {
                // 成功回写procInstId
                // 回写工作流标识ID
                @SuppressWarnings("unchecked")
                Map<String, String> procInstMap = (Map<String, String>) linkedHashMap.get("iqbResult");
                String procInstId = procInstMap.get("procInstId");
                Map<String, Object> params = new HashMap<>();
                params.put("id", ioie.getId());
                params.put("procInstId", procInstId);
                orderBiz.updateProcInstId(params);
            } else {
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000001);
            }
        } catch (IqbException e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
    }

    private LinkedHashMap startWF(WFConfig wfConfig, String regId, String procBizMemo, String orderId, Long orgId)
            throws IqbException {
        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procDefKey", wfConfig.getProcDefKey());

        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1");
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", regId);
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");

        Map<String, Object> hmBizData = new HashMap<>();
        hmBizData.put("procBizId", orderId);
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", orgId + "");
        hmBizData.put("procBizMemo", procBizMemo);
        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);

        String url = wfConfig.getStartWfurl();
        // 发送Https请求
        LinkedHashMap responseMap = null;
        log.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        try {
            responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        } catch (Exception e) {
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        Long endTime = System.currentTimeMillis();
        log.info("调用工作流接口返回信息：{}" + responseMap);
        return responseMap;
    }

}
