/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月12日 上午10:54:07
 * @version V1.0
 */
package com.iqb.consumer.manage.front;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Configuration
public class ParamConfig {

    @Value("${XF.SUCC2PAGE}")
    private String succ2Page;
    @Value("${XF.FAILED2PAGE}")
    private String failed2Page;
    @Value("${XF.CASHIER.RETURN.URL}")
    private String cashierReturnUrl;// 收银台同步URL
    @Value("${XF.CASHIER.NOTICE.URL}")
    private String cashierNoticeUrl;// 收银台异步URL
    @Value("${XF.CASHIER.PRE.NOTICE.URL}")
    private String cashierPreNoticeUrl;// 收银台异步URL
    @Value("${XF.QUICK.RETURN.URL}")
    private String quickReturnUrl;// 快捷支付同步URL
    @Value("${XF.QUICK.NOTICE.URL}")
    private String quickNoticeUrl;// 快捷支付异步URL
    @Value("${XF.QUICK.PRE.NOTICE.URL}")
    private String quickPreNoticeUrl;// 快捷支付异步URL

    @Value("${XF.SETTLE.QUICK.URL}")
    private String settleQuickUrl;
    @Value("${XF.SETTLE.CASHIER.URL}")
    private String settleCashierUrl;

    @Value("${image_upload_folder}")
    private String image_upload_folder;// 图片上传文件夹目录
    @Value("${idumai_riskcontrol_notice_url}")
    private String idumai_riskcontrol_notice_url;// 风控新车回调通知接口
    @Value("${idumai_riskcontrol_checkorder_url}")
    private String idumai_riskcontrol_checkorder_url;// 风控核对订单接口
    @Value("${idumai_riskcontrol_oldcar_notice_url}")
    private String idumai_riskcontrol_oldcar_notice_url;// 风控二手车回调通知接口
    @Value("${idumai_riskcontrol_pledge_notice_url}")
    private String idumai_riskcontrol_pledge_notice_url;// 抵押车风控回调通知接口

    @Value("${MANAGER.SEND2RISK.YZDS.URL}")
    private String managerSend2RiskYZDSurl;
    @Value("${MANAGER.SEND2RISK.DY.URL}")
    private String managerSend2RiskDYurl;
    @Value("${MANAGER.WFRETURN.YZDS.URL}")
    private String managerWfreturnYZDSurl;
    @Value("${MANAGER.WFRETURN.DY.URL}")
    private String managerWfreturnDYurl;
    @Value("${MANAGER.MODIFYORDER.URL}")
    private String managerModifyOrderUrl;
    @Value("${CMD.COPYINFO.URL}")
    private String cmdCopyInfoUrl;

    @Value("${MANAGER.WFRETURN.PGYH.RISK.URL}")
    private String managerSend2RiskPGYHurl; // 蒲公英行风控接口地址
    @Value("${MANAGER.WFRETURN.PGYH.CALLBACK.URL}")
    private String pgyh_riskcontrol_notice_url;// 蒲公英行风控返回接口地址
    @Value("${MANAGER.WFRETURN.PGYH.URL}")
    private String managerWfreturnPGYHurl; // 蒲公英行项目信息接口地址

    /* 电子合同相关 */
    @Value("${iqb_contract_make_contract_url}")
    private String iqb_contract_make_contract_url;// 生成合同接口
    @Value("${iqb_contract_submit_contract_url}")
    private String iqb_contract_submit_contract_url;// 提交合同接口
    @Value("${iqb_contract_notify_contract_url}")
    private String iqb_contract_notify_contract_url;// 生成合同回调接口
    @Value("${iqb_contract_task_contract_url}")
    private String iqb_contract_task_contract_url;// 实际借款人签名后，回调地址

    @Value("${iqb_contract_sign_notify_contract_url_2}")
    private String iqb_contract_sign_notify_contract_url_2;// 合同签署完毕回调接口，帮帮手
    @Value("${iqb_contract_sign_notify_contract_url_7}")
    private String iqb_contract_sign_notify_contract_url_7;// 合同签署完毕回调接口，轮动
    @Value("${iqb_contract_sign_notify_contract_url_4}")
    private String iqb_contract_sign_notify_contract_url_4;// 合同签署完毕回调接口，先有车
    @Value("${iqb_contract_sign_notify_contract_url_10}")
    private String iqb_contract_sign_notify_contract_url_10;// 合同签署完毕回调接口，客户家

