package com.iqb.consumer.asset.allocation.base;

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
 * 2016年9月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public enum AssetAllocationReturnInfo implements IReturnInfo {
    /** 通用 **/
    COMMON_01010001("crm_01010001", "数据完整性校验失败", "系统异常，请联系管理员"),
    ASSET_PUSH_01030001("asset_01030001", "asset推送信息为空", "信息不完整"),
    ASSET_PUSH_01030002("asset_01030002", "数据库中不存在传入的order id", "系统异常，请联系管理员"),
    ASSET_PUSH_01030003("asset_01030003", "asset推送至其他系统代码抛异常", "asset推送至其他系统代码抛异常"),
    ASSET_PUSH_01030004("asset_01030004", "asset推送至其他系统抛异常", "找不到推送平台"),
    ASSET_PUSH_01030005("asset_01030005", "asset推送至其他系统抛异常", "此标已存在,不需要再次同步"),
    ASSET_PUSH_01030006("asset_01030006", "asset推送至其他系统抛异常", "传入的参数为空"),

    ASSET_PUSH_01030008("asset_01030008", "爱钱帮返回信息为空", "爱钱帮返回信息为空"),
    ASSET_PUSH_01030009("asset_01030009", "调用爱钱帮注册接口返回失败", "调用爱钱帮注册接口返回失败"),
    ASSET_PUSH_01030010("asset_01030010", "资产推送获取债权人信息失败-传入用户代码为空", "资产推送获取债权人信息失败-传入用户代码为空"),
    ASSET_PUSH_01030011("asset_01030011", "资产推送获取推送爱钱帮资产数据失败-查询结果为空", "资产推送获取推送爱钱帮资产数据失败-查询结果为空"),
    ASSET_PUSH_01030012("asset_01030012", "资产分配-推送爱钱帮有字段不完整", "资产分配-推送爱钱帮有字段不完整"),

    ASSET_PUSH_01030007("asset_01030007", "asset申请日期大于剩余日期", "申请日期大于剩余日期");
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
    private AssetAllocationReturnInfo(String retCode, String retFactInfo, String retUserInfo) {
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
    private static Map<String, AssetAllocationReturnInfo> map = new HashMap<String, AssetAllocationReturnInfo>();

    /** 将所有枚举缓存 */
    static {
        EnumSet<AssetAllocationReturnInfo> currEnumSet = EnumSet.allOf(AssetAllocationReturnInfo.class);

        for (AssetAllocationReturnInfo retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }
}
