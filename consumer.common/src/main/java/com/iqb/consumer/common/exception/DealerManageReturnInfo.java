package com.iqb.consumer.common.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.IReturnInfo;

public enum DealerManageReturnInfo implements IReturnInfo {

    DEALER_SAVEDATA_00000000("00000000", "成功保存数据", "成功保存数据"),
    DEALER_FORMATERROR_10000000("10000000", "数据格式化异常", "数据不全"),
    DEALER_SAVEERROR_10000001("10000001", "存储车管信息异常", "车辆管理信息保存失败,请重试"),
    DEALER_UPDATEERROR_10000002("10000002", "修改车管信息异常", "修改资料错误,请查看后重试"),
    DEALER_ORDERIDNOTEXIT_10000003("10000003", "车商信息不存在", "车商信息不存在"),
    DEALER_QUERYERROR_10000004("10000004", "查询车商存在异常", "查询车商信息失败");
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
    private DealerManageReturnInfo(String retCode, String retFactInfo, String retUserInfo) {
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
    private static Map<String, DealerManageReturnInfo> map = new HashMap<String, DealerManageReturnInfo>();

    /** 将所有枚举缓存 */
    static {
        EnumSet<DealerManageReturnInfo> currEnumSet = EnumSet.allOf(DealerManageReturnInfo.class);

        for (DealerManageReturnInfo retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }
}
