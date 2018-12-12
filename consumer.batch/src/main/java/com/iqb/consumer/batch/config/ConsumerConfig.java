package com.iqb.consumer.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description: consumer配置文件
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月27日    wangxinbang       1.0        1.0 Version
 *          </pre>
 */
@Configuration
public class ConsumerConfig {

    /******************************************** 加密相关 ***********************************************/
    /** 公钥地址 **/
    @Value("${COMMON.PUBLICKEY}")
    private String commonPublicKey;
    /** 私钥地址 **/
    @Value("${COMMON.PRIVATEKEY}")
    private String commonPrivateKey;

    /******************************************** 请求地址 ***********************************************/
    /** 账户基础请求地址 **/
    @Value("${BASE.REQ.URL.FINANCE}")
    private String financeBaseReqUrl;
    /** 账户代偿查询请求地址 **/
    @Value("${FINANCE.BILL.QUERY.URL}")
    private String financeBillQueryUrl;
    /** 账户分期请求地址 **/
    @Value("${FINANCE.INSTALL.INST.URL}")
    private String financeInstallInstUrl;
    /** 账户账单分页查询请求地址 **/
    @Value("${FINANCE.BILL.GETPAGELIST.URL}")
    private String financeBillGetPagelistUrl;
    /** 账户平账接口请求地址 **/
    @Value("${FINANCE.BILL.REFUND.URL}")
    private String financeBillRefundUrl;
    /** 校验代偿账单接口请求地址 **/
    @Value("${FINANCE.BILL.VALIDATE.URL}")
    private String financeBillValidateUrl;
    /** 用户开户接口请求地址 **/
    @Value("${FINANCE.ACCOUNT.OPENACC.URL}")
    private String financeAccountOpenAccUrl;
    /** 用户开户接口请求地址 **/
    @Value("${FINANCE.ACCOUNT.QUERYACC.URL}")
    private String financeAccountQueryAccUrl;
    /** 财务应付明细查询请求地址 **/
    @Value("${FINANCE.BILL.SHOULDDEPTDETAIL.URL}")
    private String financeBillShouldDeptDetailUrl;
    /** 财务应付明细查询请求地址2 **/
    @Value("${FINANCE.BILL.SHOULDDEPTDETAIL2.URL}")
    private String financeBillShouldDeptDetail2Url;
    /** 财务应付明细查询请求地址(不分页) **/
    @Value("${FINANCE.BILL.STOCKSTATISTICS.URL}")
    private String financeBillStockStatisticsUrl;
    /** 财务应付明细查询请求地址(分页) **/
    @Value("${FINANCE.BILL.STOCKSTATISTICS.PAGE.URL}")
    private String financeBillStockStatisticsPageUrl;
    /** 账户罚息减免地址 **/
    @Value("${FINANCE.BILL.PENALTY.DERATA.URL}")
    private String financeBillPenaltyDerataUrl;

    /** 查询每日到期账单 **/
    @Value("${FINANCE.BILL.EVERYDAYEXPIREBILL.URL}")
    private String everyDayExpireBill;
    /** 获取距当前系统时间三天后的账单 **/
    @Value("${FINANCE.BILL.GETLATELYTHREEDAYSBILL.URL}")
    private String getLatelyThreeDaysBill;
    /** 根据订单号 期数查询账单信息 **/
    @Value("${FINANCE.BILL.INSTALLMENTBILLINFO.URL}")
    private String installmentBillInfoByOrderId;
    /**************************************** 工作流相关配置 ****************************************/
    /** 工作流基础地址 **/
    @Value("${WF.REQ.ENV.BASE.URL}")
    private String wfReqEnvBaseUrl;
    /** 启动工作流 **/
    @Value("${WF.STARTANDCOMMITPROCESS.URL}")
    private String wfStartandcommitprocessUrl;
    /** 已逾期且逾期天数小于等于5天的账单 **/
    @Value("${iqb.finance.queryBillLastDateThree}")
    private String queryBillLastDateThree;
    /** 批量根据订单号还有当前期数查询账单信息 **/
    @Value("${iqb.finance.queryBillInfoByOrderIdPage}")
    private String queryBillInfoByOrderIdPage;
    /** 已逾期且逾期天数大于5天的账单 **/
    @Value("${iqb.finance.queryBillLastDateFive}")
    private String queryBillLastDateFive;

    /** 中阁短信发送接口 **/
    @Value("${iqb.front.smsUrl}")
    private String smsUrl;

    public String getQueryBillLastDateFive() {
        return queryBillLastDateFive;
    }

    public void setQueryBillLastDateFive(String queryBillLastDateFive) {
        this.queryBillLastDateFive = queryBillLastDateFive;
    }

    public String getQueryBillInfoByOrderIdPage() {
        return queryBillInfoByOrderIdPage;
    }

    public void setQueryBillInfoByOrderIdPage(String queryBillInfoByOrderIdPage) {
        this.queryBillInfoByOrderIdPage = queryBillInfoByOrderIdPage;
    }

    public String getQueryBillLastDateThree() {
        return queryBillLastDateThree;
    }

    public void setQueryBillLastDateThree(String queryBillLastDateThree) {
        this.queryBillLastDateThree = queryBillLastDateThree;
    }

    public String getFinanceBillStockStatisticsUrl() {
        return financeBillStockStatisticsUrl;
    }

