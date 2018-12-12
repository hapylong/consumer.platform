package com.iqb.consumer.common.constant;

import java.util.HashSet;
import java.util.Set;

public class Constant {

    public static final int HOUSE_BILL_WFSTATUS_100 = 1; // @response BillQueryPojo //可分期单子
    public static final int HOUSE_BILL_WFSTATUS_100_PAGE = 2; // @response BillQueryPojo //可分期单子
    public static final Set<Integer> HOUSE_BILL_QUERY_TYPE = new HashSet<>();
    static {
        HOUSE_BILL_QUERY_TYPE.add(HOUSE_BILL_WFSTATUS_100);
        HOUSE_BILL_QUERY_TYPE.add(HOUSE_BILL_WFSTATUS_100_PAGE);
    }

    public static final int BILL_QUERY_HOUSE = 2; // 查询待还款账单

    public static final int FINANCE_QUERY_BILL_LIST_DAIHUANKUAN = 1; // 查询待还款账单
    public static final int FINANCE_QUERY_BILL_LIST_YIHUANKUAN = 3; // 查询已还款账单
    public static final int FINANCE_QUERY_BILL_LIST_YIYUQI = 5; // 查询已逾期账单
    public static final int FINANCE_QUERY_BILL_LIST_PAGE_DAIHUANKUAN = 2; // 查询待还款账单分页
    public static final int FINANCE_QUERY_BILL_LIST_PAGE_YIHUANKUAN = 4; // 查询已还款账单分页
    public static final int FINANCE_QUERY_BILL_LIST_PAGE_YIYUQI = 6; // 查询已逾期账单分页

    public static final String FINANCE_QUERY_STATUS_YIYUQI = "0"; // 已逾期
    public static final String FINANCE_QUERY_STATUS_DAIHUANKUAN = "1"; // 待还款
    public static final String FINANCE_QUERY_STATUS_YIHUANKUAN = "3"; // 已还款

}
