package com.iqb.consumer.data.layer.bean.dandelion.entity;

import java.util.List;

import javax.persistence.Table;

import jodd.util.StringUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.BaseEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.CreditInfoPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.MortgageInfoPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.PersistDesignPersonRequestPojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.SignGuarant;

@Table(name = "inst_creditinfo")
public class InstCreditInfoEntity extends BaseEntity {
    public static final int MAX_GUARANTOR_NUM = 5;
    public static final int INIT_GUARANTOR_NUM = 1;
    private static final long serialVersionUID = 1L;
    private String orderId; //
    private String designCode; // 指定任务人员编码
    private String designName; // 人员描述名称
    private Integer guarantorNum; // 担保信息个数
    private String creditType; // 信贷类型
    private Integer borrowTogether; // 是否共借人 0否 1是
    private String borrTogetherName; // 共借人姓名
    private String creditInfo; // 信贷信息
    private List<CreditInfoPojo> creditInfoX;
    private String amtAdvice; // 额度建议
    private List<SignGuarant> signGuarant; //
    private List<SignGuarant> signGuarantCopy; //
    private String mortgageInfo;// 抵押信息
    private List<MortgageInfoPojo> mortgageInfoX;// 抵押信息
    private String mortgageTotalAmt;// 质押估值总额
    private String adviceAmt;// 质押估值总额

    /** inst_orderotherinfo **/
    private String remark;// 备注
    private String projectName; // 项目名称
    /** 非表中字段，用以区分业务类型 **/
    public static final int TYPE_SAVE_BORROW_INFO = 1; // 1,保存借款信息
    public static final int TYPE_SAVE_PERSION_INFO = 2; // 2保存共借人
    public static final int TYPE_SAVE_MORTGAGE_INFO = 3; // 2保存抵押信息
    public static final int TYPE_UNDEFINE = -1; // 未定义
    private Integer type; // 1,保存借款信息，2保存共借人

    private String guarantee; // 担保公司
    private String guaranteeName;// 担保人

    public List<MortgageInfoPojo> getMortgageInfoX() {
        return mortgageInfoX;
    }

    public void setMortgageInfoX(List<MortgageInfoPojo> mortgageInfoX) {
        this.mortgageInfoX = mortgageInfoX;
        this.mortgageInfo = JSONArray.toJSONString(mortgageInfoX);
    }

    public String getMortgageInfo() {
        return mortgageInfo;
    }

    public void setMortgageInfo(String mortgageInfo) {

        this.mortgageInfo = mortgageInfo;
    }

    public String getMortgageTotalAmt() {
        return mortgageTotalAmt;
    }

    public void setMortgageTotalAmt(String mortgageTotalAmt) {
        this.mortgageTotalAmt = mortgageTotalAmt;
    }

    public String getAdviceAmt() {
        return adviceAmt;
    }

    public void setAdviceAmt(String adviceAmt) {
        this.adviceAmt = adviceAmt;
    }

    public String getSignGuarant() {
        return JSONObject.toJSONString(signGuarant);
    }

    @SuppressWarnings("unchecked")
    public void setSignGuarant(Object signGuarant) {
        if (signGuarant instanceof List) {
            this.signGuarant = (List<SignGuarant>) signGuarant;
        } else if (signGuarant instanceof String) {
            this.signGuarant = JSONObject.parseArray((String) signGuarant, SignGuarant.class);
            signGuarantCopy = this.signGuarant;
        }

    }

    public List<SignGuarant> getSignGuarantCopy() {
        return signGuarantCopy;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDesignCode() {
        return designCode;
    }

    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public Integer getGuarantorNum() {
        return guarantorNum;
    }

    public void setGuarantorNum(Integer guarantorNum) {
        this.guarantorNum = guarantorNum;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public Integer getBorrowTogether() {
        return borrowTogether;
    }

    public void setBorrowTogether(Integer borrowTogether) {
        this.borrowTogether = borrowTogether;
    }

    public String getBorrTogetherName() {
        return borrTogetherName;
    }

    public void setBorrTogetherName(String borrTogetherName) {
        this.borrTogetherName = borrTogetherName;
    }

    public String getCreditInfo() {
        return creditInfo;
    }

    public void setCreditInfo(String creditInfo) {
        if (!StringUtil.isEmpty(creditInfo)) {
            this.creditInfoX = JSONArray.parseArray(creditInfo, CreditInfoPojo.class);
        }
        this.creditInfo = creditInfo;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAmtAdvice() {
        return amtAdvice;
    }

    public void setAmtAdvice(String amtAdvice) {
        this.amtAdvice = amtAdvice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<CreditInfoPojo> getCreditInfoX() {
        return creditInfoX;
    }

    public void setCreditInfoX(List<CreditInfoPojo> creditInfoX) {
        this.creditInfoX = creditInfoX;
        this.creditInfo = JSONArray.toJSONString(creditInfoX);
    }

    public String getGuarantee() {
        return guarantee;
    }

    public void setGuarantee(String guarantee) {
        this.guarantee = guarantee;
    }

    public String getGuaranteeName() {
        return guaranteeName;
    }

    public void setGuaranteeName(String guaranteeName) {
        this.guaranteeName = guaranteeName;
    }

    public void createDesignPersion(PersistDesignPersonRequestPojo pdpr) {
        orderId = pdpr.getOrderId();
        designCode = pdpr.getId();
        designName = pdpr.getText();
        guarantorNum = INIT_GUARANTOR_NUM;
    }

    public void updateDesignPersion(PersistDesignPersonRequestPojo pdpr) {
        orderId = pdpr.getOrderId();
        designCode = pdpr.getId();
        designName = pdpr.getText();
    }

    /**
     * 
     * Description: 4.9保存信用贷类型，共借人等 请求参数 校验
     * 
     * @param
     * @return boolean
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午2:55:55
     */
    public boolean checkPersistCreditTypeRequestMessage() {
        switch (type) {
            case TYPE_SAVE_BORROW_INFO:
                return !(StringUtil.isEmpty(orderId) || creditType == null);
            case TYPE_SAVE_PERSION_INFO:
                return !(StringUtil.isEmpty(orderId) || borrowTogether == null);
            case TYPE_SAVE_MORTGAGE_INFO:
                return !(StringUtil.isEmpty(orderId));
            default:
                return false;
        }
    }

    public static void main(String[] args) {
        String c = "[{\"creditType\":\"123\",\"loanAmt\":231.12}]";
        List<CreditInfoPojo> data = JSONArray.parseArray(c, CreditInfoPojo.class);
        System.err.println(data.get(0).getLoanAmt());
        System.out.println(JSONArray.toJSONString(data));
    }

}
