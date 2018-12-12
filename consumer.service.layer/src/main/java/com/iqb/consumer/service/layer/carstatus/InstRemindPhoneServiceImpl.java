package com.iqb.consumer.service.layer.carstatus;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.exception.ApiReturnInfo;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.data.layer.bean.carstatus.entity.InstManageCarInfoEntity;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.CgetCarStatusInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindPhoneBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.InstRemindRecordBean;
import com.iqb.consumer.data.layer.bean.carstatus.pojo.ManageCarInfoBean;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.carstatus.CarStatusManager;
import com.iqb.consumer.data.layer.biz.carstatus.InstRemindPhoneBiz;
import com.iqb.consumer.data.layer.biz.carstatus.InstRemindRecordBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.common.utils.https.SendHttpsUtil;

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
 * 2018年4月26日下午2:44:38 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class InstRemindPhoneServiceImpl implements InstRemindPhoneService {
    protected static Logger logger = LoggerFactory.getLogger(InstRemindPhoneServiceImpl.class);
    @Autowired
    private InstRemindPhoneBiz instRemindPhoneBiz;
    @Autowired
    private InstRemindRecordBiz instRemindRecordBiz;
    @Autowired
    private MerchantBeanBiz merchantBiz;
    @Autowired
    private SysUserSession sysUserSession;
    @Autowired
    private com.iqb.consumer.data.layer.biz.conf.WFConfigBiz wfConfigBiz;
    @Autowired
    private OrderBiz orderBiz;
    @Autowired
    private DictService dictServiceImpl;
    @Resource
    private ConsumerConfig consumerConfig;
    @Resource
    private EncryptUtils encryptUtils;
    @Autowired
    private CarStatusManager carStatusManager;

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月26日
     */
    @Override
    public PageInfo<InstRemindPhoneBean> selectInstRemindPhoneList(JSONObject objs) {
        // 获取商户权限的Objs
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
        if (!StringUtil.isNull(merchNames)) {
            if (!"全部商户".equals(merchNames)) {
                objs = getMerchLimitObject(objs);
            }
        }
        List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList(objs);
        return new PageInfo<>(instRemindPhoneList);
    }

    public PageInfo<InstRemindPhoneBean> selectInstRemindPhoneList2(JSONObject objs) {

        List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList2(objs);
        return new PageInfo<>(instRemindPhoneList);
    }

    /**
     * 
     * @param params
     * @return
     * @Author chengzhen Create Date: 2018年4月26日 17:07:25
     */
    @SuppressWarnings({"rawtypes"})
    private int startWF(InstRemindRecordBean instRemindRecordBean) throws IqbException {
        LinkedHashMap responseMap = null;
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(instRemindRecordBean.getOrderId());
        MerchantBean merchantBean = merchantBiz.getMerByMerNo(orderBean.getMerchantNo());
        // 获取wfconfig文件
        WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 10);
        String procBizMemo =
                "补充客户信息," + instRemindRecordBean.getOrderId();// 摘要信息
        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procDefKey", wfConfig.getProcDefKey());
        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1");
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", sysUserSession.getUserCode());
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");
        Map<String, Object> hmBizData = new HashMap<>();
        hmBizData.put("procBizId", orderBean.getOrderId() + "-" + instRemindRecordBean.getCurItems() + "-"
                + instRemindRecordBean.getFlag());
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", merchantBean.getId() + "");
        hmBizData.put("procBizMemo", procBizMemo);
        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);
        String url = wfConfig.getStartWfurl();
        // 发送Https请求
        logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        try {
            responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        } catch (Exception e) {
            logger.error("---电话提醒启动工作流报错---{}", e);
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("调用工作流接口返回信息：{}" + responseMap);
        logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        if (responseMap.get("retCode").toString().equals("00000000")) {
            // 更新inst_remind_iphone状态为处理中
            InstRemindPhoneBean instRemindPhoneBean = instRemindPhoneBiz.selectInstRemindPhoneOne(instRemindRecordBean);
            if (instRemindPhoneBean != null) {
                instRemindPhoneBean.setStatus("3");
                instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
            }

        }
        return result;
    }

    /**
     * 根据订单号 期数查询电话提醒列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月25日
     */
    @Override
    public PageInfo<InstRemindRecordBean> selectInstRemindRecordList(JSONObject objs) {
        // 获取商户权限的Objs
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
        if (!StringUtil.isNull(merchNames)) {
            if (!"全部商户".equals(merchNames)) {
                objs = getMerchLimitObject(objs);
            }
        }
        List<InstRemindRecordBean> instRemindRecordList = instRemindRecordBiz.selectInstRemindRecordList(objs);
        return new PageInfo<>(instRemindRecordList);
    }

    /**
     * 
     * 插入电话信息信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年4月26日
     */
    @Override
    public int insertInstRemindRecordInfo(InstRemindRecordBean bean) throws IqbException {
        bean.setProcessUser(sysUserSession.getUserCode());
        if ((bean.getFlag() == 1 && "3".equals(bean.getDealReason()))
                || (bean.getFlag() == 2 && "3".equals(bean.getMobileDealOpinion()))) {
            // 设置电话提醒、电催处理状态
            InstRemindPhoneBean instRemindPhoneBean = new InstRemindPhoneBean();
            instRemindPhoneBean.setOrderId(bean.getOrderId());
            instRemindPhoneBean.setCurItems(bean.getCurItems());
            instRemindPhoneBean.setStatus("2");
            instRemindPhoneBean.setFlag(bean.getFlag());
            instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
            // 启动工作流
            startWF(bean);
        }
        // 通话结果正常
        if ("1".equals(bean.getTelRecord())) {
            // 设置电话提醒、电催处理状态
            InstRemindPhoneBean instRemindPhoneBean = new InstRemindPhoneBean();
            instRemindPhoneBean.setOrderId(bean.getOrderId());
            instRemindPhoneBean.setCurItems(bean.getCurItems());
            instRemindPhoneBean.setStatus("3");
            instRemindPhoneBean.setFlag(bean.getFlag());
            instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
        }
        // 处理意见为保存任务时;电话提醒 电催状态为处理中
        if ("1".equals(bean.getDealReason()) || "1".equals(bean.getMobileDealOpinion())) {
            // 设置电话提醒、电催处理状态
            InstRemindPhoneBean instRemindPhoneBean = new InstRemindPhoneBean();
            instRemindPhoneBean.setOrderId(bean.getOrderId());
            instRemindPhoneBean.setCurItems(bean.getCurItems());
            instRemindPhoneBean.setStatus("2");
            instRemindPhoneBean.setFlag(bean.getFlag());
            instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
            // 处理意见为保存任务时;电话提醒 电催状态为处理中
        }
        // 处理意见为转外催时;电话提醒 电催状态为处理中,并且启动电话提醒转贷后流程或电催转贷后流程
        if ("2".equals(bean.getDealReason()) || "2".equals(bean.getMobileDealOpinion())) {
            // 启动电话提醒转贷后 电催转贷后流程

            if (bean.getFlag() == 1) {
                startWFRemind(bean);
            } else {
                startWFUrge(bean);
            }
        }
        return instRemindRecordBiz.insertInstRemindRecordInfo(bean);
    }

    /**
     * 导出电话提醒列表信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月27日
     */
    @Override
    public String exportInstRemindPhoneList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
            if (!StringUtil.isNull(merchNames)) {
                if (!"全部商户".equals(merchNames)) {
                    objs = getMerchLimitObject(objs);
                }
            }
            objs.put("isPageHelper", false);
            List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportInstRemindPhoneList(instRemindPhoneList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=instRemindPhoneInfo-" + DateTools.getYmdhmsTime() + ".xls";
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

    /**
     * 导出电话提醒列表信息Total
     * 
     * @param params
     * @return
     * @Author chengzhen Create Date: 2018年5月10日 18:54:41
     */
    @Override
    public String exportInstRemindPhoneList3(JSONObject objs, HttpServletResponse response) {
        try {
            objs.put("isPageHelper", false);
            List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList2(objs);
            logger.info("--------导出--{}", JSONObject.toJSONString(instRemindPhoneList));
            // 2.导出excel表格
            HSSFWorkbook workbook = exportInstRemindPhoneList3(instRemindPhoneList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=instRemindPhoneInfo-" + DateTools.getYmdhmsTime() + ".xls";
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

    /**
     * 导出电催列表信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月27日
     */
    @Override
    public String exportInstRemindPhoneList2(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
            if (!StringUtil.isNull(merchNames)) {
                if (!"全部商户".equals(merchNames)) {
                    objs = getMerchLimitObject(objs);
                }
            }
            objs.put("isPageHelper", false);
            List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportInstRemindPhoneList2(instRemindPhoneList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=instRemindPhoneInfo-" + DateTools.getYmdhmsTime() + ".xls";
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

    /**
     * 导出电催列表信息Total
     * 
     * @param params
     * @return
     * @Author chengzhen Create Date: 2018年5月10日 18:54:28
     */
    @Override
    public String exportInstRemindPhoneList4(JSONObject objs, HttpServletResponse response) {
        try {
            objs.put("isPageHelper", false);
            List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList2(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportInstRemindPhoneList4(instRemindPhoneList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=instRemindPhoneInfo-" + DateTools.getYmdhmsTime() + ".xls";
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

    /**
     * 
     * Description:获取商户列表信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月20日
     */
    private JSONObject getMerchLimitObject(JSONObject objs) {
        objs.put("status", objs.getString("status"));
        // 权限树修改
        String merchNames = objs.getString("merchNames");// 商户逗号分隔字段
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
     * 
     * 根据订单号 回显贷后客户信息流程页面数据
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 14:11:50
     */
    public InstRemindRecordBean queryCustomerInfo(JSONObject objs) {
        return instRemindRecordBiz.queryCustomerInfo(objs);
    }

    /**
     * 贷后客户信息流程页面点击审核通过前执行该接口,修改状态为未处理
     * 
     * 
     * @author chengzhen
     * @param objs
     * @param request
     * @return 2018年4月27日 16:03:02
     */
    public void updateInstRemindPhoneBean(JSONObject objs) {
        InstRemindPhoneBean instRemindPhoneBean = new InstRemindPhoneBean();
        instRemindPhoneBean.setOrderId(objs.get("orderId").toString());
        instRemindPhoneBean.setCurItems(Integer.parseInt(objs.get("curItems").toString()));
        if (objs.get("flag") != null || !"".equals(objs.get("flag").toString())) {

        }
        instRemindPhoneBean.setFlag(Integer.parseInt(objs.get("flag").toString()));
        instRemindPhoneBean.setStatus("1");
        instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
    }

    // 导出
    private HSSFWorkbook exportInstRemindPhoneList(List<InstRemindPhoneBean> list) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "商户名称", "期数", "总期数", "账单状态", "最后还款日", "本期应还", "预计逾期金额/天", "通话结果", "失败原因", "处理意见", "提醒状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("电话提醒");

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
            InstRemindPhoneBean instRemindPhoneBean = (InstRemindPhoneBean) list.get(i);

            row.createCell(0).setCellValue(instRemindPhoneBean.getOrderId());
            row.createCell(1).setCellValue(instRemindPhoneBean.getRealName());
            row.createCell(2).setCellValue(instRemindPhoneBean.getRegId());
            row.createCell(3).setCellValue(instRemindPhoneBean.getMerchantName());
            row.createCell(4).setCellValue(instRemindPhoneBean.getCurItems());
            row.createCell(5).setCellValue(instRemindPhoneBean.getOrderItems());
            row.createCell(6).setCellValue(convertBillStatus(instRemindPhoneBean.getBillInfoStatus()));

            row.createCell(7).setCellValue(instRemindPhoneBean.getLastRepayDate());
            row.createCell(8).setCellValue(
                    instRemindPhoneBean.getCurRepayAmt() == null ? 0 : instRemindPhoneBean.getCurRepayAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(9).setCellValue(
                    instRemindPhoneBean.getPerOverdueAmt() == null ? 0 : instRemindPhoneBean.getPerOverdueAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(10).setCellValue(instRemindPhoneBean.getTelRecord());
            row.createCell(11).setCellValue(instRemindPhoneBean.getFailReason());
            row.createCell(12).setCellValue(instRemindPhoneBean.getDealOpinion());
            row.createCell(13).setCellValue(instRemindPhoneBean.getStatus());
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 2000);// 姓名
        sheet.setColumnWidth(2, 3500);// 手机号
        sheet.setColumnWidth(3, 10000);// 商户名
        sheet.setColumnWidth(4, 2000);// 期数
        sheet.setColumnWidth(5, 2000);// 总期数
        sheet.setColumnWidth(6, 3500);// 最后还款日

        sheet.setColumnWidth(7, 5000);// 本期应还
        sheet.setColumnWidth(8, 5000);// 预计逾期金额/天
        sheet.setColumnWidth(9, 5000);// 通话结果
        sheet.setColumnWidth(10, 5000);// 失败原因
        sheet.setColumnWidth(11, 5000);// 处理意见
        sheet.setColumnWidth(12, 5000);// 提醒状态
        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    private HSSFWorkbook exportInstRemindPhoneList3(List<InstRemindPhoneBean> list) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "机构名称", "期数", "总期数", "账单状态", "最后还款日", "本期应还", "预计逾期金额/天", "通话结果", "失败原因", "处理意见",
                "提醒状态", "备注", "处理时间"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("电话提醒");

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
            InstRemindPhoneBean instRemindPhoneBean = (InstRemindPhoneBean) list.get(i);

            row.createCell(0).setCellValue(instRemindPhoneBean.getOrderId());
            row.createCell(1).setCellValue(instRemindPhoneBean.getRealName());
            row.createCell(2).setCellValue(instRemindPhoneBean.getRegId());
            row.createCell(3).setCellValue(instRemindPhoneBean.getMerchantName());
            row.createCell(4).setCellValue(instRemindPhoneBean.getCurItems());
            row.createCell(5).setCellValue(instRemindPhoneBean.getOrderItems());
            row.createCell(6).setCellValue(convertBillStatus(instRemindPhoneBean.getBillInfoStatus()));

            row.createCell(7).setCellValue(instRemindPhoneBean.getLastRepayDate());
            row.createCell(8).setCellValue(
                    instRemindPhoneBean.getCurRepayAmt() == null ? 0 : instRemindPhoneBean.getCurRepayAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(9).setCellValue(
                    instRemindPhoneBean.getPerOverdueAmt() == null ? 0 : instRemindPhoneBean.getPerOverdueAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(10).setCellValue(instRemindPhoneBean.getTelRecord());
            row.createCell(11).setCellValue(instRemindPhoneBean.getFailReason());
            row.createCell(12).setCellValue(instRemindPhoneBean.getDealOpinion());
            row.createCell(13).setCellValue(instRemindPhoneBean.getStatus());
            row.createCell(14).setCellValue(instRemindPhoneBean.getRemark());
            row.createCell(15).setCellValue(instRemindPhoneBean.getProcessTime());
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 2000);// 姓名
        sheet.setColumnWidth(2, 3500);// 手机号
        sheet.setColumnWidth(3, 10000);// 商户名
        sheet.setColumnWidth(4, 2000);// 期数
        sheet.setColumnWidth(5, 2000);// 总期数
        sheet.setColumnWidth(6, 3500);// 最后还款日

        sheet.setColumnWidth(7, 5000);// 本期应还
        sheet.setColumnWidth(8, 5000);// 预计逾期金额/天
        sheet.setColumnWidth(9, 5000);// 通话结果
        sheet.setColumnWidth(10, 5000);// 失败原因
        sheet.setColumnWidth(11, 5000);// 处理意见
        sheet.setColumnWidth(12, 5000);// 提醒状态
        sheet.setColumnWidth(13, 5000);// 提醒状态
        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    // 导出
    private HSSFWorkbook exportInstRemindPhoneList2(List<InstRemindPhoneBean> list) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "商户", "期数", "总期数", "账单状态", "最后还款日", "逾期天数", "本期应还", "罚息", "违约金", "电催结果", "处理意见", "电催状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("电催");

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
            InstRemindPhoneBean instRemindPhoneBean = (InstRemindPhoneBean) list.get(i);

            row.createCell(0).setCellValue(instRemindPhoneBean.getOrderId());
            row.createCell(1).setCellValue(instRemindPhoneBean.getRealName());
            row.createCell(2).setCellValue(instRemindPhoneBean.getRegId());
            row.createCell(3).setCellValue(instRemindPhoneBean.getMerchantName());
            row.createCell(4).setCellValue(instRemindPhoneBean.getCurItems());
            row.createCell(5).setCellValue(instRemindPhoneBean.getOrderItems());
            row.createCell(6).setCellValue(convertBillStatus(instRemindPhoneBean.getBillInfoStatus()));
            row.createCell(7).setCellValue(instRemindPhoneBean.getLastRepayDate());
            row.createCell(8).setCellValue(instRemindPhoneBean.getOverdueDays());
            row.createCell(9).setCellValue(
                    instRemindPhoneBean.getCurRepayAmt() == null ? 0 : instRemindPhoneBean.getCurRepayAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(10).setCellValue(
                    instRemindPhoneBean.getPerOverdueAmt() == null ? 0 : instRemindPhoneBean.getPerOverdueAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(11).setCellValue(
                    instRemindPhoneBean.getOverdueAmt() == null ? 0 : instRemindPhoneBean.getOverdueAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(12).setCellValue(instRemindPhoneBean.getMobileCollection());
            row.createCell(13).setCellValue(instRemindPhoneBean.getMobileDealOpinion());
            row.createCell(14).setCellValue(instRemindPhoneBean.getStatus());
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 2000);// 姓名
        sheet.setColumnWidth(2, 3500);// 手机号
        sheet.setColumnWidth(3, 10000);// 商户名
        sheet.setColumnWidth(4, 2000);// 期数
        sheet.setColumnWidth(5, 2000);// 总期数
        sheet.setColumnWidth(6, 3500);// 最后还款日

        sheet.setColumnWidth(7, 5000);// 本期应还
        sheet.setColumnWidth(8, 5000);// 预计逾期金额/天
        sheet.setColumnWidth(9, 5000);// 通话结果
        sheet.setColumnWidth(10, 5000);// 失败原因
        sheet.setColumnWidth(11, 5000);// 处理意见
        sheet.setColumnWidth(12, 5000);// 提醒状态
        sheet.setColumnWidth(13, 5000);// 提醒状态
        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    // 导出
    private HSSFWorkbook exportInstRemindPhoneList4(List<InstRemindPhoneBean> list) {
        String[] excelHeader =
        {"订单号", "姓名", "手机号", "商户", "期数", "总期数", "账单状态", "最后还款日", "逾期天数", "本期应还", "罚息", "违约金", "电催结果", "处理意见",
                "电催状态", "备注", "处理时间"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("电催");

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
            InstRemindPhoneBean instRemindPhoneBean = (InstRemindPhoneBean) list.get(i);

            row.createCell(0).setCellValue(instRemindPhoneBean.getOrderId());
            row.createCell(1).setCellValue(instRemindPhoneBean.getRealName());
            row.createCell(2).setCellValue(instRemindPhoneBean.getRegId());
            row.createCell(3).setCellValue(instRemindPhoneBean.getMerchantName());
            row.createCell(4).setCellValue(instRemindPhoneBean.getCurItems());
            row.createCell(5).setCellValue(instRemindPhoneBean.getOrderItems());
            row.createCell(6).setCellValue(convertBillStatus(instRemindPhoneBean.getBillInfoStatus()));
            row.createCell(7).setCellValue(instRemindPhoneBean.getLastRepayDate());
            row.createCell(8).setCellValue(instRemindPhoneBean.getOverdueDays());
            row.createCell(9).setCellValue(
                    instRemindPhoneBean.getCurRepayAmt() == null ? 0 : instRemindPhoneBean.getCurRepayAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(10).setCellValue(
                    instRemindPhoneBean.getPerOverdueAmt() == null ? 0 : instRemindPhoneBean.getPerOverdueAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(11).setCellValue(
                    instRemindPhoneBean.getOverdueAmt() == null ? 0 : instRemindPhoneBean.getOverdueAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(12).setCellValue(instRemindPhoneBean.getMobileCollection());
            row.createCell(13).setCellValue(instRemindPhoneBean.getMobileDealOpinion());
            row.createCell(14).setCellValue(instRemindPhoneBean.getStatus());
            row.createCell(15).setCellValue(instRemindPhoneBean.getRemark());
            row.createCell(16).setCellValue(instRemindPhoneBean.getProcessTime());
        }
        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 2000);// 姓名
        sheet.setColumnWidth(2, 3500);// 手机号
        sheet.setColumnWidth(3, 10000);// 商户名
        sheet.setColumnWidth(4, 2000);// 期数
        sheet.setColumnWidth(5, 2000);// 总期数
        sheet.setColumnWidth(6, 3500);// 最后还款日

        sheet.setColumnWidth(7, 5000);// 本期应还
        sheet.setColumnWidth(8, 5000);// 预计逾期金额/天
        sheet.setColumnWidth(9, 5000);// 通话结果
        sheet.setColumnWidth(10, 5000);// 失败原因
        sheet.setColumnWidth(11, 5000);// 处理意见
        sheet.setColumnWidth(12, 5000);// 提醒状态
        sheet.setColumnWidth(13, 5000);// 提醒状态
        sheet.setColumnWidth(14, 5000);// 提醒状态
        for (int j = 5; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }
        return wb;
    }

    @Override
    public PageInfo<CgetCarStatusInfoResponseMessage> afterLoanList(JSONObject requestMessage) {
        return new PageInfo<>(instRemindPhoneBiz.afterLoanList(requestMessage));
    }

    @Override
    public OrderBean queryOrderInfoByOrderId(JSONObject requestMessage) {
        return instRemindPhoneBiz.queryOrderInfoByOrderId(requestMessage);
    }

    @Override
    public void insertManagecarInfo(JSONObject requestMessage) {
        instRemindPhoneBiz.insertManagecarInfo(requestMessage);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<Map<String, Object>> queryBillIfoByOId(String orderId) {
        // 发送账户系统查询用户代偿信息
        JSONObject params = new JSONObject();
        String openId = dictServiceImpl.getOpenIdByOrderId(orderId);
        params.put("openId", openId);
        params.put("orderId", orderId);
        params.put("pageNum", 1);
        params.put("numPerPage", 100);
        String resultStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceAllBillGetPagelistUrl(),
                encryptUtils.encrypt(params));
        if (StringUtil.isNull(resultStr)) {
            return null;
        }
        Map<String, Object> retMap = JSONObject.parseObject(resultStr);
        Map<String, Object> res = (Map<String, Object>) retMap.get("result");
        if (res == null) {
            return null;
        }
        List<Map<String, Object>> recordList = (List<Map<String, Object>>) res.get("recordList");
        for (Map<String, Object> map : recordList) {
            String orderId1 = map.get("orderId").toString();
            String c = map.get("repayNo").toString();
            JSONObject objs = new JSONObject();
            objs.put("orderId", orderId1);
            objs.put("curItems", c);
            objs.put("flag", "2");
            InstRemindRecordBean queryCustomerInfo = instRemindRecordBiz.queryCustomerInfo(objs);
            if (queryCustomerInfo == null) {
                map.put("mobileCollection", "");
                map.put("mobileDealOpinion", "");
                map.put("remark", "");
            } else {
                map.put("mobileCollection",
                        queryCustomerInfo.getMobileCollection() == null ? "" : queryCustomerInfo.getMobileCollection());
                map.put("mobileDealOpinion",
                        queryCustomerInfo.getMobileDealOpinion() == null ? "" : queryCustomerInfo
                                .getMobileDealOpinion());
                map.put("remark", queryCustomerInfo.getRemark() == null ? "" : queryCustomerInfo.getRemark());
            }
        }
        return recordList;
    }

    @Override
    public int saveAfterRiskAndStartWf(JSONObject objs) throws IqbException {
        String repaymentFlag = objs.getString("repaymentFlag");
        if (!StringUtil.isNull(repaymentFlag) && repaymentFlag.equals("2")) {
            startWF2(objs);
        } else {
            objs.put("processStatus", "30");
            return instRemindPhoneBiz.updateManagerCarInfo(objs);
        }
        return 0;
    }

    /**
     * 
     * @param params 开启贷后流程
     * @return
     * @Author chengzhen Create Date: 2018年4月26日 17:07:25
     */
    @SuppressWarnings({"rawtypes"})
    private int startWF2(JSONObject objs) throws IqbException {
        LinkedHashMap responseMap = null;
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(objs.get("orderId").toString());
        MerchantBean merchantBean = merchantBiz.getMerByMerNo(orderBean.getMerchantNo());
        // 获取wfconfig文件
        WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 30);
        String procBizMemo =
                "贷后处理," + objs.get("orderId");// 摘要信息
        Map<String, Object> hmProcData = new HashMap<>();
        hmProcData.put("procDefKey", wfConfig.getProcDefKey());
        Map<String, Object> hmVariables = new HashMap<>();
        hmVariables.put("procAuthType", "1");
        hmVariables.put("procTokenUser", wfConfig.getTokenUser());
        hmVariables.put("procTokenPass", wfConfig.getTokenPass());
        hmVariables.put("procTaskUser", sysUserSession.getUserCode());
        hmVariables.put("procTaskRole", wfConfig.getTaskRole());
        hmVariables.put("procApprStatus", "1");
        hmVariables.put("procApprOpinion", "同意");
        hmVariables.put("procAssignee", "");
        hmVariables.put("procAppointTask", "");
        Map<String, Object> hmBizData = new HashMap<>();
        hmBizData.put("procBizId", orderBean.getOrderId());
        hmBizData.put("procBizType", "");
        hmBizData.put("procOrgCode", merchantBean.getId() + "");
        hmBizData.put("procBizMemo", procBizMemo);
        Map<String, Map<String, Object>> reqData = new HashMap<>();
        reqData.put("procData", hmProcData);
        reqData.put("variableData", hmVariables);
        reqData.put("bizData", hmBizData);
        String url = wfConfig.getStartWfurl();
        // 发送Https请求
        logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
        Long startTime = System.currentTimeMillis();
        try {
            responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
        } catch (Exception e) {
            logger.error("---电话提醒启动工作流报错---{}", e);
            throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
        }
        Long endTime = System.currentTimeMillis();
        logger.info("调用工作流接口返回信息：{}" + responseMap);
        logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        if (responseMap.get("retCode").toString().equals("00000000")) {
            // 更新贷后状态
            objs.put("processStatus", "10");
            instRemindPhoneBiz.updateManagerCarInfo(objs);
        }
        return result;
    }

    /**
     * 
     * Description:将电话提醒、电催信息抽取到贷后车辆信息表中
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月7日
     */
    public void saveInstManagerCarInfo(String orderId) {
        InstManageCarInfoEntity manageCarBean = carStatusManager.getCarStatusInfoByOrderId(orderId);
        if (manageCarBean != null) {
            if (manageCarBean.getStatus() == 70) {
                Map<String, Object> params = new HashMap<>();
                params.put("orderId", orderId);
                params.put("status", "05");
                params.put("afterLoanDate", new Date());

                carStatusManager.updateManagerCarInfoByOrderId(params);
            }
        } else {
            carStatusManager.persisitImci(orderId);
        }
        // 同时将inst_orderInfo表carStatus状态改为1
        carStatusManager.markIOIEByCarstatus(orderId);
    }

    @Override
    public List<ManageCarInfoBean> queryManagecarInfo(String orderId) {
        return instRemindRecordBiz.queryManagecarInfo(orderId);
    }

    /**
     * 
     * Description:根据条件查询电话提醒 电催总记录数
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月1日
     */
    @Override
    public int getInstRemindRecordListTotal(JSONObject objs) {
        objs.put("isPageHelper", true);
        List<InstRemindPhoneBean> instRemindPhoneList = instRemindPhoneBiz.selectInstRemindPhoneList2(objs);
        if (!CollectionUtils.isEmpty(instRemindPhoneList)) {
            return instRemindPhoneList.size();
        }
        return 0;
    }

    public String convertBillStatus(String billStatus) {
        if (!StringUtil.isNull(billStatus)) {
            if (billStatus.equals("0")) {
                return "已逾期";
            } else if (billStatus.equals("1")) {
                return "待还款";
            } else if (billStatus.equals("2")) {
                return "部分还款";
            } else if (billStatus.equals("3")) {
                return "已还款";
            }
        }
        return "";
    }

    /**
     * 根据订单号查询风控信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月6日
     */
    @Override
    public InstRemindPhoneBean getRiskInfoByOrderId(JSONObject objs) {
        return instRemindPhoneBiz.getRiskInfoByOrderId(objs);
    }

    /**
     * 
     * Description:电话提醒转贷后流程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月13日
     */
    @SuppressWarnings("rawtypes")
    private int startWFRemind(InstRemindRecordBean instRemindRecordBean) throws IqbException {
        LinkedHashMap responseMap = null;
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(instRemindRecordBean.getOrderId());
        MerchantBean merchantBean = merchantBiz.getMerByMerNo(orderBean.getMerchantNo());
        // 获取wfconfig文件
        WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 70);
        if (instRemindRecordBean != null) {
            String procBizMemo =
                    "电话提醒转贷后;" + instRemindRecordBean.getOrderId();// 摘要信息
            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());
            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", sysUserSession.getUserCode());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");
            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderBean.getOrderId() + "-" + instRemindRecordBean.getCurItems() + "-"
                    + instRemindRecordBean.getFlag());
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);
            String url = wfConfig.getStartWfurl();
            // 发送Https请求
            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                logger.error("---电话提醒启动工作流报错---{}", e);
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}" + responseMap);
            logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        } else {
            throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000005);
        }
        if (responseMap.get("retCode").toString().equals("00000000")) {
            // 更新inst_remind_iphone状态为处理中
            InstRemindPhoneBean instRemindPhoneBean = instRemindPhoneBiz.selectInstRemindPhoneOne(instRemindRecordBean);
            if (instRemindPhoneBean != null) {
                instRemindPhoneBean.setStatus("2");
                instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
            }
        }
        return result;
    }

    /**
     * 
     * Description:电话提醒转贷后流程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月13日
     */
    @SuppressWarnings("rawtypes")
    private int startWFUrge(InstRemindRecordBean instRemindRecordBean) throws IqbException {
        LinkedHashMap responseMap = null;
        int result = 0;
        OrderBean orderBean = orderBiz.selByOrderId(instRemindRecordBean.getOrderId());
        MerchantBean merchantBean = merchantBiz.getMerByMerNo(orderBean.getMerchantNo());
        // 获取wfconfig文件
        WFConfig wfConfig = wfConfigBiz.getConfigByBizType(orderBean.getBizType(), 60);
        if (instRemindRecordBean != null) {
            String procBizMemo =
                    "电催转贷后;" + instRemindRecordBean.getOrderId();// 摘要信息
            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());
            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            hmVariables.put("procTaskUser", sysUserSession.getUserCode());
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");
            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", orderBean.getOrderId() + "-" + instRemindRecordBean.getCurItems() + "-"
                    + instRemindRecordBean.getFlag());
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", merchantBean.getId() + "");
            hmBizData.put("procBizMemo", procBizMemo);
            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);
            String url = wfConfig.getStartWfurl();
            // 发送Https请求
            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                logger.error("---电话提醒启动工作流报错---{}", e);
                throw new IqbException(ApiReturnInfo.API_BIZ_STARTWF_30000000);
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}" + responseMap);
            logger.info("工作流接口交互花费时间，{}" + (endTime - startTime));
        } else {
            throw new IqbException(ApiReturnInfo.API_BIZTYPE_NULL_40000005);
        }
        if (responseMap.get("retCode").toString().equals("00000000")) {
            // 更新inst_remind_iphone状态为处理中
            InstRemindPhoneBean instRemindPhoneBean = instRemindPhoneBiz.selectInstRemindPhoneOne(instRemindRecordBean);
            if (instRemindPhoneBean != null) {
                instRemindPhoneBean.setStatus("2");
                instRemindPhoneBiz.updateInstRemindPhoneBean(instRemindPhoneBean);
            }

        }
        return result;
    }

    /**
     * 根据订单号查询电话电催信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月13日
     */
    @Override
    public InstRemindPhoneBean getRemindInfoByOrderId(JSONObject objs) {
        return instRemindPhoneBiz.getRemindInfoByOrderId(objs);
    }

    /**
     * 根据条件获取电话电催标记信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月13日
     */
    @Override
    public List<InstRemindRecordBean> getInstRemindRecordList(JSONObject objs) {
        objs.put("isPageHelper", true);
        return instRemindRecordBiz.selectInstRemindRecordList(objs);
    }
}