    public void setFinanceBillStockStatisticsUrl(String financeBillStockStatisticsUrl) {
        this.financeBillStockStatisticsUrl = financeBillStockStatisticsUrl;
    }

    public String getFinanceBillStockStatisticsPageUrl() {
        return financeBillStockStatisticsPageUrl;
    }

    public void setFinanceBillStockStatisticsPageUrl(String financeBillStockStatisticsPageUrl) {
        this.financeBillStockStatisticsPageUrl = financeBillStockStatisticsPageUrl;
    }

    public String getFinanceBillPenaltyDerataUrl() {
        return financeBillPenaltyDerataUrl;
    }

    public void setFinanceBillPenaltyDerataUrl(String financeBillPenaltyDerataUrl) {
        this.financeBillPenaltyDerataUrl = financeBillPenaltyDerataUrl;
    }

    public String getFinanceBillShouldDeptDetail2Url() {
        return financeBillShouldDeptDetail2Url;
    }

    public void setFinanceBillShouldDeptDetail2Url(String financeBillShouldDeptDetail2Url) {
        this.financeBillShouldDeptDetail2Url = financeBillShouldDeptDetail2Url;
    }

    public String getFinanceBillShouldDeptDetailUrl() {
        return financeBillShouldDeptDetailUrl;
    }

    public void setFinanceBillShouldDeptDetailUrl(String financeBillShouldDeptDetailUrl) {
        this.financeBillShouldDeptDetailUrl = financeBillShouldDeptDetailUrl;
    }

    public String getFinanceAccountQueryAccUrl() {
        return financeAccountQueryAccUrl;
    }

    public void setFinanceAccountQueryAccUrl(String financeAccountQueryAccUrl) {
        this.financeAccountQueryAccUrl = financeAccountQueryAccUrl;
    }

    public String getFinanceAccountOpenAccUrl() {
        return financeAccountOpenAccUrl;
    }

    public void setFinanceAccountOpenAccUrl(String financeAccountOpenAccUrl) {
        this.financeAccountOpenAccUrl = financeAccountOpenAccUrl;
    }

    public String getFinanceBillValidateUrl() {
        return financeBillValidateUrl;
    }

    public void setFinanceBillValidateUrl(String financeBillValidateUrl) {
        this.financeBillValidateUrl = financeBillValidateUrl;
    }

    public String getFinanceBillRefundUrl() {
        return financeBillRefundUrl;
    }

    public void setFinanceBillRefundUrl(String financeBillRefundUrl) {
        this.financeBillRefundUrl = financeBillRefundUrl;
    }

    public String getFinanceBillGetPagelistUrl() {
        return financeBillGetPagelistUrl;
    }

    public void setFinanceBillGetPagelistUrl(String financeBillGetPagelistUrl) {
        this.financeBillGetPagelistUrl = financeBillGetPagelistUrl;
    }

    public String getFinanceInstallInstUrl() {
        return financeInstallInstUrl;
    }

    public void setFinanceInstallInstUrl(String financeInstallInstUrl) {
        this.financeInstallInstUrl = financeInstallInstUrl;
    }

    public String getFinanceBillQueryUrl() {
        return financeBillQueryUrl;
    }

    public void setFinanceBillQueryUrl(String financeBillQueryUrl) {
        this.financeBillQueryUrl = financeBillQueryUrl;
    }

    public String getCommonPublicKey() {
        return commonPublicKey;
    }

    public void setCommonPublicKey(String commonPublicKey) {
        this.commonPublicKey = commonPublicKey;
    }

    public String getCommonPrivateKey() {
        return commonPrivateKey;
    }

    public void setCommonPrivateKey(String commonPrivateKey) {
        this.commonPrivateKey = commonPrivateKey;
    }

    public String getFinanceBaseReqUrl() {
        return financeBaseReqUrl;
    }

    public void setFinanceBaseReqUrl(String financeBaseReqUrl) {
        this.financeBaseReqUrl = financeBaseReqUrl;
    }

    public String getWfReqEnvBaseUrl() {
        return wfReqEnvBaseUrl;
    }

    public void setWfReqEnvBaseUrl(String wfReqEnvBaseUrl) {
        this.wfReqEnvBaseUrl = wfReqEnvBaseUrl;
    }

    public String getWfStartandcommitprocessUrl() {
        return wfStartandcommitprocessUrl;
    }

    public void setWfStartandcommitprocessUrl(String wfStartandcommitprocessUrl) {
        this.wfStartandcommitprocessUrl = wfStartandcommitprocessUrl;
    }

    public String getEveryDayExpireBill() {
        return everyDayExpireBill;
    }

    public void setEveryDayExpireBill(String everyDayExpireBill) {
        this.everyDayExpireBill = everyDayExpireBill;
    }

    public String getGetLatelyThreeDaysBill() {
        return getLatelyThreeDaysBill;
    }

    public void setGetLatelyThreeDaysBill(String getLatelyThreeDaysBill) {
        this.getLatelyThreeDaysBill = getLatelyThreeDaysBill;
    }

    public String getInstallmentBillInfoByOrderId() {
        return installmentBillInfoByOrderId;
    }

    public void setInstallmentBillInfoByOrderId(String installmentBillInfoByOrderId) {
        this.installmentBillInfoByOrderId = installmentBillInfoByOrderId;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

}
