package com.iqb.consumer.common.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.IReturnInfo;

public enum ApiReturnInfo implements IReturnInfo {
    /** 通用异常0100 **/
    API_SUCCESS_00000000("00000000", "数据成功接收", "数据成功接收"),
    API_GETNULLKEY_10000000("10000000", "商户无密钥", "商户密钥未配置"),
    API_GETKEY_10000001("10000001", "解密异常", "API接口,获取商户密钥异常"),
    API_DECRYPTION_10000002("10000002", "解密异常", "API接口接收数据解密异常"),

    API_BIZANALYTIC_20000000("20000000", "解析客户发送过来的数据格式不对", "数据格式不对"),
    API_BIZ_SAVEUSER_ERROR_20000001("20000001", "保存用户信息异常", "保存用户信息失败"),
    API_BIZ_SAVEBANK_ERROR_20000002("20000002", "保存银行卡信息异常", "保存银行卡信息失败"),
    API_BIZ_SAVERISK_RROR_20000003("20000003", "保存风控信息异常", "保存风控信息失败"),
    API_BIZ_SAVERISK_RROR_20000004("20000004", "保存车辆信息异常", "保存车辆信息异常"),
    API_BIZ_SAVERISK_RROR_20000005("20000005", "保存车辆经销商信息异常", "保存车辆经销商信息异常"),
    API_BIZ_SAVERISK_RROR_20000006("20000006", "保存车辆转租信息异常", "保存车辆转租信息异常"),
    API_BIZ_SAVEORDER_ERROR_20000004("20000004", "订单生成失败", "生成订单失败"),

    API_BIZ_STARTWF_30000000("30000000", "启动流程异常", "启动流程异常"),
    API_BIZ_STARTWF_30000001("30000001", "启动流程配置错误", "启动流程配置错误"),

    API_BIZ_PARAMS_40000001("40000001", "参数格式异常", "参数格式异常"),
    API_MERCHANT_NULL_40000002("40000002", "商户号为空", "商户号为空"),
    API_BIZTYPE_NULL_40000003("40000003", "业务类型为空", "业务类型为空"),
    API_BIZTYPE_NULL_40000004("40000004", "手机号码为空", "手机号码为空"),
    API_BIZTYPE_NULL_40000005("40000005", "用户订单号为空", "用户订单号为空"),
    API_PLAN_NOEXIT_40000004("40000004", "计划ID不存在", "计划不存在"),
    API_RECAL_ERROR_40000006("40000006", "计算明细发生错误", "计算明细发生错误"),
    API_PAY_PARAMS_NULL_40000007("40000007", "手机号码不能为空", "手机号码不能为空"),
    API_PAY_PARAMS_NULL_40000008("40000008", "银行卡号不能为空", "银行卡号不能为空"),
    API_PAY_PARAMS_NULL_40000009("40000009", "金额不能为空", "金额不能为空"),
    API_PAY_PARAMS_NULL_400000010("400000010", "验证码不能为空", "验证码不能为空"),
    API_PAY_PARAMS_NULL_400000011("400000011", "身份证号码不能为空", "身份证号码不能为空"),
    API_PAY_PARAMS_NULL_400000012("400000012", "订单号不能为空", "订单号不能为空"),
    API_PAY_PARAMS_NULL_400000013("400000013", "还款类别不能为空", "还款类别不能为空"),
    API_PAY_PARAMS_NULL_400000014("400000014", "支付ID不能为空", "支付ID不能为空"),
    API_PAY_PARAMS_NULL_400000015("400000015", "还款金额与预付款金额不匹配", "还款金额与预付款金额不匹配"),
    API_PAY_PARAMS_NULL_400000016("400000016", "还款金额与账单金额不匹配", "还款金额与账单金额不匹配"),
    API_PAY_PARAMS_NULL_400000017("400000017", "支付ID不匹配", "支付ID不匹配"),
    API_PAY_PARAMS_NULL_400000018("400000018", "用户姓名不能为空", "用户姓名不能为空"),
    API_PAY_PARAMS_NULL_400000019("400000019", "第三方回调地址不能为空", "第三方回调地址不能为空"),
    API_PAY_PARAMS_NULL_400000020("400000020", "支付参数校验失败", "支付参数校验失败"),
    API_GET_SUCCESS_80000000("80000000", "成功获取数据", "成功获取数据"),
    API_GET_FAIL_80000002("80000002", "查询账户系统出现异常", "查询账户系统出现异常"),
    API_GET_FAIL_80000003("80000003", "当前订单未产生账单", "当前订单未产生账单"),
    API_GET_FAIL_80000004("80000004", "当前订单相关信息丢失", "当前订单相关信息丢失"),
    API_GET_FAIL_80000005("80000005", "无此订单", "无此订单"),
    API_GET_FAIL_80000006("80000006", "调用先锋接口无返回", "调用先锋接口无返回"),
    API_NOTSUPPORTBIZ_90000000("90000000", "业务暂不支持", "接口业务暂不支持"),
    API_NOTINWHITELIST_90000001("90000001", "不在白名单", "请先添加白名单"),