    /** 标准接口-先锋预付款回调接口 **/
    @Value("${INTO.XF.QUICK.PRE.NOTICE.URL}")
    private String intoXFQuickPreNoticeUrl;
    /** 标准接口-先锋正常还款回调接口 **/
    @Value("${INTO.XF.QUICK.NORMAL.NOTICE.URL}")
    private String intoXFQuickNormalNoticeUrl;

    /** 人工风控随机结果 环境标识 **/
    @Value("${RISK.RANDOM.ORDERFLAG.ENV}")
    private String envFlag;
    /** 鉴权URL地址 **/
    @Value("${authInfoUrl}")
    private String authInfoUrl;

    @Value("${MANAGER.WFRETURN.HYZZD.URL}")
    private String managerWfreturnHYZZDurl; // 华益周转贷项目信息接口地址

    @Value("${MANAGER.WFRETURN.YZDD.URL}")
    private String managerWfreturnYZDDurl; // 以租代购并行-项目信息接口地址

    @Value("${MANAGER.WFRETURN.FRB.URL}")
    private String managerWfreturnFRBurl; // 房融保-项目信息接口地址

    public String getSettleQuickUrl() {
        return settleQuickUrl;
    }

    public void setSettleQuickUrl(String settleQuickUrl) {
        this.settleQuickUrl = settleQuickUrl;
    }

    public String getSettleCashierUrl() {
        return settleCashierUrl;
    }

    public void setSettleCashierUrl(String settleCashierUrl) {
        this.settleCashierUrl = settleCashierUrl;
    }

    public String getAuthInfoUrl() {
        return authInfoUrl;
    }

    public void setAuthInfoUrl(String authInfoUrl) {
        this.authInfoUrl = authInfoUrl;
    }

    public String getCmdCopyInfoUrl() {
        return cmdCopyInfoUrl;
    }

    public void setCmdCopyInfoUrl(String cmdCopyInfoUrl) {
        this.cmdCopyInfoUrl = cmdCopyInfoUrl;
    }

    public String getIqb_contract_sign_notify_contract_url_2() {
        return iqb_contract_sign_notify_contract_url_2;
    }

    public void setIqb_contract_sign_notify_contract_url_2(String iqb_contract_sign_notify_contract_url_2) {
        this.iqb_contract_sign_notify_contract_url_2 = iqb_contract_sign_notify_contract_url_2;
    }

    public String getIqb_contract_sign_notify_contract_url_7() {
        return iqb_contract_sign_notify_contract_url_7;
    }

    public void setIqb_contract_sign_notify_contract_url_7(String iqb_contract_sign_notify_contract_url_7) {
        this.iqb_contract_sign_notify_contract_url_7 = iqb_contract_sign_notify_contract_url_7;
    }

    public String getIqb_contract_sign_notify_contract_url_4() {
        return iqb_contract_sign_notify_contract_url_4;
    }

    public void setIqb_contract_sign_notify_contract_url_4(String iqb_contract_sign_notify_contract_url_4) {
        this.iqb_contract_sign_notify_contract_url_4 = iqb_contract_sign_notify_contract_url_4;
    }

    public String getIqb_contract_sign_notify_contract_url_10() {
        return iqb_contract_sign_notify_contract_url_10;
    }

    public void setIqb_contract_sign_notify_contract_url_10(String iqb_contract_sign_notify_contract_url_10) {
        this.iqb_contract_sign_notify_contract_url_10 = iqb_contract_sign_notify_contract_url_10;
    }

    public String getIqb_contract_task_contract_url() {
        return iqb_contract_task_contract_url;
    }

    public void setIqb_contract_task_contract_url(
            String iqb_contract_task_contract_url) {
        this.iqb_contract_task_contract_url = iqb_contract_task_contract_url;
    }

    public String getIqb_contract_notify_contract_url() {
        return iqb_contract_notify_contract_url;
    }

