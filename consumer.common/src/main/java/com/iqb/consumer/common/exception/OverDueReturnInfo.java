package com.iqb.consumer.common.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.IReturnInfo;

public enum OverDueReturnInfo implements IReturnInfo {

    OVERDUE_DOUBLE("70000000", "用户同时执行违约操作,请稍等", "用户同时执行违约操作,请稍等"),
    OVERDUE_DOUBLE_ORDERID("70000001", "存在订单重复发起的情况", "存在订单重复发起的情况");

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
    private OverDueReturnInfo(String retCode, String retFactInfo, String retUserInfo) {
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
    private static Map<String, OverDueReturnInfo> map = new HashMap<String, OverDueReturnInfo>();

    /** 将所有枚举缓存 */
    static {
        EnumSet<OverDueReturnInfo> currEnumSet = EnumSet.allOf(OverDueReturnInfo.class);

        for (OverDueReturnInfo retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }

}
