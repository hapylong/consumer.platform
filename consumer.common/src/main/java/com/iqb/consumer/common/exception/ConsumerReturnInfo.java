package com.iqb.consumer.common.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.IReturnInfo;

/**
 * 
 * Description: 电子合同返回码
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年2月7日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public enum ConsumerReturnInfo implements IReturnInfo {

    /** 通用异常0100 **/
    CONSUMER_COMMON_01000001("consumer_common_01000001", "传入信息为空", "传入信息为空"),
    CONSUMER_COMMON_01000002("consumer_common_01000002", "JSON格式转换异常", "JSON格式转换异常"),
    CONSUMER_COMMON_01000003("consumer_common_01000003", "入参校验失败", "入参校验失败"),
    CONSUMER_COMMON_01000004("consumer_common_01000004", "未查询到对应的信息", "未查询到对应的信息"),
    CONSUMER_COMMON_01000005("consumer_common_01000005", "入参完整性校验失败", "入参完整性校验失败"),

    CONSUMER_FINANCE_02000006("consumer_finance_01000006", "调用账户系统返回失败", "调用账户系统返回失败"),
    CONSUMER_FINANCE_02000007("consumer_finance_01000007", "解析账户系统返回数据失败", "解析账户系统返回数据失败"),
    CONSUMER_FINANCE_02000008("consumer_finance_01000008", "调用账户系统罚息减免接口失败", "调用账户系统罚息减免接口失败"),

    CONSUMER_WF_03000001("consumer_wf_03000001", "工作流返回数据为空", "工作流返回数据为空"),
    CONSUMER_WF_03000002("consumer_wf_03000002", "工作流接口调用失败", "工作流接口调用失败"),
    CONSUMER_WF_03000003("consumer_wf_03000003", "工作流返回的流程id为空", "工作流返回的流程id为空"),
    CONSUMER_WF_03000004("consumer_wf_03000004", "工作流返回的业务ID为空", "工作流返回的业务ID为空"),
    CONSUMER_WF_03000005("consumer_wf_03000005", "工作流返回的流程审批结论为空", "工作流返回的流程审批结论为空"),
    CONSUMER_WF_03000006("consumer_wf_03000006", "工作流返回的当前任务为空", "工作流返回的当前任务为空"),
    CONSUMER_WF_03000007("consumer_wf_03000007", "工作流返回的是否结束为空", "工作流返回的是否结束为空"),
    CONSUMER_WF_03000008("consumer_wf_03000008", "工作流前置回调中未获取到机构代码", "工作流前置回调中未获取到机构代码"),

    CONSUMER_PENALTY_DERATE_02000001("consumer_penalty_derate_02000001", "该笔账单已经操作过", "该笔账单已经操作过"),
    CONSUMER_PENALTY_DERATE_02000002("consumer_penalty_derate_02000002", "该订单已经发起提前结清流程", "该订单已经发起提前结清流程"),
    PROJECT_CREATE_NAME("projectName", "项目名称生成失败", "项目名称生成失败"),

    GET_PROJECT_CREATE_NAME_LOCK("getProjectNameLock", "获取项目名称锁", "获取项目名称锁"),

    EC_POI_09010001("ec01010003", "没有找到对应的ec通道", "没有找到对应的ec通道"),

    CONSUMER_INSTSETTLECONFIG_01000001("consumer_instsettleconfig_01000001", "未选择商户", "未选择商户"),
    CONSUMER_FINANCE_02000009("CONSUMER_FINANCE_02000009", "金额计算错误", "金额计算错误"),
    CONSUMER_COMMON_01000006("consumer_common_01000006", "该订单在罚息减免流程中已启动", "该订单在罚息减免流程中已启动");

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
    private ConsumerReturnInfo(String retCode, String retFactInfo, String retUserInfo) {
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
    private static Map<String, ConsumerReturnInfo> map = new HashMap<String, ConsumerReturnInfo>();

    /** 将所有枚举缓存 */
    static {
        EnumSet<ConsumerReturnInfo> currEnumSet = EnumSet.allOf(ConsumerReturnInfo.class);

        for (ConsumerReturnInfo retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }

}
