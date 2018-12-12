/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年6月14日 下午8:43:54
 * @version V1.0
 */
package com.iqb.consumer.common.exception;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public enum Code {
    XFSUCCESS("00", "调用服务成功"),
    SUCCESS("0000", "处理成功"),
    FAIL("00001", "处理失败"),
    S("S", "成功"),
    F("F", "失败"),
    I("I", "处理中"),
    VERIFY_S("100001", "认证成功"),

    SING_BUILD_F("3001", "生成签名异常"),
    SIGN_VERIFY_FAIL("3002", "验签失败"),
    PARAM_VALIAD_FAIL("3003", "参数校验失败"),
    NET_EXCEPTION("3004", "网络连接异常"),
    ERROR("9999", "系统异常"),

    PAY_COMFIRM_FAIL("10001", "支付失败"),
    PAY_PREPAY_FAIL("1002", "预支付失败"),
    PAY_PREPAY_EXCEPTION("1003", "调用预支付发生异常"),
    PAY_SMS_VERIFY_FAIL("1004", "短信校验失败"),
    PAY_SMS_SEND_FAIL("1005", "重发短信失败"),
    PAY_BANK_QUERY_FAIL("1006", "获取银行列表失败"),
    PAY_CONFIRM_ERROR("1007", "确认支付发生异常"),

    ORDER_ADD_FAIL("2001", "保存订单发生数据库异常"),
    ORDER_MOD_FAIL("2002", "更新订单发生数据库异常"),
    ORDER_GET_FAIL("2003", "查询订单失败"),
    ORDER_QUERY_NONE("2004", "订单不存在"),
    ORDER_QUERY_ERROR("2005", "查询先锋支付订单发生异常"),
    ORDER_QUERY_GATENONE("2006", "支付网关订单不存在"),
    ORDER_QUERY_XFNONE("2007", "查询先锋订单不存在");

    private String code;
    private String desc;

    private Code(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
