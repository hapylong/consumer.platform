package com.iqb.consumer.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * Description:
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月8日    adam       1.0        1.0 Version 
 * </pre>
 */
@Configuration
public class UrlConfig {

    @Value("${get.repay.params.url}")
    private String repayParamsUrl;

    @Value("${COMMON.PRIVATEKEY}")
    private String commonPrivateKey;

    @Value("${COMMON.PUBLICKEY}")
    private String commonPublicKey;

    @Value("${iqb.finance.sublet.getSubletInfo}")
    private String subletInfoUrl;
    // 查询是否有逾期标识
    @Value("${iqb.finance.getOverdueFlag}")
    private String getOverdueFlag;
    // 咨询范IP验证
    @Value("${iqb.getZxfIp}")
    private String getZxfIp;

    @Value("${iqb.finance.planreseturl}")
    private String planResetUrl;

    @Value("${iqb.finance.planstopurl}")
    private String planStopUrl;

    @Value("${house.chatF.create.bill.url}")
    private String houseChatFCreateBill;

    @Value("${analysis.url}")
    private String analysisUrl;

    @Value("${newanalysis.url}")
    private String newAnalysisUrl;

    @Value("${totalnewanalysis.url}")
    private String totalNewAnalysisUrl;

    @Value("${noinsidetotalnewanalysis.url}")
    private String noinsidetotalNewAnalysisUrl;

    /** 提前结清流程key **/
    @Value("${wfConfig.getProcDefKey}")
    private String procDefKey;
    /** 提前结清流程toknUser **/
    @Value("${wfConfig.getTokenUser}")
    private String tokenUser;
    /** 提前结清流程tokenPass **/
    @Value("${wfConfig.getTokenPass}")
    private String tokenPass;
    /** 提前结清流程taskRole **/
    @Value("${wfConfig.getTaskRole}")
    private String taskRole;
    /** 提前结清流程地址 **/
    @Value("${wfConfig.getStartWfurl}")
    private String startWfurl;
    /** 违约结算流程key **/
    @Value("${wfConfig.getForfeiDefKey}")
    private String forfeiDefKey;
    /** 已逾期且逾期天数小于等于5天的账单 **/
    @Value("${iqb.finance.queryBillLastDateThree}")
    private String queryBillLastDateThree;
    /** 车300风控地址 **/
    @Value("${car.Three.Risk.Url}")
    private String carThreeRiskUrl;
    /** 车300风控token **/
    @Value("${car.Three.Risk.Token}")
    private String carThreeRiskToken;

    public String getQueryBillLastDateThree() {
        return queryBillLastDateThree;
    }

    public void setQueryBillLastDateThree(String queryBillLastDateThree) {
        this.queryBillLastDateThree = queryBillLastDateThree;
    }

    public String getNoinsidetotalNewAnalysisUrl() {
        return noinsidetotalNewAnalysisUrl;
    }

    public void setNoinsidetotalNewAnalysisUrl(String noinsidetotalNewAnalysisUrl) {
        this.noinsidetotalNewAnalysisUrl = noinsidetotalNewAnalysisUrl;
    }

    public String getTotalNewAnalysisUrl() {
        return totalNewAnalysisUrl;
    }

    public void setTotalNewAnalysisUrl(String totalNewAnalysisUrl) {
        this.totalNewAnalysisUrl = totalNewAnalysisUrl;
    }

    public String getNewAnalysisUrl() {
        return newAnalysisUrl;
    }

    public void setNewAnalysisUrl(String newAnalysisUrl) {
        this.newAnalysisUrl = newAnalysisUrl;
    }

    @Value("${finish.bill}")
    private String finishBillUrl;

    public String getGetZxfIp() {
        return getZxfIp;
    }

    public void setGetZxfIp(String getZxfIp) {
        this.getZxfIp = getZxfIp;
    }

    public String getGetOverdueFlag() {
        return getOverdueFlag;
    }

    public void setGetOverdueFlag(String getOverdueFlag) {
        this.getOverdueFlag = getOverdueFlag;
    }

    public String getRepayParamsUrl() {
        return repayParamsUrl;
    }

    public void setRepayParamsUrl(String repayParamsUrl) {
        this.repayParamsUrl = repayParamsUrl;
    }

    public String getCommonPrivateKey() {
        return commonPrivateKey;
    }

    public void setCommonPrivateKey(String commonPrivateKey) {
        this.commonPrivateKey = commonPrivateKey;
    }

    public String getCommonPublicKey() {
        return commonPublicKey;
    }

    public void setCommonPublicKey(String commonPublicKey) {
        this.commonPublicKey = commonPublicKey;
    }

    public String getSubletInfoUrl() {
        return subletInfoUrl;
    }

    public void setSubletInfoUrl(String subletInfoUrl) {
        this.subletInfoUrl = subletInfoUrl;
    }

    public String getPlanResetUrl() {
        return planResetUrl;
    }

    public void setPlanResetUrl(String planResetUrl) {
        this.planResetUrl = planResetUrl;
    }

    public String getPlanStopUrl() {
        return planStopUrl;
    }

    public void setPlanStopUrl(String planStopUrl) {
        this.planStopUrl = planStopUrl;
    }

    public String getHouseChatFCreateBill() {
        return houseChatFCreateBill;
    }

    public void setHouseChatFCreateBill(String houseChatFCreateBill) {
        this.houseChatFCreateBill = houseChatFCreateBill;
    }

    public String getAnalysisUrl() {
        return analysisUrl;
    }

    public void setAnalysisUrl(String analysisUrl) {
        this.analysisUrl = analysisUrl;
    }

    public String getFinishBillUrl() {
        return finishBillUrl;
    }

    public void setFinishBillUrl(String finishBillUrl) {
        this.finishBillUrl = finishBillUrl;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getTokenUser() {
        return tokenUser;
    }

    public void setTokenUser(String tokenUser) {
        this.tokenUser = tokenUser;
    }

    public String getTokenPass() {
        return tokenPass;
    }

    public void setTokenPass(String tokenPass) {
        this.tokenPass = tokenPass;
    }

    public String getTaskRole() {
        return taskRole;
    }

    public void setTaskRole(String taskRole) {
        this.taskRole = taskRole;
    }

    public String getStartWfurl() {
        return startWfurl;
    }

    public void setStartWfurl(String startWfurl) {
        this.startWfurl = startWfurl;
    }

    public String getForfeiDefKey() {
        return forfeiDefKey;
    }

    public void setForfeiDefKey(String forfeiDefKey) {
        this.forfeiDefKey = forfeiDefKey;
    }

    public String getCarThreeRiskUrl() {
        return carThreeRiskUrl;
    }

    public void setCarThreeRiskUrl(String carThreeRiskUrl) {
        this.carThreeRiskUrl = carThreeRiskUrl;
    }

    public String getCarThreeRiskToken() {
        return carThreeRiskToken;
    }

    public void setCarThreeRiskToken(String carThreeRiskToken) {
        this.carThreeRiskToken = carThreeRiskToken;
    }
}
