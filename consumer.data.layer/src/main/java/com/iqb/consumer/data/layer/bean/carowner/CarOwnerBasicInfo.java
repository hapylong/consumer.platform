package com.iqb.consumer.data.layer.bean.carowner;

import java.math.BigDecimal;
import java.util.Date;

import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.riskinfo.RiskInfoBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;

import jodd.util.StringUtil;

public class CarOwnerBasicInfo {

    private String orderId;// 订单号
    private String merchantNo; // cdhtc
    private String realName; // 姓名
    private String regId; // 手机号
    private String idNo; // 证件号码
    private BigDecimal applyAmt; // 申请金额
    private Integer orderItems; // 期限(月)
    private String address; // 居住地
    private String job; // 从事工作
    private String bankName; //
    private String bankNo; // 账号
    private String userName1; // 姓名1
    private String mobile1; // 电话1
    private String relation1; // 关系1
    private String userName2; // 姓名2
    private String mobile2; // 电话2
    private String relation2; // 关系2
    private String userName3; // 姓名3
    private String mobile3; // 电话3
    private String relation3; // 关系3
    private String userName4; // 姓名4
    private String mobile4; // 电话4
    private String relation4; // 关系4
    private String plate; // 车牌号
    private Integer carAge; // 车龄
    private String orderName; // 品牌-型号

    private UserBean ub;
    private InstOrderInfoEntity ioie;
    private BankCardBean bcb;
    private RiskInfoBean<CarOwnerCheckInfo> rib;
    private AuthorityCardBean acb;

    private static final String EMPTY = "";

    public UserBean getUb() {
        if (ub == null) {
            ub = new UserBean();
            ub.setRegId(regId);
            ub.setIdNo(idNo);
            ub.setRealName(realName);
        }
        return ub;
    }

    public InstOrderInfoEntity getIoie() {
        if (ioie == null) {
            ioie = new InstOrderInfoEntity();
            ioie.setRegId(regId);
            ioie.setApplyAmt(applyAmt);
            ioie.setOrderName(orderName);
            ioie.setBizType("2400"); // 车主贷业务类型
            ioie.setMerchantNo(merchantNo);
            ioie.setOrderName(orderName);
            ioie.setOrderItems(orderItems);
            ioie.setCreateTime(new Date());
            ioie.setRiskStatus(2);
            ioie.setStatus(1);
            ioie.setVersion("1");
            ioie.setWfStatus(10);
        }
        return ioie;
    }

    public BankCardBean getBcb() {
        if (bcb == null) {
            bcb = new BankCardBean();
            bcb.setBankCardNo(bankNo);
            bcb.setBankMobile(regId);
            bcb.setBankName(bankName);
            bcb.setCreateTime(new Date());
            bcb.setVersion(1);
        }
        return bcb;
    }

    public AuthorityCardBean getAcb() {
        if (acb == null) {
            acb = new AuthorityCardBean();
            acb.setPlate(plate);
            acb.setCarAge(String.valueOf(carAge));
            if (!StringUtil.isEmpty(orderName)) {
                acb.setPlateType(orderName.split("-")[1]);
            }
            acb.setCreateTime(new Date());
            acb.setVersion(1);
        }
        return acb;
    }

    public RiskInfoBean<CarOwnerCheckInfo> getRib(String orderId, String merchantNo) {
        if (rib == null) {
            rib = new RiskInfoBean<>();
            rib.setRegId(regId);
            rib.setRiskType(4);
            CarOwnerCheckInfo checkInfo = new CarOwnerCheckInfo();
            checkInfo.setAddress(address);
            checkInfo.setBankCard(bankNo);
            checkInfo.setColleagues1(EMPTY);
            checkInfo.setColleagues2(EMPTY);
            checkInfo.setCompanyAddress(EMPTY);
            checkInfo.setCompanyName(job);
            checkInfo.setCompanyPhone(EMPTY);
            checkInfo.setCreditCode(EMPTY);
            checkInfo.setCreditNo(EMPTY);
            checkInfo.setCreditPasswd(EMPTY);
            checkInfo.setIdCard(idNo);
            checkInfo.setMerchantId(merchantNo);
            checkInfo.setOrderId(orderId);
            checkInfo.setPhone1(mobile1);
            checkInfo.setPhone2(mobile2);
            checkInfo.setPhone3(mobile3);
            checkInfo.setPhone4(mobile4);
            checkInfo.setProType("车主贷");
            checkInfo.setRealName(realName);
            checkInfo.setRegId(regId);
            checkInfo.setRelation1(relation1);
            checkInfo.setRelation2(relation2);
            checkInfo.setRelation3(relation3);
            checkInfo.setRelation4(relation4);
            checkInfo.setRelaName1(userName1);
            checkInfo.setRelaName2(userName2);
            checkInfo.setRelaName3(userName3);
            checkInfo.setRelaName4(userName4);
            checkInfo.setSex1(EMPTY);
            checkInfo.setSex2(EMPTY);
            checkInfo.setSex3(EMPTY);
            checkInfo.setTel1(EMPTY);
            checkInfo.setTel2(EMPTY);
            rib.setCheckInfoEntity(checkInfo);
        }
        return rib;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public BigDecimal getApplyAmt() {
        return applyAmt;
    }

    public void setApplyAmt(BigDecimal applyAmt) {
        this.applyAmt = applyAmt;
    }

    public Integer getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Integer orderItems) {
        this.orderItems = orderItems;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getRelation1() {
        return relation1;
    }

    public void setRelation1(String relation1) {
        this.relation1 = relation1;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getRelation2() {
        return relation2;
    }

    public void setRelation2(String relation2) {
        this.relation2 = relation2;
    }

    public String getUserName3() {
        return userName3;
    }

    public void setUserName3(String userName3) {
        this.userName3 = userName3;
    }

    public String getMobile3() {
        return mobile3;
    }

    public void setMobile3(String mobile3) {
        this.mobile3 = mobile3;
    }

    public String getRelation3() {
        return relation3;
    }

    public void setRelation3(String relation3) {
        this.relation3 = relation3;
    }

    public String getUserName4() {
        return userName4;
    }

    public void setUserName4(String userName4) {
        this.userName4 = userName4;
    }

    public String getMobile4() {
        return mobile4;
    }

    public void setMobile4(String mobile4) {
        this.mobile4 = mobile4;
    }

    public String getRelation4() {
        return relation4;
    }

    public void setRelation4(String relation4) {
        this.relation4 = relation4;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Integer getCarAge() {
        return carAge;
    }

    public void setCarAge(Integer carAge) {
        this.carAge = carAge;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

}
