package com.iqb.consumer.manage.front.workflow.wfstructure;

import java.util.HashMap;
import java.util.Map;

public class WorkFlowsStructure {
    /** risk **/
    public static final int RISK_IN_REVIEW = 2; // 审核中 describe:帮帮手提交订单
    public static final int RISK_OVER_PASSED = 0; // 已通过 describe:人工风控通过
    public static final int RISK_OVER_REFUSE = 1; // 已拒绝 describe:人工风控拒绝
    public static final int RISK_OVER_STAGEE = 3; // 已分期 describe:订单分期
    public static final int RISK_OVER_CLEAR = 10; // 已结清 describe:提前还款完成/还款完成
    public static final int RISK_OVER_CANCEL = 6; // 已取消 describe:对已估价订单取消
    public static final int RISK_WAIT_ESTIMATE = 21; // 待估价 describe:帮帮手提交估价订单
    public static final int RISK_OVER_ESTIMATE = 22; // 已估价 describe:车帮综合部审核估价订单
    public static final int RISK_WAIT_VERIFY = 5; // 待确认 describe:医美
    public static final int RISK_OVER_SUSPEND = 11; // 已终止 describe:流程终止订单 新增

    // public static final int RISK_WAIT_PAYMENT = 4; //待支付 describe:已过时

    /** wf **/
    public static final String WF_OVER_REFUSE = "0"; // 流程拒绝
    public static final String WF_WAIT_CAR_ESTIMATED = "11"; // 待车价评估
    public static final String WF_WAIT_CAR_TWICE_ESTIMATED = "12"; // 待车价复评 describe:车价评估通过
    public static final String WF_WAIT_STORE_PRE_DEAL = "2"; // 待门店预处理
    public static final String WF_WAIT_RISK_APPROVE = "3"; // 待风控审批 describe:门店预处理通过
    public static final String WF_WAIT_MAN_RISK = "31"; // 待人工风控 describe:风控通过
    public static final String WF_WAIT_GOOD_EVALUATE = "4"; // 待抵质押物估价 describe:风控审核通过
    public static final String WF_WAIT_CAR_LOAN_ESTIMATED = "41"; // 待车秒贷审核 describe:抵质押物估价通过
    public static final String WF_WAIT_CREDIT_CONFIRM = "44"; // 待门店额度确认 describe:抵质押物估价通过
    public static final String WF_WAIT_ITEM_INFO_VINDICATE = "5"; // 待项目信息维护 describe:抵质押审核通过
    public static final String WF_WAIT_RISK_TWICE_APPROVE = "6"; // 待内控审批 describe:项目信息审核通过
    public static final String WF_WAIT_FINANCE_VERIFY = "7"; // 待财务收款确认 describe:内控审核通过
    public static final String WF_WAIT_ITEM_FIRST_REVIEW = "8"; // 待项目初审 describe:财务审核通过
    public static final String WF_WAIT_ITEM_OBTAIN_VERIFY = "81"; // 待确认入库
    public static final String WF_WAIT_ITEM_LAST_REVIEW = "82"; // 待终审
    public static final String WF_OVER_FINISHED = "9"; // 流程结束

    /** preAmtStatus **/
    public static final int PRE_AMT_WAIT_PAY = 2; // 待支付 describe:抵质押物估价/门店额度确认
    public static final int PRE_AMT_OVER_PAY = 1; // 已支付 describe:首付款已支付

    public static final Map<Integer, Integer> RISK_STRUCTURE = new HashMap<Integer, Integer>();

}
