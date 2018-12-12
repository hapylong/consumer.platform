package com.iqb.consumer.asset.allocation.assetallocine.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.github.pagehelper.StringUtil;

public class RARRequestMessage {

    /** jys_breakorderinfo **/
    private Integer projectId; // id
    private String projectCode; // bOrderId
    private BigDecimal applyamt; // bOrderAmt
    private String exchRecordNbr; // recordNum
    private Integer packNum; // breakPackNum

    /** jys_assetallocation **/
    private String projectName; // proName
    private String registerAgency; // bakOrgan
    private String transConditions; // tranCondition
    private String proOverview; // proDetail
    private String guaranteeWay; // safeWay
    private String listingAddr; // url
    private String projectChanel; // channel

    /** jys_packinfo **/
    private String guarantee; // guaranteeInstitution
    private String guaranteeName; // guaranteeName
    private String raiseObj; // raiseInstitutions

    private Integer checkTime; // now

    /** jys_orderinfo **/
    private String projectType; // bizType
    private Integer deadline; // expireDate
    private Date deadlinePre; // 接 expireDate 转 int 给deadline
    private BigDecimal raiseTotalAmount;// orderAmt
    private String packageId; // orderId

    /** jys_user **/
    private String loanName; // realName (user orderInfo userId 关联)
    private String loanIdcard; // idNo

    private String cardNum;// 线上借款人银行卡号
    private String bankName;// 线上借款人银行卡开户行名称
    private String phone;// 线上借款人手机号
    private String userName;// 实际借款人姓名
    private String idCard;// 实际借款人身份证号
    private String channal;
    private String jysOrderId;
    private String bankNo;

    private String guaCreditCode;// 担保公司社会信用代码
    private String guaranteeIdCard;// 担保公司法定代表人身份证号
    private String guaCardNum;// 担保公司卡号
    private String guaBankName;// 担保公司卡开户行
    private String guaBankCity;// 担保公司卡开户地区

    private String vehicleType;// 车型
    private String vehiclePlate;// 车牌号
    private String vehicleIdNo;// 车架号

    private int loanType;// 借款类型 1.个人借款；2.机构借款
    private int trulyLoanType;// 实际借款类型 1.个人借款 2.机构借款
    private int creditType;// 授信类型1、代表质押2、代表非质押
    private String houseAddr;// 房屋地址
    private int houseType;// 房屋类型
    private BigDecimal houseArea;// 房屋面积
    private BigDecimal houseValuation;// 房屋评估价格
    private int housePledged;// 房屋是否有一抵0：无1：有
    private BigDecimal housePledgedAmount;// 一抵金额

    public String getChannal() {
        return channal;
    }

