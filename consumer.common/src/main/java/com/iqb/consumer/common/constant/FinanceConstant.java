package com.iqb.consumer.common.constant;

/**
 * 对应账户系统相关参数常量
 */
public class FinanceConstant {
    public static final String SUCCESS = "success";// 成功
    public static final String ERROR = "error";// 失败

    /** 结果处理相关常量 **/
    public class ResultConstant {
        /** 列表总数key **/
        public static final String TOTALCOUNT_KEY = "totalCount";
        /** 每页数量key **/
        public static final String NUMPERPAGE_KEY = "numPerPage";
        /** 当前页码key **/
        public static final String CURRENTPAGE_KEY = "currentPage";
        /** 结果key **/
        public static final String RESULT_KEY = "result";
        /** 结果列表key **/
        public static final String RECORDLIST_KEY = "recordList";
        /** retCode key **/
        public static final String RETCODE_KEY = "retCode";
        /** msg key **/
        public static final String RETMSG_KEY = "msg";
        /** RETCODE_SUCC key **/
        public static final String RETCODE_SUCC = "success";
        /** RETCODE_ERROR key **/
        public static final String RETCODE_ERROR = "error";
    }

    /** 参数相关常量 **/
    public class ParameterConstant {
        /** getOverdueBill key **/
        public static final String GETOVERDUEBILL_KEY = "getOverdueBill";
        /** GETOVERDUEBILL_DEFAULT_VALUE **/
        public static final String GETOVERDUEBILL_DEFAULT_VALUE = "1";
        /** status key **/
        public static final String STATUS_KEY = "status";
        /** status_overdue key **/
        public static final String STATUS_OVERDUE_VALUE = "0";
    }

}
