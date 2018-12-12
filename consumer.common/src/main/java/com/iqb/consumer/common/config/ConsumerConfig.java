package com.iqb.consumer.common.config;

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
    /** 根据期数查询指定账单 **/
    @Value("${FINANCE.BILL.ASSIGN.URL}")
    private String assignBillUrl;
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
    /** 抵押车业务分成明细请求地址(分页) **/
    @Value("${FINANCE.BILL.MORTGAGECAR.QUERY.URL}")
    private String financeBillMortgageStatisticsUrl;

    /** 账户账单分页查询请求地址--中阁使用 **/
    @Value("${FINANCE.ALL.BILL.GETPAGELIST.URL}")
    private String financeAllBillGetPagelistUrl;

    /** 查询最近三期未还款账单地址 **/
    @Value("${finance.bill.current.url}")
    private String financeBillCurrentUrl;
    /** 查询订单下所有账单接口地址 **/
    @Value("${finance.bill.selectBills.url}")
    private String financeBillSelectBillsUrl;
    /** 根据orderId repayno获取账单应还总金额 **/
    @Value("${finance.bill.selectBillsByRepayNo.url}")
    private String selectBillsByRepayNoUrl;
    /** 重算金额URL **/
    @Value("${finance.calculateAmtUrl}")
    private String calculateAmtUrl;
    /** 根据手机号码获取绑定的银行卡 **/
    @Value("${INTO.XF.BINDBANKCARD.URL}")
    private String intoXfBindBankCardUrl;
    /** 银行卡解绑接口 **/
    @Value("${INTO.XF.UNBINDBANKCARD.URL}")
    private String intoXfUnBindBankCardUrl;
    /** #标准接口-正常还款获取还款信息接口 **/
    @Value("${INTO.XF.NORMALREPAY.BILLINFO.URL}")
    private String intoXfNormalRepayBillInfoUrl;

    /** api接口查询最近三期账单接口 **/
    @Value("${finance.bill.current.three.url}")
    private String financeBillCurrentThreeUrl;

    /** 按订单分组汇总账单信息 **/
    @Value("${FINANCE.BILL.COLLECTBYORDER.URL}")
    private String financeBillCollectByOrderUrl;
    /**************************************** 工作流相关配置 ****************************************/
    /** 工作流基础地址 **/
    @Value("${WF.REQ.ENV.BASE.URL}")
    private String wfReqEnvBaseUrl;
    /** 启动工作流 **/
    @Value("${WF.STARTANDCOMMITPROCESS.URL}")
    private String wfStartandcommitprocessUrl;

    @Value("${MANAGER.SEND2RISK.YZDS.URL}")
    private String managerSend2RiskYZDSurl; // 以租代售风控接口地址
    @Value("${MANAGER.SEND2RISK.DY.URL}")
    private String managerSend2RiskDYurl; // 抵押车风控接口地址
    @Value("${MANAGER.WFRETURN.YZDS.URL}")
    private String managerWfreturnYZDSurl;
    @Value("${idumai_riskcontrol_oldcar_notice_url}")
    private String idumai_riskcontrol_oldcar_notice_url;// 风控二手车回调通知接口
    @Value("${idumai_riskcontrol_notice_url}")
    private String idumai_riskcontrol_notice_url;// 风控新车回调通知接口
    @Value("${idumai_riskcontrol_checkorder_url}")
    private String idumai_riskcontrol_checkorder_url;// 风控核对订单接口
    @Value("${idumai_riskcontrol_pledge_notice_url}")
    private String idumai_riskcontrol_pledge_notice_url;// 抵押车风控回调通知接口

    @Value("${MANAGER.WFRETURN.PGYH.RISK.URL}")
    private String managerSend2RiskPGYHurl; // 蒲公英行风控接口地址
    @Value("${MANAGER.WFRETURN.PGYH.CALLBACK.URL}")
    private String pgyh_riskcontrol_notice_url;// 蒲公英行风控返回接口地址
    @Value("${MANAGER.WFRETURN.PGYH.URL}")
    private String managerWfreturnPGYHurl; // 蒲公英行项目信息接口地址
    /** 人工风控随机结果 环境标识 **/
    @Value("${RISK.RANDOM.ORDERFLAG.ENV}")
    private String envFlag;

    @Value("${OWNERLOAN.WFRETURN.PGYH.CALLBACK.URL}")
    private String ownerLoanWfreturnUrl; // 车主贷风控回调接口
    @Value("${OWNERLOAN.OWNEN_GPS_PER.URL}")
    private double OWNEN_GPS_PER;

    @Value("${local_riskcontrol_checkorder_url}")
    private String localRiskcontrolCheckorderUrl;// 本地风控核对订单接口
    @Value("${local_riskcontrol_getDetail_url}")
    private String local_riskcontrol_getDetail_url;// 风控涉案详情接口

    @Value("${finance.bill.current.status.url}")
    private String financeBillCurrentBillInfoUrl;// 获取账单信息
    @Value("${risk.return.callback.url}")
    private String riskReturnCallbackUrl;// 风控回调接口

    /** 鉴权URL地址 **/
    @Value("${authInfoUrl}")
    private String authInfoUrl;

    @Value("${BATCH.UPDATEMOBILE.URL}")
    private String batchUpdateMobileUrl;// 批量更新接收短信手机号url

    /****************** 车易估 **********************/
    @Value("${CHEEGU.URL}")
    private String cheeguUrl; // 车易估请求地址
    @Value("${CHEEGU.USERNAME}")
    private String cheeguUserName; // 车易估用户
    @Value("${CHEEGU.PASSWORD}")
    private String cheeguPassWord; // 车易估密码
    @Value("${CHEEGU.TOKEN}")
    private String cheeguToken; // 车易估token

    /************* 车e估违章查询接口地址 ****************/
    /** 车e估user **/
    @Value("${CHEEGU.PEPCCANCY.URL}")
    private String cheeguPeccancyUrl;
    /** 车e估user **/
    @Value("${CHEEGU.PEPCCANCY.USER}")
    private String cheeguPeccancyUser;
    /** 车e估-中阁回调类 **/
    @Value("${CHEEGU.PEPCCANCY.CALLBACK}")
    private String cheeguPeccancyCallBack;
    /** 车e估key **/
    @Value("${CHEEGU.PEPCCANCY.KEY}")
    private String cheeguPeccancyKey;

    /** 批量根据订单号还有当前期数查询账单信息 **/
    @Value("${iqb.finance.queryBillInfoByOrderIdPage}")
    private String queryBillInfoByOrderIdPage;
    /** 根据订单号获取已还款最大期数 **/
    @Value("${iqb.finance.OrderHasMaxRepayNo}")
    private String queryOrderHasMaxRepayNo;
    /** 根据订单号列表获取已还款最大期数 **/
    @Value("${iqb.finance.OrdersHasMaxRepayNo}")
    private String queryOrdersHasMaxRepayNo;

    /** 根据订单号查询首次还款时间 **/
    @Value("${iqb.finance.queryFirstLastRepayDateByOrderId}")
    private String queryFirstLastRepayDateByOrderId;

    /** 根据订单号获、预计放款时间取当前期数以及剩余本金 **/
    @Value("${iqb.finance.queryRemainPrincipalByOrderId}")
    private String queryAssetRemainPrincipalByOrderId;

    public String getLocal_riskcontrol_getDetail_url() {
        return local_riskcontrol_getDetail_url;
    }

    public void setLocal_riskcontrol_getDetail_url(String local_riskcontrol_getDetail_url) {
        this.local_riskcontrol_getDetail_url = local_riskcontrol_getDetail_url;
    }

    public String getLocalRiskcontrolCheckorderUrl() {
        return localRiskcontrolCheckorderUrl;
    }

    public void setLocalRiskcontrolCheckorderUrl(String localRiskcontrolCheckorderUrl) {
        this.localRiskcontrolCheckorderUrl = localRiskcontrolCheckorderUrl;
    }

    public String getRiskReturnCallbackUrl() {
        return riskReturnCallbackUrl;
    }

    public void setRiskReturnCallbackUrl(String riskReturnCallbackUrl) {
        this.riskReturnCallbackUrl = riskReturnCallbackUrl;
    }

    public String getAssignBillUrl() {
        return assignBillUrl;
    }

    public void setAssignBillUrl(String assignBillUrl) {
        this.assignBillUrl = assignBillUrl;
    }

    public String getCalculateAmtUrl() {
        return calculateAmtUrl;
    }

    public void setCalculateAmtUrl(String calculateAmtUrl) {
        this.calculateAmtUrl = calculateAmtUrl;
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

    /**
     * 抵押车业务分成-查询账单信息地址
     * 
     * @return the financeBillMortgageStatisticsUrl
     */
    public String getFinanceBillMortgageStatisticsUrl() {
        return financeBillMortgageStatisticsUrl;
    }

    /**
     * @param financeBillMortgageStatisticsUrl the financeBillMortgageStatisticsUrl to set
     */
    public void setFinanceBillMortgageStatisticsUrl(String financeBillMortgageStatisticsUrl) {
        this.financeBillMortgageStatisticsUrl = financeBillMortgageStatisticsUrl;
    }

    /**
     * @return the financeAllBillGetPagelistUrl
     */
    public String getFinanceAllBillGetPagelistUrl() {
        return financeAllBillGetPagelistUrl;
    }

    /**
     * @param financeAllBillGetPagelistUrl the financeAllBillGetPagelistUrl to set
     */
    public void setFinanceAllBillGetPagelistUrl(String financeAllBillGetPagelistUrl) {
        this.financeAllBillGetPagelistUrl = financeAllBillGetPagelistUrl;
    }

    /**
     * @return the financeBillCurrentUrl
     */
    public String getFinanceBillCurrentUrl() {
        return financeBillCurrentUrl;
    }

    /**
     * @param financeBillCurrentUrl the financeBillCurrentUrl to set
     */
    public void setFinanceBillCurrentUrl(String financeBillCurrentUrl) {
        this.financeBillCurrentUrl = financeBillCurrentUrl;
    }

    /**
     * @return the financeBillSelectBillsUrl
     */
    public String getFinanceBillSelectBillsUrl() {
        return financeBillSelectBillsUrl;
    }

    /**
     * @param financeBillSelectBillsUrl the financeBillSelectBillsUrl to set
     */
    public void setFinanceBillSelectBillsUrl(String financeBillSelectBillsUrl) {
        this.financeBillSelectBillsUrl = financeBillSelectBillsUrl;
    }

    /**
     * @return the selectBillsByRepayNoUrl
     */
    public String getSelectBillsByRepayNoUrl() {
        return selectBillsByRepayNoUrl;
    }

    /**
     * @param selectBillsByRepayNoUrl the selectBillsByRepayNoUrl to set
     */
    public void setSelectBillsByRepayNoUrl(String selectBillsByRepayNoUrl) {
        this.selectBillsByRepayNoUrl = selectBillsByRepayNoUrl;
    }

    /**
     * @return the intoXfBindBankCardUrl
     */
    public String getIntoXfBindBankCardUrl() {
        return intoXfBindBankCardUrl;
    }

    /**
     * @param intoXfBindBankCardUrl the intoXfBindBankCardUrl to set
     */
    public void setIntoXfBindBankCardUrl(String intoXfBindBankCardUrl) {
        this.intoXfBindBankCardUrl = intoXfBindBankCardUrl;
    }

    /**
     * @return the intoXfUnBindBankCardUrl
     */
    public String getIntoXfUnBindBankCardUrl() {
        return intoXfUnBindBankCardUrl;
    }

    /**
     * @param intoXfUnBindBankCardUrl the intoXfUnBindBankCardUrl to set
     */
    public void setIntoXfUnBindBankCardUrl(String intoXfUnBindBankCardUrl) {
        this.intoXfUnBindBankCardUrl = intoXfUnBindBankCardUrl;
    }

    /**
     * @return the intoXfNormalRepayBillInfoUrl
     */
    public String getIntoXfNormalRepayBillInfoUrl() {
        return intoXfNormalRepayBillInfoUrl;
    }

    /**
     * @param intoXfNormalRepayBillInfoUrl the intoXfNormalRepayBillInfoUrl to set
     */
    public void setIntoXfNormalRepayBillInfoUrl(String intoXfNormalRepayBillInfoUrl) {
        this.intoXfNormalRepayBillInfoUrl = intoXfNormalRepayBillInfoUrl;
    }

    public String getFinanceBillCurrentThreeUrl() {
        return financeBillCurrentThreeUrl;
    }

    public void setFinanceBillCurrentThreeUrl(String financeBillCurrentThreeUrl) {
        this.financeBillCurrentThreeUrl = financeBillCurrentThreeUrl;
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

    public String getManagerWfreturnPGYHurl() {
        return managerWfreturnPGYHurl;
    }

    public void setManagerWfreturnPGYHurl(String managerWfreturnPGYHurl) {
        this.managerWfreturnPGYHurl = managerWfreturnPGYHurl;
    }

    public String getManagerWfreturnYZDSurl() {
        return managerWfreturnYZDSurl;
    }

    public void setManagerWfreturnYZDSurl(String managerWfreturnYZDSurl) {
        this.managerWfreturnYZDSurl = managerWfreturnYZDSurl;
    }

    public String getIdumai_riskcontrol_oldcar_notice_url() {
        return idumai_riskcontrol_oldcar_notice_url;
    }

    public void setIdumai_riskcontrol_oldcar_notice_url(String idumai_riskcontrol_oldcar_notice_url) {
        this.idumai_riskcontrol_oldcar_notice_url = idumai_riskcontrol_oldcar_notice_url;
    }

    public String getIdumai_riskcontrol_notice_url() {
        return idumai_riskcontrol_notice_url;
    }

    public void setIdumai_riskcontrol_notice_url(String idumai_riskcontrol_notice_url) {
        this.idumai_riskcontrol_notice_url = idumai_riskcontrol_notice_url;
    }

    public String getIdumai_riskcontrol_checkorder_url() {
        return idumai_riskcontrol_checkorder_url;
    }

    public void setIdumai_riskcontrol_checkorder_url(String idumai_riskcontrol_checkorder_url) {
        this.idumai_riskcontrol_checkorder_url = idumai_riskcontrol_checkorder_url;
    }

    public String getEnvFlag() {
        return envFlag;
    }

    public void setEnvFlag(String envFlag) {
        this.envFlag = envFlag;
    }

    public String getIdumai_riskcontrol_pledge_notice_url() {
        return idumai_riskcontrol_pledge_notice_url;
    }

    public void setIdumai_riskcontrol_pledge_notice_url(String idumai_riskcontrol_pledge_notice_url) {
        this.idumai_riskcontrol_pledge_notice_url = idumai_riskcontrol_pledge_notice_url;
    }

    public String getOwnerLoanWfreturnUrl() {
        return ownerLoanWfreturnUrl;
    }

    public void setOwnerLoanWfreturnUrl(String ownerLoanWfreturnUrl) {
        this.ownerLoanWfreturnUrl = ownerLoanWfreturnUrl;
    }

    public double getOWNEN_GPS_PER() {
        return OWNEN_GPS_PER;
    }

    public void setOWNEN_GPS_PER(double oWNEN_GPS_PER) {
        OWNEN_GPS_PER = oWNEN_GPS_PER;
    }

    public String getFinanceBillCurrentBillInfoUrl() {
        return financeBillCurrentBillInfoUrl;
    }

    public void setFinanceBillCurrentBillInfoUrl(String financeBillCurrentBillInfoUrl) {
        this.financeBillCurrentBillInfoUrl = financeBillCurrentBillInfoUrl;
    }

    public String getAuthInfoUrl() {
        return authInfoUrl;
    }

    public void setAuthInfoUrl(String authInfoUrl) {
        this.authInfoUrl = authInfoUrl;
    }

    public String getCheeguUrl() {
        return cheeguUrl;
    }

    public void setCheeguUrl(String cheeguUrl) {
        this.cheeguUrl = cheeguUrl;
    }

    public String getCheeguUserName() {
        return cheeguUserName;
    }

    public void setCheeguUserName(String cheeguUserName) {
        this.cheeguUserName = cheeguUserName;
    }

    public String getCheeguPassWord() {
        return cheeguPassWord;
    }

    public void setCheeguPassWord(String cheeguPassWord) {
        this.cheeguPassWord = cheeguPassWord;
    }

    public String getCheeguToken() {
        return cheeguToken;
    }

    public void setCheeguToken(String cheeguToken) {
        this.cheeguToken = cheeguToken;
    }

    public String getBatchUpdateMobileUrl() {
        return batchUpdateMobileUrl;
    }

    public void setBatchUpdateMobileUrl(String batchUpdateMobileUrl) {
        this.batchUpdateMobileUrl = batchUpdateMobileUrl;
    }

    public String getCheeguPeccancyUrl() {
        return cheeguPeccancyUrl;
    }

    public void setCheeguPeccancyUrl(String cheeguPeccancyUrl) {
        this.cheeguPeccancyUrl = cheeguPeccancyUrl;
    }

    public String getCheeguPeccancyUser() {
        return cheeguPeccancyUser;
    }

    public void setCheeguPeccancyUser(String cheeguPeccancyUser) {
        this.cheeguPeccancyUser = cheeguPeccancyUser;
    }

    public String getCheeguPeccancyKey() {
        return cheeguPeccancyKey;
    }

    public void setCheeguPeccancyKey(String cheeguPeccancyKey) {
        this.cheeguPeccancyKey = cheeguPeccancyKey;
    }

    public String getQueryBillInfoByOrderIdPage() {
        return queryBillInfoByOrderIdPage;
    }

    public void setQueryBillInfoByOrderIdPage(String queryBillInfoByOrderIdPage) {
        this.queryBillInfoByOrderIdPage = queryBillInfoByOrderIdPage;
    }

    public String getQueryOrderHasMaxRepayNo() {
        return queryOrderHasMaxRepayNo;
    }

    public void setQueryOrderHasMaxRepayNo(String queryOrderHasMaxRepayNo) {
        this.queryOrderHasMaxRepayNo = queryOrderHasMaxRepayNo;
    }

    public String getQueryOrdersHasMaxRepayNo() {
        return queryOrdersHasMaxRepayNo;
    }

    public void setQueryOrdersHasMaxRepayNo(String queryOrdersHasMaxRepayNo) {
        this.queryOrdersHasMaxRepayNo = queryOrdersHasMaxRepayNo;
    }

    public String getCheeguPeccancyCallBack() {
        return cheeguPeccancyCallBack;
    }

    public void setCheeguPeccancyCallBack(String cheeguPeccancyCallBack) {
        this.cheeguPeccancyCallBack = cheeguPeccancyCallBack;
    }

    public String getFinanceBillCollectByOrderUrl() {
        return financeBillCollectByOrderUrl;
    }

    public void setFinanceBillCollectByOrderUrl(String financeBillCollectByOrderUrl) {
        this.financeBillCollectByOrderUrl = financeBillCollectByOrderUrl;
    }

    public String getQueryFirstLastRepayDateByOrderId() {
        return queryFirstLastRepayDateByOrderId;
    }

    public void setQueryFirstLastRepayDateByOrderId(String queryFirstLastRepayDateByOrderId) {
        this.queryFirstLastRepayDateByOrderId = queryFirstLastRepayDateByOrderId;
    }

    public String getQueryAssetRemainPrincipalByOrderId() {
        return queryAssetRemainPrincipalByOrderId;
    }

    public void setQueryAssetRemainPrincipalByOrderId(String queryAssetRemainPrincipalByOrderId) {
        this.queryAssetRemainPrincipalByOrderId = queryAssetRemainPrincipalByOrderId;
    }

}
