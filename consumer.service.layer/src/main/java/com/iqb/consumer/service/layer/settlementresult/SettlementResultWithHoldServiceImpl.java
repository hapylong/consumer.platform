package com.iqb.consumer.service.layer.settlementresult;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.BuckleConfig;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.HttpUtils;
import com.iqb.consumer.common.utils.RandomUtils;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.pay.OverDueBillPojo;
import com.iqb.consumer.data.layer.bean.pay.SettlementCenterBuckleCallbackRequestMessage;
import com.iqb.consumer.data.layer.bean.settlementresult.BillWithHoldBean;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultBean;
import com.iqb.consumer.data.layer.bean.settlementresult.SettlementResultWithHoldBean;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.pay.PayManager;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.data.layer.biz.settlementresult.SettlementResultWithHoldBiz;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.platform.security.client.ApplicationSecurityClient;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年1月7日下午3:10:12 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class SettlementResultWithHoldServiceImpl implements SettlementResultWithHoldService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(SettlementResultWithHoldServiceImpl.class);
    @Resource
    private SettlementResultWithHoldBiz settlementResultWithHoldBiz;
    @Autowired
    private MerchantBeanBiz merchantBiz;
    @Autowired
    private SysUserSession sysUserSession;
    @Resource
    private BuckleConfig buckleConfig;
    @Autowired
    private DictService dictServiceImpl;
    @Autowired
    private PaymentLogBiz paymentLogBiz;
    @Autowired
    private PayManager payManager;

    public static final int NOT_SEND = 1;
    public static final int SEND = 2;
    public static final String TYPE_REFUND = "0"; // 还款

    /**
     * 
     * Description:显示账单代扣列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月7日
     */
    public PageInfo<SettlementResultWithHoldBean> listSettlementResultWithHold(JSONObject objs) {
        logger.debug("---根据条件获取账单代扣信息开始---{}", objs);
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        logger.debug("---根据条件获取账单代扣信息结束---{}", objs);

        List<SettlementResultWithHoldBean> resultList = settlementResultWithHoldBiz.listSettlementResult(objs);
        return new PageInfo<>(resultList);

    }

    // 获取商户权限的Objs
    private JSONObject getMerchLimitObject(JSONObject objs) {
        objs.put("status", objs.getString("status"));
        // 权限树修改
        String merchNames = objs.getString("merchantNo");// 商户逗号分隔字段
        if (merchNames == null || "".equals(merchNames) || "全部商户".equals(merchNames)) {
            if ("1".equals(objs.getString("id"))) {
                objs.put("merList", null);
            } else {
                List<MerchantBean> merchList = getAllMerListByOrgCode(objs);
                if (merchList == null || merchList.size() == 0) {
                    return null;
                }
                objs.put("merList", merchList);
            }
        } else {
            objs.put("merchNames", merchNames);
            List<MerchantBean> merchList = merchantBiz.getMerCodeByMerSortNameList(objs);
            objs.put("merList", merchList);
        }

        return objs;
    }

    // 获取登录商户下所有子商户列表
    private List<MerchantBean> getAllMerListByOrgCode(JSONObject objs) {
        String orgCode = sysUserSession.getOrgCode();
        objs.put("id", orgCode);
        List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
        return list;
    }

    /**
     * 导出还款代扣信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年1月7日
     */
    @Override
    public String exportSettlementResultList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            objs = getMerchLimitObject(objs);

            List<SettlementResultWithHoldBean> resultList =
                    settlementResultWithHoldBiz.exportSettlementResultList(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportOrderList(resultList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=orderInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出订单列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    // 导出
    private HSSFWorkbook exportOrderList(List<SettlementResultWithHoldBean> list) {
        String[] excelHeader =
        {"商户", "订单号", "姓名", "手机号", "期数", "总期数", "最后还款日", "月供", "罚息", "本期应还", "逾期天数", "账单状态", "划扣状态",
                "失败原因"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("还款代扣信息页");

        HSSFRow row = sheet.createRow(0);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFCellStyle amtStyle = wb.createCellStyle(); // 货币样式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        amtStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            SettlementResultWithHoldBean settlementResultBean = (SettlementResultWithHoldBean) list.get(i);

            row.createCell(0).setCellValue(settlementResultBean.getMerchantName());
            row.createCell(1).setCellValue(settlementResultBean.getOrderId());
            row.createCell(2).setCellValue(settlementResultBean.getRealName());
            row.createCell(3).setCellValue(settlementResultBean.getRegId());
            row.createCell(4).setCellValue(settlementResultBean.getRepayNo());
            row.createCell(5).setCellValue(settlementResultBean.getOrderItems());
            row.createCell(6).setCellValue(
                    DateUtil.getDateString(settlementResultBean.getLastRepayDate(), DateUtil.SHORT_DATE_FORMAT_CN));
            row.createCell(7).setCellValue(
                    settlementResultBean.getMonthInterest() == null ? 0 : settlementResultBean.getMonthInterest()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(8).setCellValue(
                    settlementResultBean.getOverdueInterest() == null ? 0 : settlementResultBean.getOverdueInterest()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(9).setCellValue(
                    settlementResultBean.getCurRepayAmt() == null ? 0 : settlementResultBean.getCurRepayAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(10).setCellValue(settlementResultBean.getOverdueDays());
            row.createCell(11).setCellValue(settlementResultBean.getBillStatus());

            row.createCell(12).setCellValue(doStatus(settlementResultBean.getStatus()));
            row.createCell(13).setCellValue(settlementResultBean.getDescribe());
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 商户
        sheet.setColumnWidth(1, 4000);// 订单号
        sheet.setColumnWidth(2, 3000);// 姓名
        sheet.setColumnWidth(3, 3000);// 手机号
        sheet.setColumnWidth(4, 1000);// 期数
        sheet.setColumnWidth(5, 1000);// 总期数
        sheet.setColumnWidth(6, 2000);// 最后还款日
        sheet.setColumnWidth(7, 2000);// 月供
        sheet.setColumnWidth(8, 2000);// 罚息
        sheet.setColumnWidth(9, 2000);// 本期应还
        sheet.setColumnWidth(10, 1000);// 逾期天数
        sheet.setColumnWidth(11, 3000);// 账单状态
        sheet.setColumnWidth(12, 3000);// 划扣状态
        sheet.setColumnWidth(13, 10000);// 失败原因

        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    /**
     * 还款代扣
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年1月9日
     */
    @Override
    public String billWithHold(JSONObject objs) {
        /**
         * 还款代扣逻辑： 1:从前端获取list<Map<String,String>> list中封装orderId，repayNo
         * 2：查询inst_settlementresult_withhold inst_user inst_bankcard组装发送数据 3：调用结算中心代扣接口循环发送数据
         */
        int result = 0;
        JSONArray array = objs.getJSONArray("params");
        if (array != null && array.size() > 0) {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = (JSONObject) array.get(i);
                params.put("orderId", obj.getString("orderId"));
                params.put("repayNo", obj.getIntValue("repayNo"));
                BillWithHoldBean billWithHoldBean = settlementResultWithHoldBiz.selectBillWithHoldInfo(params);
                if (billWithHoldBean != null) {
                    billWithHoldBean.setTradeNo(generateTradeNo(obj.getString("orderId"),
                            String.valueOf(obj.getIntValue("repayNo"))));
                    billWithHoldBean.setTradeType(TYPE_REFUND);
                    billWithHoldBean.setNotifyUrl(buckleConfig.getWithHoldCallBackUrl());
                    Map<String, String> billMap = convertBeanToMap(billWithHoldBean);
                    result += call(billWithHoldBean, billMap);
                }
            }
            if (result == array.size()) {
                return "success";
            } else {
                return "fail";
            }
        }
        return "success";
    }

    /**
     * 
     * Description:生成
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    public static String generateTradeNo(String orderId, String repayNo) {
        return orderId + "@" + repayNo + "@" + RandomUtils.randomInt(8);
    }

    /**
     * 
     * Description:将账单代扣信息转化为结算中心可用的map
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    private Map<String, String> convertBeanToMap(BillWithHoldBean billWithHoldBean) {
        Map<String, String> data = null;

        String BUSINESS_CHANNAL = buckleConfig.getIqbBuckleChannal();
        String BUCKLE_PRIVATE_KEY = buckleConfig.getIqbBucklePrivateKey();
        String CERT_PATH = buckleConfig.getCertPath();
        String APP_SECRET = buckleConfig.getAppSecret();

        data = ApplicationSecurityClient.encryption(BUSINESS_CHANNAL, APP_SECRET, CERT_PATH,
                BUCKLE_PRIVATE_KEY, billWithHoldBean.convertMap());

        return data;
    }

    /**
     * 
     * Description:调用结算系统进行代扣
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月9日
     */
    private int call(BillWithHoldBean billWithHoldBean, Map<String, String> map) {
        logger.debug("---调用结算系统完成代扣开始---,代扣参数---{}", map);
        String response = null;
        int result = 0;
        try {
            response = HttpUtils.post(buckleConfig.getIqbBuckleUrl(), map);
        } catch (IOException e) {
            logger.error("---还款代扣调用结算系统异常---{}", e);
            Map<String, Object> params = new HashMap<>();
            params.put("id", billWithHoldBean.getId());
            params.put("describe", "调用结算中心接口异常");
            params.put("status", NOT_SEND);
            result = settlementResultWithHoldBiz.updateSettlementById(params);
        }
        if (!StringUtil.isNull(response)) {
            logger.debug("---还款代扣调用结算系统返回信息---{}", response);
            Map<String, Object> params = new HashMap<>();
            params.put("id", billWithHoldBean.getId());
            params.put("describe", "结算中心返回消息【请求后】：" + JSON.parseObject(response).getString("msg"));
            params.put("status", SEND);
            params.put("sendTime", new Date());
            params.put("tradeNo", billWithHoldBean.getTradeNo());
            result = settlementResultWithHoldBiz.updateSettlementById(params);
        }
        return result;
    }

    /**
     * 还款代扣回调接口
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年1月9日
     */
    @Override
    public int callback(JSONObject objs) {
        logger.debug("---还款代扣---结算中心回调---{}", objs);
        int result = 0;
        SettlementCenterBuckleCallbackRequestMessage scbc = null;
        try {
            scbc = JSONObject.
                    toJavaObject(objs, SettlementCenterBuckleCallbackRequestMessage.class);
            if (scbc == null || !scbc.checkRequest()) {
                throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error("---还款代扣---结算中心回调报错--- :", e);
        }
        String orderId = OverDueBillPojo.getOidByTradeNo(scbc.getTradeNo());
        if (paymentLogBiz.getPayLogByTNo(scbc.getTradeNo()) != null) {
            return result;
        }
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        try {
            switch (scbc.getStatus()) {
                case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_ALL:
                    result = payManager.callBackSAForWithHold(scbc, openId, orderId);
                    break;
                case SettlementCenterBuckleCallbackRequestMessage.SUCCESS_PART:
                    result = payManager.callBackSPForWithHold(scbc, openId, orderId);
                    break;
                case SettlementCenterBuckleCallbackRequestMessage.FAIL:
                    result = payManager.callBackFail(scbc, openId, orderId);
                    break;
                default:
                    throw new GenerallyException(Reason.UNKNOW_TYPE, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error("---还款代扣---结算中心回调报错--- :", e);
        }
        return result;
    }

    private String doStatus(int status) {
        String str = "未发送";
        if (status == 1) {
            str = "未发送";
        } else if (status == 2) {
            str = "发送成功";
        } else if (status == 3) {
            str = "划扣成功";
        } else if (status == 4) {
            str = "划扣部分成功";
        } else if (status == 5) {
            str = "划扣失败";
        }
        return str;
    }

    /**
     * 
     * Description:合并自动代扣与手动代扣信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年1月15日
     */
    @SuppressWarnings("unused")
    private List<SettlementResultWithHoldBean> mergeList(List<SettlementResultWithHoldBean> list,
            List<SettlementResultBean> setList, List<SettlementResultWithHoldBean> resultList) {
        if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(setList)) {
            for (SettlementResultWithHoldBean bean : list) {
                Iterator<SettlementResultBean> iter = setList.iterator();
                String orderId = bean.getOrderId();
                int repayNo = bean.getRepayNo();
                while (iter.hasNext()) {
                    SettlementResultBean setBean = iter.next();
                    if (orderId.equals(setBean.getOrderId()) && repayNo == setBean.getRepayNo()) {
                        bean.setStatus(setBean.getStatus());
                    }
                }
            }
        } else if (!CollectionUtils.isEmpty(resultList)) {
            list = resultList;
        }
        return list;
    }
}
