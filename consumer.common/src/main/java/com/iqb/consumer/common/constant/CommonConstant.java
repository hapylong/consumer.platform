/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月6日 上午10:18:00
 * @version V1.0
 */
package com.iqb.consumer.common.constant;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class CommonConstant {
    public static final int UUID_LENGTH = 24;
    /** 默认版本号 **/
    public static final int DEFAULT_VERSION = 0;

    /** 分页相关常量 **/
    public class PageConst {
        /** 页数 **/
        public static final String PAGENUM_KEY = "pageNum";
        /** 页数默认值 **/
        public static final String PAGENUM_DEFAULT_VALUE = "1";
        /** 每页容量 **/
        public static final String PAGESIZE_KEY = "pageSize";
        /** 每页容量默认值 **/
        public static final String PAGESIZE_DEFAULT_VALUE = "10";
    }

    /** 状态相关常量 **/
    public class StatusConst {
        /** 减免申请进行中 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_START = "1";
        /** 减免申请成功 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_SUCC = "2";
        /** 减免申请失败 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_FAIL = "3";
        /** 减免申请终止 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_INTERRUPT = "4";
    }

    /** 工作流状态相关常量 **/
    public class WFStatusConst {
        /** 流程拒绝 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_0 = "0";
        /** 待车价评估 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_11 = "11";
        /** 待车价复评 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_12 = "12";
        /** 待风控审批 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_3 = "3";
        /** 待人工风控 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_31 = "31";
        /** 待抵质押物估价 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_4 = "4";
        /** 待车秒贷审核 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_41 = "41";
        /** 待项目信息维护 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_5 = "5";
        /** 待项目初审 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_8 = "8";
        /** 待确认入库 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_81 = "81";
        /** 待终审 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_82 = "82";
        /** 流程结束 **/
        public static final String PENALTY_DERATE_APPLY_STATUS_9 = "9";
    }

    /** 订单状态相关常量 **/
    public class RiskStatusConst {
        /** 拒绝 **/
        public static final String RiskStatus_1 = "1";
        /** 审核中 **/
        public static final String RiskStatus_2 = "2";
        /** 已通过 **/
        public static final String RiskStatus_0 = "0";
        /** 已终止 **/
        public static final String RiskStatus_11 = "11";
    }

    /** 工作流key值相关 **/
    public class WFKeyConst {
        /** 流程数据 **/
        public static final String PROCDATA_KEY = "procData";
        /** 流程参数 **/
        public static final String VARIABLEDATA_KEY = "variableData";
        /** 流程认证数据 **/
        public static final String AUTHDATA_KEY = "authData";
        /** 流程业务数据 **/
        public static final String BIZDATA_KEY = "bizData";
        /** 流程定义代码 **/
        public static final String PROCDEFKEY_KEY = "procDefKey";
        /** 用户认证方式 1token 2session **/
        public static final String PROCAUTHTYPE_KEY = "procAuthType";
        /** 用户认证方式 1token 2session **/
        public static final String PROCAUTHTYPE_TOKEN = "1";
        /** 用户认证方式 1token 2session **/
        public static final String PROCAUTHTYPE_SESSION = "2";
        /** 流程审批结论 **/
        public static final String PROCAPPRSTATUS_KEY = "procApprStatus";
        /** 流程审批意见 **/
        public static final String PROCAPPROPINION_KEY = "procApprOpinion";
        /** 流程指定处理人 **/
        public static final String PROCASSIGNEE_KEY = "procAssignee";
        /** 指定流转任务 **/
        public static final String PROCAPPOINTTASK_KEY = "procAppointTask";
        /** 业务ID **/
        public static final String PROCBIZID_KEY = "procBizId";
        /** 业务类型 **/
        public static final String PROCBIZTYPE_KEY = "procBizType";
        /** 业务所属机构 **/
        public static final String PROCORGCODE_KEY = "procOrgCode";
        /** 流程摘要 **/
        public static final String PROCBIZMEMO_KEY = "procBizMemo";
        /** 流程定义代码 **/
        public static final String PROCTASKID_KEY = "procTaskId";
        /** 当前任务 **/
        public static final String PROCCURRTASK_KEY = "procCurrTask";
        /** 流程是否结束 **/
        public static final String PROCENDED_KEY = "procEnded";
        /** 下一节点任务 **/
        public static final String PROCNEXTTASKS_KEY = "procNextTasks";
        /** 减免逾期罚息信息 **/
        public static final String PENALTYDERATEINFO_KEY = "penaltyDerateInfo";
    }
    /**
     * bizType
     */
    public class BizTypeConst {
        public static final String BIZTYPE_1000 = "1000";
        public static final String BIZTYPE_1100 = "1100";
        public static final String BIZTYPE_2001 = "2001";
        public static final String BIZTYPE_2002 = "2002";
        public static final String BIZTYPE_2100 = "2100";
        public static final String BIZTYPE_2200 = "2200";
        public static final String BIZTYPE_2300 = "2300";
        public static final String BIZTYPE_3001 = "3001";
    }
    public class DelerStatus {
        public static final String DelerStatus_1 = "1";
        public static final String DelerStatus_2 = "2";

    }
    /**
     * 质押车状态 1 评估中 2 已估价 3 已退回 4 已使用
     * 
     */
    public class pledgeStatus {
        /** 评估中 **/
        public static final String PLEDGESTATUS_1 = "1";
        /** 已估价 **/
        public static final String PLEDGESTATUS_2 = "2";
        /** 已退回 **/
        public static final String PLEDGESTATUS_3 = "3";
        /** 已使用 **/
        public static final String PLEDGESTATUS_4 = "4";
        /** 已拒绝 **/
        public static final String PLEDGESTATUS_5 = "5";

    }

    public enum DictTypeCodeEnum {
        bizType2OpenId, business_type, lease_financial, SECTORS
    }

    public class OrderHandelFlag {
        public static final String pro = "pro";
        public static final String base = "test_";
        public static final String orderFlag = "sf";
    }
    public class CarStatus {
        /** 已失联 **/
        public static final String STATUS_20 = "20";
        /** 待入库 **/
        public static final String STATUS_25 = "25";
        /** 已入库 **/
        public static final String STATUS_30 = "30";
        /** 待出售 **/
        public static final String STATUS_35 = "35";
        /** 已出售 **/
        public static final String STATUS_40 = "40";
        /** 待转租 **/
        public static final String STATUS_45 = "45";
        /** 已转租 **/
        public static final String STATUS_50 = "50";
        /** 待返还 **/
        public static final String STATUS_55 = "55";
        /** 已返还 **/
        public static final String STATUS_60 = "60";
        /** 未拖回 **/
        public static final String STATUS_65 = "65";
        /** 正常 **/
        public static final String STATUS_70 = "70";
    }
    public class ProcessStatus {
        /** 待贷后处理中 **/
        public static final String ProcessStatus_05 = "5";
        /** 贷后处理中 **/
        public static final String ProcessStatus_10 = "10";
        /** 待外包处理 **/
        public static final String ProcessStatus_15 = "15";
        /** 外包处理中 **/
        public static final String ProcessStatus_20 = "20";
        /** 法务处理中 **/
        public static final String ProcessStatus_25 = "25";
        /** 处理结束 **/
        public static final String ProcessStatus_30 = "30";
    }
    public class CaseStatus {
        /** 待法务受理 **/
        public static final String CaseStatus_05 = "5";
        /** 资料准备中 **/
        public static final String CaseStatus_10 = "10";
        /** 立案申请中 **/
        public static final String CaseStatus_15 = "15";
        /** 已立案 **/
        public static final String CaseStatus_20 = "20";
        /** 庭审登记中 **/
        public static final String CaseStatus_25 = "25";
        /** 执行中 **/
        public static final String CaseStatus_30 = "30";
        /** 结束 **/
        public static final String CaseStatus_35 = "35";
    }
}