    API_INTO_PARAMS_NULL_500000001("500000001", "身份证号码不能为空", "身份证号码不能为空"),
    API_INTO_PARAMS_NULL_500000002("500000002", "借款人姓名不能为空", "借款人姓名不能为空"),
    API_INTO_PARAMS_NULL_500000003("500000003", "借款人手机号码不能为空", "借款人手机号码不能为空"),
    API_INTO_PARAMS_NULL_500000004("500000004", "银行卡号不能为空", "银行卡号不能为空"),
    API_INTO_PARAMS_NULL_500000005("500000005", "银行预留手机号不能为空", "银行预留手机号不能为空"),
    API_INTO_PARAMS_NULL_500000006("500000006", "银行简称不能为空", "银行简称不能为空"),
    API_INTO_PARAMS_NULL_500000007("500000007", "银行名称不能为空", "银行名称不能为空"),
    API_INTO_PARAMS_NULL_500000008("500000008", "购车价格不能为空", "购车价格不能为空"),
    API_INTO_PARAMS_NULL_500000009("500000009", "Gps价格不能为空", "Gps价格不能为空"),
    API_INTO_PARAMS_NULL_5000000010("5000000010", "保险价格不能为空", "保险价格不能为空"),

    API_INTO_PARAMS_NULL_5000000011("5000000011", "订单金额不能为空", "订单金额不能为空"),
    API_INTO_PARAMS_NULL_5000000012("5000000012", "订单名称不能为空", "订单名称不能为空"),
    API_INTO_PARAMS_NULL_5000000013("5000000013", "错误的产品方案", "错误的产品方案"),
    API_INTO_PARAMS_NULL_5000000014("5000000014", "商户名称不能为空", "商户名称不能为空"),
    API_INTO_PARAMS_NULL_5000000015("5000000015", "业务类型不能为空", "业务类型不能为空"),
    API_INTO_PARAMS_NULL_5000000016("5000000016", "婚姻状态不能为空", "婚姻状态不能为空"),
    API_INTO_PARAMS_NULL_5000000017("5000000017", "地址不能为空", "地址不能为空"),
    API_INTO_PARAMS_NULL_5000000018("5000000018", "亲属姓名1不能为空", "亲属姓名1不能为空"),
    API_INTO_PARAMS_NULL_5000000019("5000000019", "亲属电话1不能为空", "亲属电话1不能为空"),
    API_INTO_PARAMS_NULL_5000000020("5000000020", "亲属姓名2不能为空", "亲属姓名2不能为空"),

    API_INTO_PARAMS_NULL_5000000021("5000000021", "亲属电话2不能为空", "亲属电话2不能为空"),
    API_INTO_PARAMS_NULL_5000000022("5000000022", "员工号不能为空", "员工号不能为空"),
    API_INTO_PARAMS_NULL_5000000023("5000000023", "员工姓名不能为空", "员工姓名不能为空"),

    API_INTO_PARAMS_NULL_5000000024("5000000024", "车架号不能为空", "车架号不能为空"),
    API_INTO_PARAMS_NULL_5000000025("5000000025", "发动机型号不能为空", "发动机型号不能为空"),
    API_INTO_PARAMS_NULL_5000000026("5000000026", "车牌号不能为空", "车牌号不能为空"),
    API_INTO_PARAMS_NULL_5000000027("5000000027", "车辆类型不能为空", "车辆类型不能为空"),
    API_INTO_PARAMS_NULL_5000000028("5000000028", "转租订单号不能为空", "转租订单号不能为空"),
    API_INTO_PARAMS_NULL_5000000029("5000000029", "是否线上收取不能为空", "是否线上收取不能为空"),
    API_INTO_PARAMS_NULL_5000000030("5000000030", "费用合计不能为空", "费用合计不能为空"),

    API_INTO_PARAMS_NULL_5000000031("5000000031", "购置税不能为空", "购置税不能为空"),
    API_INTO_PARAMS_NULL_5000000032("5000000032", "车商服务费不能为空", "车商服务费不能为空"),

    API_AFTERLOAN_SUCCESS_00000000("00000000", "数据保存成功", "数据保存成功"),
    API_AFTERLOAN_FAIL_00000000("00000001", "数据保存失败", "数据保存失败"), ;

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
    private ApiReturnInfo(String retCode, String retFactInfo, String retUserInfo) {
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
    private static Map<String, ApiReturnInfo> map = new HashMap<String, ApiReturnInfo>();

    /** 将所有枚举缓存 */
    static {
        EnumSet<ApiReturnInfo> currEnumSet = EnumSet.allOf(ApiReturnInfo.class);

        for (ApiReturnInfo retCodeType : currEnumSet) {
            map.put(retCodeType.getRetCode(), retCodeType);
        }
    }
}
