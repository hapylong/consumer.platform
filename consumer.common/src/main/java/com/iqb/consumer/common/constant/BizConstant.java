package com.iqb.consumer.common.constant;

/**
 * 
 * Description: 业务相关常量
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月13日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class BizConstant {

    /** 流程定义代码 **/
    public class BizProcDefKeyConstant {
        /** 罚息减免 **/
        public static final String PENALTYDERATE_KEY = "penalty_derate_apply";
    }

    /** 质押车流程定义代码 **/
    public class PledgeInquiryWFCode {
        /** 车辆评估 **/
        public static final String PLEDGE_INQUIRY_PRETREATMENT = "pledge_inquiry_pretreatment";
        public static final String PLEDGE_INQUIRY_REVIEW = "pledge_inquiry_review";
    }

    /** 抵押车流程定义代码 **/
    public class BizPledgeProcDefKeyConstant {
        /** 订单申请 **/
        public static final String PLEDGE_ORDER_KEY = "pledge_order";
        /** 风控部审批 **/
        public static final String PLEDGE_DEPARTMENT_KEY = "pledge_department";
        /** 车帮风控审批 **/
        public static final String PLEDGE_RISK_KEY = "pledge_risk";
        /** 项目信息维护 **/
        public static final String PLEDGE_STORE_KEY = "pledge_store";
        /** 项目初审 **/
        public static final String PLEDGE_FIRST_PROJECT_KEY = "pledge_first_project";
        /** 入库确认 **/
        public static final String PLEDGE_STORAGE_KEY = "pledge_storage";
        /** 项目终审 **/
        public static final String PLEDGE_FINAL_PROJECT_KEY = "pledge_final_project";
    }

    /** 工作流相关常量 **/
    public class WFConst {
        /** 工作流接口返回成功字段 **/
        public static final String WFInterRetSuccKey = "success";
        /** 流程id key **/
        public static final String ProcInstIdKey = "procInstId";
        /** 工作流接口返回成功 **/
        public static final String WFInterRetSucc = "1";
        /** 流程审批结论 1：通过；2：退回；0：拒绝 **/
        public static final String PROCAPPRSTATUS_SUCC = "1";
        public static final String PROCAPPRSTATUS_BACK = "2";
        public static final String PROCAPPRSTATUS_REFUSE = "0";
        /** 流程是否结束，1：是；0：否 **/
        public static final String PROCENDED_YES = "1";
        public static final String PROCENDED_NO = "0";
    }

}