    public void setIqb_contract_notify_contract_url(
            String iqb_contract_notify_contract_url) {
        this.iqb_contract_notify_contract_url = iqb_contract_notify_contract_url;
    }

    public String getIqb_contract_submit_contract_url() {
        return iqb_contract_submit_contract_url;
    }

    public void setIqb_contract_submit_contract_url(
            String iqb_contract_submit_contract_url) {
        this.iqb_contract_submit_contract_url = iqb_contract_submit_contract_url;
    }

    public String getIqb_contract_make_contract_url() {
        return iqb_contract_make_contract_url;
    }

    public void setIqb_contract_make_contract_url(
            String iqb_contract_make_contract_url) {
        this.iqb_contract_make_contract_url = iqb_contract_make_contract_url;
    }

    public String getManagerModifyOrderUrl() {
        return managerModifyOrderUrl;
    }

    public void setManagerModifyOrderUrl(String managerModifyOrderUrl) {
        this.managerModifyOrderUrl = managerModifyOrderUrl;
    }

    public String getManagerSend2RiskYZDSurl() {
        return managerSend2RiskYZDSurl;
    }

    public void setManagerSend2RiskYZDSurl(String managerSend2RiskYZDSurl) {
        this.managerSend2RiskYZDSurl = managerSend2RiskYZDSurl;
    }

    public String getManagerSend2RiskDYurl() {
        return managerSend2RiskDYurl;
    }

    public void setManagerSend2RiskDYurl(String managerSend2RiskDYurl) {
        this.managerSend2RiskDYurl = managerSend2RiskDYurl;
    }

    public String getManagerWfreturnYZDSurl() {
        return managerWfreturnYZDSurl;
    }

    public void setManagerWfreturnYZDSurl(String managerWfreturnYZDSurl) {
        this.managerWfreturnYZDSurl = managerWfreturnYZDSurl;
    }

    public String getManagerWfreturnDYurl() {
        return managerWfreturnDYurl;
    }

    public void setManagerWfreturnDYurl(String managerWfreturnDYurl) {
        this.managerWfreturnDYurl = managerWfreturnDYurl;
    }

    public String getIdumai_riskcontrol_oldcar_notice_url() {
        return idumai_riskcontrol_oldcar_notice_url;
    }

    public void setIdumai_riskcontrol_oldcar_notice_url(
            String idumai_riskcontrol_oldcar_notice_url) {
        this.idumai_riskcontrol_oldcar_notice_url = idumai_riskcontrol_oldcar_notice_url;
    }

    public String getIdumai_riskcontrol_pledge_notice_url() {
        return idumai_riskcontrol_pledge_notice_url;
    }

    public void setIdumai_riskcontrol_pledge_notice_url(
            String idumai_riskcontrol_pledge_notice_url) {
        this.idumai_riskcontrol_pledge_notice_url = idumai_riskcontrol_pledge_notice_url;
    }

    public String getIdumai_riskcontrol_notice_url() {
        return idumai_riskcontrol_notice_url;
    }

    public void setIdumai_riskcontrol_notice_url(
            String idumai_riskcontrol_notice_url) {
        this.idumai_riskcontrol_notice_url = idumai_riskcontrol_notice_url;
    }

    public String getIdumai_riskcontrol_checkorder_url() {
        return idumai_riskcontrol_checkorder_url;
    }

    public void setIdumai_riskcontrol_checkorder_url(
            String idumai_riskcontrol_checkorder_url) {
        this.idumai_riskcontrol_checkorder_url = idumai_riskcontrol_checkorder_url;
    }

    public String getSucc2Page() {
        return succ2Page;
    }

    public void setSucc2Page(String succ2Page) {
        this.succ2Page = succ2Page;
    }

    public String getFailed2Page() {
        return failed2Page;
    }

    public void setFailed2Page(String failed2Page) {
        this.failed2Page = failed2Page;
    }

    public String getImage_upload_folder() {
        return image_upload_folder;
    }

    public void setImage_upload_folder(String image_upload_folder) {
        this.image_upload_folder = image_upload_folder;
    }

    public String getCashierPreNoticeUrl() {
        return cashierPreNoticeUrl;
    }

