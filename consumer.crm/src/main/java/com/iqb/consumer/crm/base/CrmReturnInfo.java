package com.iqb.consumer.crm.base;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.IReturnInfo;

/**
 * 
 * Description: 系统返回码
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月19日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public enum CrmReturnInfo implements IReturnInfo {
    /** 通用 **/
    COMMON_01010001("crm_01010001", "数据完整性校验失败", "系统异常，请联系管理员"),

    /** etep推送相关 **/
    ETEP_PUSH_01020001("crm_01020001", "etep推送信息为空", "etep推送信息为空"),
    ETEP_PUSH_01020002("crm_01020002", "etep推送机构编码为空", "etep推送机构编码为空"),

    /** customer推送相关 **/
    CUSTOMER_PUSH_01030001("crm_01030001", "customer推送至消费金融信息为空", "信息不完整"),
    CUSTOMER_PUSH_01030002("crm_01030002", "数据库中不存在传入的customerCode", "系统异常，请联系管理员"),
    CUSTOMER_PUSH_01030003("crm_01030003", "crm推送至其他系统代码抛异常", "推送失败，请联系管理员"),

    /** crm客户相关 **/
    CRM_CUSTOMER_01040001("crm_01040001", "传入客户信息为空", "传入客户信息为空"),
    CRM_CUSTOMER_01040002("crm_01040002", "传入客户编码为空", "传入客户编码为空"),
    CRM_CUSTOMER_01040003("crm_01040003", "根据客户编码查询客户信息为空", "系统异常，请联系管理员"),
    CRM_CUSTOMER_01040004("crm_01040004", "客户编码已存在", "客户编码已存在"),
    CRM_CUSTOMER_01040005("crm_01040005", "传入客户类型为空", "传入客户类型为空"),
    CRM_CUSTOMER_01040006("crm_01040006", "客户简称编码已存在", "客户简称编码已存在"),
    CRM_CUSTOMER_01040007("crm_01040007", "客户已经推送消费金融不能删除", "客户已经推送消费金融不能删除"),

    /** crm门店接口相关 **/
    CRM_CUSTOMER_STORE_01050001("crm_01050001", "传入信息为空", "传入信息为空"),
    CRM_CUSTOMER_STORE_01050002("crm_01050002", "传入客户编码为空", "传入客户编码为空");

    /** 响应代码 **/
    private String retCode = "";

    /** 提示信息-用户提示信息 **/
    private String retUserInfo = "";

    /** 响应码含义-实际响应信息 **/
    private String retFactInfo = "";

    /**
     * @param retCode 响应代码
     * @param retFactInfo 响应码含义-实际响应信息
     * @param retUserInfo 提示信息-用户提示信息
     */
    private CrmReturnInfo(String retCode, String retFactInfo, String retUserInfo) {
        this.retCode = retCode;
        this.retFactInfo = retFactInfo;
        this.retUserInfo = retUserInfo;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetFactInfo() {
        return retFactInfo;
    }

    public void setRetFactInfo(String retFactInfo) {
        this.retFactInfo = retFactInfo;
    }

    public String getRetUserInfo() {
        return retUserInfo;
    }

    public void setRetUserInfo(String retUserInfo) {
        this.retUserInfo = retUserInfo;
    }

    /**
     * 通过响应代码 获取对应的ReturnInfo
     * 
     * @param retCode-返回码
     * @return 响应枚举类型
     */
    public IReturnInfo getReturnCodeInfoByCode(IReturnInfo returnInfo) {
        if (map.get(returnInfo.getRetCode()) != null) {
            return map.get(returnInfo.getRetCode());
        } else {
            return CommonReturnInfo.BASE00000099;
        }
    }

    /**
     * 重写toString
     */
    public String toString() {
        return new StringBuffer("{retCode:").append(retCode)
                .append(";retFactInfo(实际响应信息):").append(retFactInfo)
                .append(";retUserInfo(客户提示信息):").append(retUserInfo).append("}").toString();
    }

    /** 存放全部枚举的缓存对象 */
    private static Map<String, CrmReturnInfo> map = new HashMap<String, CrmReturnInfo>();

    /** 将所有枚举缓存 */
    static {
        EnumSet<CrmReturnInfo> currEnumSet = EnumSet.allOf(CrmReturnInfo.class);

        for (CrmReturnInfo retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }
}
