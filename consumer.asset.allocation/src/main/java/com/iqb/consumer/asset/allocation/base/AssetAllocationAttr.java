package com.iqb.consumer.asset.allocation.base;

import com.iqb.etep.common.utils.Attr;

/**
 * 
 * Description: 资产分配常量类
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
public class AssetAllocationAttr extends Attr {

    /** 与饭饭金服接口交互 **/
    public class AssetTransferFFJFAttr {
        /** 推送给饭饭金服 **/
        public static final String PUSH_ASSET_TO_FFJS = "2";
        /** 推送给爱钱帮 **/
        public static final String PUSH_ASSET_TO_IQB = "1";
        /** 推送给交易所 **/
        public static final String PUSH_ASSET_TO_JYS = "3";
        /** 推送饭饭存管 **/
        public static final String PUSH_ASSET_TO_FFCG = "4";
        /** 推送饭团存管 **/
        public static final String PUSH_ASSET_TO_FTCG = "5";
        /** 推送给饭饭金服 **/
        public static final String PUSH_ASSET_TO_FFJS_NAME = "饭团金服";
        /** 推送给爱钱帮 **/
        public static final String PUSH_ASSET_TO_IQB_NAME = "爱钱帮";
        /** 推送给交易所 **/
        public static final String PUSH_ASSET_TO_JYS_NAME = "交易所";
    }

    /** 与爱钱帮接口交互 **/
    public class AssetTransferIQBAttr {
        /** key **/
        public static final String IQB_KEY_BASEDATA = "basedata";

        public static final String IQB_KEY_BORROWUSER = "borrower";
        public static final String IQB_KEY_BORROWINFO = "borrowInfo";
        public static final String IQB_KEY_GUARANTEEUSER = "guarantor";

        public static final String IQB_KEY_ORDER_ID = "order_id";
        public static final String IQB_KEY_ORDER_NAME = "orderName";
        public static final String IQB_KEY_BORROW_NAME = "borrow_name";
        public static final String IQB_KEY_BORROW_CODE = "borrow_code";
        public static final String IQB_KEY_BUSINESS_TYPES = "borrow_catalog";
        public static final String IQB_KEY_BORROW_MONEY = "borrow_money";
        public static final String IQB_KEY_BORROW_DURATION = "borrow_duration";
        public static final String IQB_KEY_REPAYMENT_TYPE = "repayment_type";
        public static final String IQB_KEY_REPAYMENTDAY_TYPE = "repaymentday_type";
        public static final String IQB_KEY_REPAYMENT_DAY = "repayment_day";
        public static final String IQB_KEY_IS_WITHHOLDING = "is_withholding";
        public static final String IQB_KEY_PLATFORM_CHARGE = "platform_charge";
        public static final String IQB_KEY_TOP_UP_RATE = "top_up_rate";
        public static final String IQB_KEY_IS_PUBLIC = "is_public";
        public static final String IQB_KEY_IS_PUSHFF = "is_pushff";
        public static final String IQB_KEY_REAL_REPAYMENT_DATE = "real_repayment_date";

        public static final String IQB_KEY_BORROW_MODEL = "borrow_model";
        public static final String IQB_KEY_BORROW_REALNAME = "borrow_realname";
        public static final String IQB_KEY_BORROW_IDCARD = "borrow_idcard";
        public static final String IQB_KEY_BORROW_PHONE = "borrow_phone";
        public static final String IQB_KEY_BORROW_BINDCARDID = "borrow_bindcardid";
        public static final String IQB_KEY_BORROW_MONEY_NAME = "borrow_money_name";
        public static final String IQB_KEY_BORROW_MONEY_IDCARD = "borrow_money_idcard";
        public static final String IQB_KEY_BORROW_MONEY_PHONE = "borrow_money_phone";
        public static final String IQB_KEY_BORROW_MONEY_SEX = "borrow_money_sex";
        public static final String IQB_KEY_BORROW_MONEY_AGE = "borrow_money_age";
        public static final String IQB_KEY_BORROW_MONEY_REGISTRATION = "borrow_money_registration";
        public static final String IQB_KEY_BORROW_MONEY_RECORD = "borrow_money_record";
        public static final String IQB_KEY_BORROW_MONEY_MARRIAGE = "borrow_money_marriage";
        public static final String IQB_KEY_WERE_BORROWED_NAME = "were_borrowed_name";
        public static final String IQB_KEY_WERE_BORROWED_IDCARD = "were_borrowed_idcard";
        public static final String IQB_KEY_WERE_BORROWED_PHONE = "were_borrowed_phone";
        public static final String IQB_KEY_WERE_BORROWED_SEX = "were_borrowed_sex";
        public static final String IQB_KEY_WERE_BORROWED_AGE = "were_borrowed_age";
        public static final String IQB_KEY_WERE_BORROWED_REGISTRATION = "were_borrowed_registration";
        public static final String IQB_KEY_WERE_BORROWED_RECORD = "were_borrowed_record";
        public static final String IQB_KEY_WERE_BORROWED_MARRIAGE = "were_borrowed_marriage";

        public static final String IQB_KEY_COLLATERALINFORMATION = "collateralInformation";
        public static final String IQB_KEY_BORROW_BRAND = "borrow_brand";
        public static final String IQB_KEY_BORROW_CARKINDS = "borrow_carkinds";
        public static final String IQB_KEY_PLATE = "plate";
        public static final String IQB_KEY_CARTYPE = "carType";
        public static final String IQB_KEY_REGISTDATE = "buy_time";
        public static final String IQB_KEY_CARNO = "vehicle_identification_code";
        public static final String IQB_KEY_MILEAGE = "borrow_kilometres";
        public static final String IQB_KEY_ASSESSPRICE = "borrow_referenceprice";
        public static final String IQB_KEY_IS_EXIT_LOAN = "isFenqi";

        public static final String IQB_KEY_VEHICLE_PROCEDURE = "project_info";
        public static final String IQB_KEY_IMG_LOGO = "img_logo";
        public static final String IQB_KEY_IMG_PHYSICAL = "img_physical";

        public static final String IQB_KEY_GR_INTRODUCTION = "gr_introduction";
        public static final String IQB_KEY_GR_USE = "gr_use";
        public static final String IQB_KEY_GR_WILL = "gr_will";
        public static final String IQB_KEY_GR_PROTECT = "gr_protect";
        public static final String IQB_KEY_GR_LEVEL = "gr_level";
        public static final String IQB_KEY_RISK_OPINION = "risk_opinion";

        public static final String IQB_KEY_RISKINFORMATION = "riskInformation";
        public static final String IQB_KEY_IQB_INFO = "iqb_info";

        public static final String IQB_KEY_GUARANTEECOMPANYINFORMATION = "guaranteeCompanyInformation";
        public static final String IQB_KEY_GR_NAME = "company_name";
        public static final String IQB_KEY_SIMPLE_NAME = "simple_name";
        public static final String IQB_KEY_GR_SOCIALCREDITCODE = "certificate";
        public static final String IQB_KEY_GR_ICREGCODE = "reg_id";
        public static final String IQB_KEY_GR_ORGANIZATIONCODE = "organization_code";
        public static final String IQB_KEY_GR_TAXCERTIFICATECODE = "tax_id";
        public static final String IQB_KEY_GR_PHONE = "gr_phone";
        public static final String IQB_KEY_GR_BINDCARDID = "gr_bindcardid";

        /** 爱钱帮接口返回数据字段 **/
        public static final String IQB_KEY_SIGN = "sign";
        public static final String IQB_KEY_SUCCESS = "success";
        public static final String IQB_VALUE_SUCCESS = "1";
        public static final String IQB_VALUE_FAIL = "0";
        public static final String IQB_KEY_ACCOUTTYPE = "accoutType";
        public static final String IQB_KEY_IDCARD = "idcard";
        public static final String IQB_KEY_HSBANKNO = "hsbankNo";
        public static final String IQB_KEY_BANKNO = "bankNo";
        public static final String IQB_KEY_MSGINFO = "msgInfo";
        public static final String IQB_KEY_MSG = "msg";

        public static final String IQB_KEY_UPLOADDATA = "/uploadData";

    }

    /** 资产推送状态 **/
    public class AssetPushStatus {

        /** 推送状态 1:开始 **/
        public static final int ASSET_PUSH_STATUS_START = 1;
        /** 推送状态 2:结束 **/
        public static final int ASSET_PUSH_STATUS_END = 2;
        /** 推送状态 3:异常 **/
        public static final int ASSET_PUSH_STATUS_ERROE = 3;
    }

    /** 蒲公英行商户NO **/
    public static String PGY_MERCHANT_NO = "pgy";
    /** 资金来源-爱钱帮 **/
    public static String FUND_SOURCE_IQB = "1";
}