    public void setCashierPreNoticeUrl(String cashierPreNoticeUrl) {
        this.cashierPreNoticeUrl = cashierPreNoticeUrl;
    }

    public String getQuickPreNoticeUrl() {
        return quickPreNoticeUrl;
    }

    public void setQuickPreNoticeUrl(String quickPreNoticeUrl) {
        this.quickPreNoticeUrl = quickPreNoticeUrl;
    }

    public String getCashierReturnUrl() {
        return cashierReturnUrl;
    }

    public void setCashierReturnUrl(String cashierReturnUrl) {
        this.cashierReturnUrl = cashierReturnUrl;
    }

    public String getCashierNoticeUrl() {
        return cashierNoticeUrl;
    }

    public void setCashierNoticeUrl(String cashierNoticeUrl) {
        this.cashierNoticeUrl = cashierNoticeUrl;
    }

    public String getQuickReturnUrl() {
        return quickReturnUrl;
    }

    public void setQuickReturnUrl(String quickReturnUrl) {
        this.quickReturnUrl = quickReturnUrl;
    }

    public String getQuickNoticeUrl() {
        return quickNoticeUrl;
    }

    public void setQuickNoticeUrl(String quickNoticeUrl) {
        this.quickNoticeUrl = quickNoticeUrl;
    }

    public String getManagerWfreturnPGYHurl() {
        return managerWfreturnPGYHurl;
    }

    public void setManagerWfreturnPGYHurl(String managerWfreturnPGYHurl) {
        this.managerWfreturnPGYHurl = managerWfreturnPGYHurl;
    }

    public String getManagerSend2RiskPGYHurl() {
        return managerSend2RiskPGYHurl;
    }

    public void setManagerSend2RiskPGYHurl(String managerSend2RiskPGYHurl) {
        this.managerSend2RiskPGYHurl = managerSend2RiskPGYHurl;
    }

    public String getPgyh_riskcontrol_notice_url() {
        return pgyh_riskcontrol_notice_url;
    }

    public void setPgyh_riskcontrol_notice_url(String pgyh_riskcontrol_notice_url) {
        this.pgyh_riskcontrol_notice_url = pgyh_riskcontrol_notice_url;
    }

    /**
     * @return the intoXFQuickPreNoticeUrl
     */
    public String getIntoXFQuickPreNoticeUrl() {
        return intoXFQuickPreNoticeUrl;
    }

    /**
     * @param intoXFQuickPreNoticeUrl the intoXFQuickPreNoticeUrl to set
     */
    public void setIntoXFQuickPreNoticeUrl(String intoXFQuickPreNoticeUrl) {
        this.intoXFQuickPreNoticeUrl = intoXFQuickPreNoticeUrl;
    }

    /**
     * @return the intoXFQuickNormalNoticeUrl
     */
    public String getIntoXFQuickNormalNoticeUrl() {
        return intoXFQuickNormalNoticeUrl;
    }

    /**
     * @param intoXFQuickNormalNoticeUrl the intoXFQuickNormalNoticeUrl to set
     */
    public void setIntoXFQuickNormalNoticeUrl(String intoXFQuickNormalNoticeUrl) {
        this.intoXFQuickNormalNoticeUrl = intoXFQuickNormalNoticeUrl;
    }

    public String getEnvFlag() {
        return envFlag;
    }

    public void setEnvFlag(String envFlag) {
        this.envFlag = envFlag;
    }

    public String getManagerWfreturnHYZZDurl() {
        return managerWfreturnHYZZDurl;
    }

    public void setManagerWfreturnHYZZDurl(String managerWfreturnHYZZDurl) {
        this.managerWfreturnHYZZDurl = managerWfreturnHYZZDurl;
    }

    public String getManagerWfreturnYZDDurl() {
        return managerWfreturnYZDDurl;
    }

    public void setManagerWfreturnYZDDurl(String managerWfreturnYZDDurl) {
        this.managerWfreturnYZDDurl = managerWfreturnYZDDurl;
    }

    public String getManagerWfreturnFRBurl() {
        return managerWfreturnFRBurl;
    }

    public void setManagerWfreturnFRBurl(String managerWfreturnFRBurl) {
        this.managerWfreturnFRBurl = managerWfreturnFRBurl;
    }

}