    public void setChannal(String channal) {
        this.channal = channal;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public BigDecimal getApplyamt() {
        return applyamt.setScale(2);
    }

    public void setApplyamt(BigDecimal applyamt) {
        this.applyamt = applyamt;
    }

    public String getExchRecordNbr() {
        return exchRecordNbr;
    }

    public void setExchRecordNbr(String exchRecordNbr) {
        this.exchRecordNbr = exchRecordNbr;
    }

    public String getRaiseObj() {
        return raiseObj;
    }

    public void setRaiseObj(String raiseObj) {
        this.raiseObj = raiseObj;
    }

    public Integer getPackNum() {
        return packNum;
    }

    public void setPackNum(Integer packNum) {
        this.packNum = packNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRegisterAgency() {
        return registerAgency;
    }

    public void setRegisterAgency(String registerAgency) {
        this.registerAgency = registerAgency;
    }

    public String getTransConditions() {
        return transConditions;
    }

    public void setTransConditions(String transConditions) {
        this.transConditions = transConditions;
    }

    public String getProOverview() {
        return proOverview;
    }

    public void setProOverview(String proOverview) {
        this.proOverview = proOverview;
    }

    public String getGuaranteeWay() {
        return guaranteeWay;
    }

    public void setGuaranteeWay(String guaranteeWay) {
        this.guaranteeWay = guaranteeWay;
    }

    public String getListingAddr() {
        return listingAddr;
    }

    public void setListingAddr(String listingAddr) {
        this.listingAddr = listingAddr;
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

    public Integer getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Integer checkTime) {
        this.checkTime = checkTime;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectChanel() {
        return projectChanel;
    }

    public void setProjectChanel(String projectChanel) {
        this.projectChanel = projectChanel;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public BigDecimal getRaiseTotalAmount() {
        return raiseTotalAmount.setScale(2);
    }

    public void setRaiseTotalAmount(BigDecimal raiseTotalAmount) {
        this.raiseTotalAmount = raiseTotalAmount;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanIdcard() {
        return loanIdcard;
    }

    public void setLoanIdcard(String loanIdcard) {
        this.loanIdcard = loanIdcard;
    }

    public Date getDeadlinePre() {
        return deadlinePre;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setDeadlinePre(Date deadlinePre) {
        this.deadline = (int) (deadlinePre.getTime() / 1000);
        this.deadlinePre = deadlinePre;
    }

    public String getJysOrderId() {
        return jysOrderId;
    }

    public void setJysOrderId(String jysOrderId) {
        this.jysOrderId = jysOrderId;
    }

    public String getGuaranteeIdCard() {
        return guaranteeIdCard;
    }

    public void setGuaranteeIdCard(String guaranteeIdCard) {
        this.guaranteeIdCard = guaranteeIdCard;
    }

    public String getGuaCardNum() {
        return guaCardNum;
    }

    public void setGuaCardNum(String guaCardNum) {
        this.guaCardNum = guaCardNum;
    }

    public String getGuaBankName() {
        return guaBankName;
    }

    public void setGuaBankName(String guaBankName) {
        this.guaBankName = guaBankName;
    }

    public String getGuaBankCity() {
        return guaBankCity;
    }

    public void setGuaBankCity(String guaBankCity) {
        this.guaBankCity = guaBankCity;
    }

    public String getGuaCreditCode() {
        return guaCreditCode;
    }

    public void setGuaCreditCode(String guaCreditCode) {
        this.guaCreditCode = guaCreditCode;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getVehicleIdNo() {
        return vehicleIdNo;
    }

    public void setVehicleIdNo(String vehicleIdNo) {
        this.vehicleIdNo = vehicleIdNo;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public int getLoanType() {
        return loanType;
    }

    public void setLoanType(int loanType) {
        this.loanType = loanType;
    }

    public int getTrulyLoanType() {
        return trulyLoanType;
    }

    public void setTrulyLoanType(int trulyLoanType) {
        this.trulyLoanType = trulyLoanType;
    }

    public int getCreditType() {
        return creditType;
    }

    public void setCreditType(int creditType) {
        this.creditType = creditType;
    }

    public String getHouseAddr() {
        return houseAddr;
    }

    public void setHouseAddr(String houseAddr) {
        this.houseAddr = houseAddr;
    }

    public int getHouseType() {
        return houseType;
    }

    public void setHouseType(int houseType) {
        this.houseType = houseType;
    }

    public BigDecimal getHouseArea() {
        return houseArea;
    }

    public void setHouseArea(BigDecimal houseArea) {
        this.houseArea = houseArea;
    }

    public BigDecimal getHouseValuation() {
        return houseValuation;
    }

    public void setHouseValuation(BigDecimal houseValuation) {
        this.houseValuation = houseValuation;
    }

    public int getHousePledged() {
        return housePledged;
    }

    public void setHousePledged(int housePledged) {
        this.housePledged = housePledged;
    }

    public BigDecimal getHousePledgedAmount() {
        return housePledgedAmount;
    }

    public void setHousePledgedAmount(BigDecimal housePledgedAmount) {
        this.housePledgedAmount = housePledgedAmount;
    }

    private static Set<String> typePool = new HashSet<>();
    static {
        typePool.add("2001");
        typePool.add("2002");
        typePool.add("2100");
        typePool.add("2200");
        typePool.add("1100");
        typePool.add("1000");
        typePool.add("1200");
    }

    private static final String DEFAULT_CHANNAL = "1001"; // 默认
                                                          // ：大连交易所

    public boolean checkEntity() {
        if (StringUtil.isEmpty(projectCode) ||
                StringUtil.isEmpty(exchRecordNbr) ||
                StringUtil.isEmpty(raiseObj) ||
                StringUtil.isEmpty(projectName) ||
                StringUtil.isEmpty(registerAgency) ||
                StringUtil.isEmpty(transConditions) ||
                StringUtil.isEmpty(proOverview) ||
                StringUtil.isEmpty(guaranteeWay) ||
                StringUtil.isEmpty(listingAddr) ||
                StringUtil.isEmpty(guarantee) ||
                StringUtil.isEmpty(guaranteeName) ||
                StringUtil.isEmpty(projectType) ||
                StringUtil.isEmpty(packageId) ||
                StringUtil.isEmpty(loanName) ||
                StringUtil.isEmpty(loanIdcard) ||
                projectId == null ||
                applyamt == null ||
                packNum == null ||
                deadline == null ||
                raiseTotalAmount == null) {
            return false;
        }
        /*
         * if (!typePool.contains(projectType)) { return false; }
         */
        projectChanel = DEFAULT_CHANNAL;
        checkTime = (int) (System.currentTimeMillis() / 1000);
        return true;
    }

    @Override
    public String toString() {
        return "RARRequestMessage [projectId=" + projectId + ", projectCode="
                + projectCode + ", applyamt=" + applyamt + ", exchRecordNbr="
                + exchRecordNbr + ", raiseObj=" + raiseObj + ", packNum="
                + packNum + ", projectName=" + projectName
                + ", registerAgency=" + registerAgency + ", transConditions="
                + transConditions + ", proOverview=" + proOverview
                + ", guaranteeWay=" + guaranteeWay + ", listingAddr="
                + listingAddr + ", projectChanel=" + projectChanel
                + ", guarantee=" + guarantee + ", guaranteeName="
                + guaranteeName + ", checkTime=" + checkTime + ", projectType="
                + projectType + ", deadline=" + deadline + ", deadlinePre="
                + deadlinePre + ", raiseTotalAmount=" + raiseTotalAmount
                + ", packageId=" + packageId + ", loanName=" + loanName
                + ", loanIdcard=" + loanIdcard + "]";
    }

    private static final String QUEUE_ASSET_ALLOCATION = "asset_allocation";
    private static final String ASSET_ALLOCATION_CHANNAL = "CHANNAL_RAR";
    private static final String ASSET_ALLOCATION_CHANNAL_JL = "CHANNAL_JL";

    public Map<String, Object> createMapString() {
        checkTime = (int) (System.currentTimeMillis() / 1000);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("projectId", projectId);// 项目id
        response.put("projectCode", projectCode);// 项目编号
        response.put("projectName", projectName);// 项目名称
        response.put("userName", userName);// 线上借款人姓名
        response.put("idCard", idCard);// 线上借款人身份证

        response.put("guarantee", guarantee);// 担保公司名称
        response.put("guaranteeName", guaranteeName);// 担保公司法定代表人
        response.put("checkTime", checkTime);// 审核通过时间
        response.put("applyamt", applyamt); // 借款金额
        response.put("projectType", projectType);// 项目类型

        response.put("projectChanel", projectChanel);// 项目渠道
        response.put("deadline", deadline);// 标的结束时间
        response.put("loanName", loanName);// 实际借款人姓名
        response.put("loanIdcard", loanIdcard);// 实际借款人身份
        response.put("cardNum", cardNum);// 线上借款人银行卡号

        response.put("bankName", bankName);// 线上借款人银行开户行名称
        response.put("bankNo", bankNo);// 线上借款人银行编码
        response.put("phone", phone);// 线上借款人手机号
        response.put("registerAgency", registerAgency);// 登记备案机构
        response.put("exchRecordNbr", exchRecordNbr);// 交易所备案编号

        response.put("raiseObj", raiseObj);// 募集对象（借款方）
        response.put("proOverview", proOverview);// 项目概况
        response.put("transConditions", transConditions); // 转让条件
        response.put("guaranteeWay", guaranteeWay); // 保障方式
        response.put("listingAddr", listingAddr);// 挂牌地址

        response.put("packNum", packNum);
        response.put("packageId", packageId);
        response.put("raiseTotalAmount", raiseTotalAmount);// 募集总金额
        response.put("msgType", QUEUE_ASSET_ALLOCATION);

        response.put("guaCreditCode", guaCreditCode);// 担保公司社会信用代码
        response.put("guaranteeIdCard", guaranteeIdCard);// 担保公司法定代表人身份证号
        response.put("guaCardNum", guaCardNum);// 担保公司卡号
        response.put("guaBankName", guaBankName);// 担保公司卡开户行
        response.put("guaBankCity", guaBankCity);// 担保公司卡开户地区

        response.put("vehicleType", vehicleType);// 车系
        response.put("vehiclePlate", vehiclePlate);// 车牌号
        response.put("vehicleIdNo", vehicleIdNo);// 车架号

        response.put("loanType", loanType);// 借款类型 1.个人借款；2.机构借款
        response.put("trulyLoanType", trulyLoanType);// 实际借款类型 1.个人借款 2.机构借款
        response.put("creditType", creditType);// 授信类型1、代表质押2、代表非质押
        response.put("houseAddr", houseAddr);// 房屋地址
        response.put("houseType", houseType);// 房屋类型
        response.put("houseArea", houseArea);// 房屋面积
        response.put("houseValuation", houseValuation);// 房屋评估价格
        response.put("housePledged", housePledged);// 房屋是否有一抵0：无1：有
        response.put("housePledgedAmount", housePledgedAmount);// 一抵金额

        if ("2".equals(this.channal)) {
            response.put("channal", ASSET_ALLOCATION_CHANNAL_JL);
        } else {
            response.put("channal", ASSET_ALLOCATION_CHANNAL);
        }
        return response;
    }
}
