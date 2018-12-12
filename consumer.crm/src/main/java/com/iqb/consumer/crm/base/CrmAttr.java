package com.iqb.consumer.crm.base;

import com.iqb.etep.common.utils.Attr;

/**
 * 
 * Description: crm常量
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2016年9月19日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
public class CrmAttr extends Attr {

    /** 系统数据库默认属性 **/
    public class CrmDBDefaultAttr {
        /** 版本号 **/
        public static final String CRM_DEFAULT_VERSION = "1";
        /** 删除标识 1:未删除 2:已删除 **/
        public static final String CRM_DEFAULT_DELETE_FLAG = "1";
        /** 客户状态 1:正常 2:冻结 **/
        public static final String CRM_DEFAULT_customer_status = "1";

    }

    /** crm系统推送相关常量 **/
    public class CrmPushCommonAttr {
        /** 推送类型 1:新增 **/
        public static final String CRM_PUSH_TYPE_ADD = "0";
        /** 推送类型 2:修改 **/
        public static final String CRM_PUSH_TYPE_UPDATE = "2";
        /** 推送类型 3:删除 **/
        public static final String CRM_PUSH_TYPE_DELETE = "3";

        /** 推送状态 1:开始 **/
        public static final String CRM_PUSH_STATUS_START = "1";
        /** 推送状态 2:结束 **/
        public static final String CRM_PUSH_STATUS_END = "2";
        /** 推送状态 3:异常 **/
        public static final String CRM_PUSH_STATUS_ERROE = "3";

        /** 推送返回状态 1:成功 **/
        public static final String CRM_PUSH_STATUS_SUCC = "1";
        /** 推送返回状态 2:失败 **/
        public static final String CRM_PUSH_STATUS_FAIL = "2";

        /** 推送返回结果 1:成功 **/
        public static final String CRM_PUSH_XFJR_SUCC_RESULT = "succ";
        /** 推送返回结果 2:失败 **/
        public static final String CRM_PUSH_XFJR_FAIL_RESULT = "fail";

    }

    /** crm推送地址 **/
    public class CrmPushUrlAttr {
        /** 消费金融 **/
        public static final String URL_XFJR = "http://192.168.1.176:8080/consumer.manage.front/merchant/unIntcpt-add";
    }

    /** crm推送接收端 **/
    public class CrmPushReceiveAttr {
        /** 消费金融 **/
        public static final String RECEIVE_XFJR = "消费金融";
    }

    /** 上传图片类型 **/
    public class CrmImgType {
        /** 公司印章 **/
        public static final String IMG_TYPE_COMPONY_IMG = "1";
        /** 法人印章 **/
        public static final String IMG_TYPE_CORPORATE_IMG = "2";
    }

    /** http请求方式 **/
    public class HttpInterMode {
        /** http请求方式 **/
        public static final String HTTPINTERMODE_HTTP = "http";
        /** https请求方式 **/
        public static final String HTTPINTERMODE_HTTPS = "https";
    }

}
