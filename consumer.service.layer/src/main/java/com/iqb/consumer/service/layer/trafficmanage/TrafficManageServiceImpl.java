package com.iqb.consumer.service.layer.trafficmanage;

import java.io.OutputStream;
import java.math.BigDecimal;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.BizConstant.WFConst;
import com.iqb.consumer.common.utils.httputils.SendHttpsUtil;
import com.iqb.consumer.data.layer.bean.conf.WFConfig;
import com.iqb.consumer.data.layer.biz.conf.WFConfigBiz;
import com.iqb.consumer.data.layer.biz.trafficmanage.TrafficManageBiz;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.DateTools;

@Service
public class TrafficManageServiceImpl implements ITrafficManageService {

    protected static final Logger logger = LoggerFactory.getLogger(TrafficManageServiceImpl.class);

    @Autowired
    private TrafficManageBiz trafficManageBiz;

    @Resource
    private WFConfigBiz wfConfigBiz;

    /**
     * 
     * 车务管理订单查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public PageInfo<Map<String, Object>> selectTrafficManageOrderList(JSONObject objs) {
        return new PageInfo<>(trafficManageBiz.selectTrafficManageOrderList(objs));
    }

    /**
     * 
     * 车务管理订单查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public Integer countTrafficManageOrder(JSONObject objs) {
        return trafficManageBiz.countTrafficManageOrder(objs);
    }

    /**
     * 
     * 车务查询-分页
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public PageInfo<Map<String, Object>> selectTrafficManageInfoList(JSONObject objs) {
        return new PageInfo<>(trafficManageBiz.selectTrafficManageInfoList(objs));
    }

    /**
     * 
     * 车务查询统计
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public Integer countTrafficManageInfo(JSONObject objs) {
        return trafficManageBiz.countTrafficManageInfo(objs);
    }

    /**
     * 
     * 车务查询-导出
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public String exportTrafficManageInfoList(JSONObject requestMessage,
            HttpServletResponse response) {
        try {
            List<Map<String, Object>> trafficManageInfoList =
                    trafficManageBiz.selectTrafficManageInfoListNoPage(requestMessage);
            // try {
            // trafficManageInfoList = appendCollectTrafficManageInfo(caseLawOrderList);
            // } catch (Exception e) {
            // logger.error("", e);
            // }
            // 2.导出excel表格
            HSSFWorkbook workbook = getTrafficManageInfoExportWorkbook(trafficManageInfoList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=trafficManageInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            logger.error("导出贷后案件列表出现异常:{}", e);
            return "fail";
        }

        return "success";
    }

    private HSSFWorkbook getTrafficManageInfoExportWorkbook(List<Map<String, Object>> trafficManageInfoList) {
        String[] excelHeader =
        {"商户名称", "订单号", "姓名", "手机号", "借款金额", "总期数", "年检时间", "车辆最新所有人", "车辆首次注册时间", "商险保险公司", "商险截至时间",
                "交强保险公司", "交强截至时间"
                , "审核状态", "车辆状态", "贷后状态", "业务类型", "车架号", "车牌号", "车辆颜色"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("车务查询列表");

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
        if (trafficManageInfoList != null)
            for (int i = 0; i < trafficManageInfoList.size(); i++) {
                row = sheet.createRow(i + 1);
                Map<String, Object> caseOrder = trafficManageInfoList.get(i);
                row.createCell(0).setCellValue(
                        caseOrder.get("merchantName") == null ? "" : caseOrder.get("merchantName").toString());
                row.createCell(1).setCellValue(
                        caseOrder.get("orderId") == null ? "" : caseOrder.get("orderId").toString());
                row.createCell(2).setCellValue(
                        caseOrder.get("realName") == null ? "" : caseOrder.get("realName").toString());
                row.createCell(3).setCellValue(caseOrder.get("regId") == null ? "" : caseOrder.get("regId").toString());
                row.createCell(4).setCellValue(caseOrder.get("orderAmt") == null ? 0 :
                        ((BigDecimal) caseOrder.get("orderAmt")).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                row.createCell(5).setCellValue(caseOrder.get("orderItems") == null ? "0" :
                        ((Integer) caseOrder.get("orderItems")).intValue() + "");
                row.createCell(6).setCellValue(
                        caseOrder.get("carInspectTime") == null ? "" : caseOrder.get("carInspectTime").toString());
                row.createCell(7).setCellValue(
                        caseOrder.get("currentMaster") == null ? "" : caseOrder.get("currentMaster").toString());
                row.createCell(8).setCellValue(
                        caseOrder.get("registerDate") == null ? "" : caseOrder.get("registerDate").toString());

                row.createCell(9).setCellValue(
                        caseOrder.get("comInsOrg") == null ? "" : caseOrder.get("comInsOrg").toString());
                row.createCell(10).setCellValue(
                        caseOrder.get("comInsOverTime") == null ? "" : caseOrder.get("comInsOverTime").toString());
                row.createCell(11).setCellValue(
                        caseOrder.get("comTraAccOrg") == null ? "" : caseOrder.get("comTraAccOrg").toString());
                row.createCell(12)
                        .setCellValue(
                                caseOrder.get("comTraAccOverTime") == null ? "" : caseOrder.get("comTraAccOverTime")
                                        .toString());

                //
                if (caseOrder.get("checkStatus") != null)
                    switch (caseOrder.get("checkStatus").toString()) {
                        case "1":
                            row.createCell(13).setCellValue("审核中");
                            break;
                        case "2":
                            row.createCell(13).setCellValue("已审核");
                            break;
                        case "3":
                            row.createCell(13).setCellValue("未提交");
                            break;
                    }
                if (caseOrder.get("carStatus") != null)
                    switch (caseOrder.get("carStatus").toString()) {
                        case "5":
                            row.createCell(14).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(14).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(14).setCellValue("贷后处置中");
                            break;
                        case "15":
                            row.createCell(14).setCellValue("待失联处置");
                            break;
                        case "20":
                            row.createCell(14).setCellValue("已失联");
                            break;
                        case "25":
                            row.createCell(14).setCellValue("待入库");
                            break;
                        case "30":
                            row.createCell(14).setCellValue("已入库");
                            break;
                        case "35":
                            row.createCell(14).setCellValue("待出售");
                            break;
                        case "40":
                            row.createCell(14).setCellValue("已出售");
                            break;
                        case "45":
                            row.createCell(14).setCellValue("待转租");
                            break;
                        case "50":
                            row.createCell(14).setCellValue("已转租");
                            break;
                        case "55":
                            row.createCell(14).setCellValue("待返还");
                            break;
                        case "60":
                            row.createCell(14).setCellValue("已返还");
                            break;
                        case "65":
                            row.createCell(14).setCellValue("未拖回");
                            break;
                        case "70":
                            row.createCell(14).setCellValue("正常");
                            break;
                    }
                // 贷后状态: 5 待贷后处理 10贷后处理中 15 待外包处理 20 外包处理中 25 法务处理中 30 处理结束
                if (caseOrder.get("processStatus") != null)
                    switch (caseOrder.get("processStatus").toString()) {
                        case "5":
                            row.createCell(15).setCellValue("待贷后处理");
                            break;
                        case "05":
                            row.createCell(15).setCellValue("待贷后处理");
                            break;
                        case "10":
                            row.createCell(15).setCellValue("贷后处理中");
                            break;
                        case "15":
                            row.createCell(15).setCellValue("待外包处理");
                            break;
                        case "20":
                            row.createCell(15).setCellValue("外包处理中");
                            break;
                        case "25":
                            row.createCell(15).setCellValue("法务处理中");
                            break;
                        case "30":
                            row.createCell(15).setCellValue("处理结束");
                            break;
                    }

                if (caseOrder.get("bizType") != null)
                    switch (caseOrder.get("bizType").toString()) {
                        case "2001":
                            row.createCell(16).setCellValue("以租代售新车");
                            break;
                        case "2002":
                            row.createCell(16).setCellValue("以租代售二手车");
                            break;
                        case "2100":
                            row.createCell(16).setCellValue("抵押车");
                            break;
                        case "2200":
                            row.createCell(16).setCellValue("质押车");
                            break;
                        case "1100":
                            row.createCell(16).setCellValue("易安家");
                            break;
                        case "1000":
                            row.createCell(16).setCellValue("医美");
                            break;
                        case "1200":
                            row.createCell(16).setCellValue("旅游");
                            break;
                        case "2300":
                            row.createCell(16).setCellValue("车秒贷");
                            break;
                        case "2400":
                            row.createCell(16).setCellValue("车主贷");
                            break;
                        case "3002":
                            row.createCell(16).setCellValue("华益周转贷");
                            break;
                        case "4001":
                            row.createCell(16).setCellValue("融资租赁");
                            break;
                    }

                row.createCell(17).setCellValue(
                        caseOrder.get("carNo") == null ? "" : caseOrder.get("carNo").toString());
                row.createCell(18).setCellValue(
                        caseOrder.get("plate") == null ? "" : caseOrder.get("plate").toString());
                row.createCell(19).setCellValue(
                        caseOrder.get("color") == null ? "" : caseOrder.get("color").toString());
            }
        return wb;
    }

    /**
     * 
     * 车务详情
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public Map<String, Object> getTrafficManageDetail(JSONObject objs) {
        return trafficManageBiz.getTrafficManageDetail(objs);
    }

    /**
     * 
     * 补充资料历史记录
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public List<Map<String, Object>> selectTrafManaAdditionalhistory(JSONObject objs) {
        return trafficManageBiz.selectTrafManaAdditionalhistory(objs);
    }

    /**
     * 
     * 保存补充资料
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public int saveTrafficManageAdditional(JSONObject objs) {
        String orderId = objs.getString("orderId");
        String additionalNo = objs.getString("additionalNo");
        Map<String, Object> objsMap = JSON.toJavaObject(objs, Map.class);
        JSONObject parMap = new JSONObject();
        parMap.put("orderId", orderId);
        parMap.put("additionalNo", additionalNo);
        parMap.put("checkStatus", 1);
        Integer cnt = trafficManageBiz.countAdditionalByCondition(parMap);
        if (cnt != null && cnt > 0)
            return trafficManageBiz.updateTrafficManageAdditional(objsMap);

        if (trafficManageBiz.saveTrafficManageAdditional(objsMap) == 0) {
            logger.error("orderId[{}],additionalNo[{}]-->save fail !!!", objs.getString("orderId"),
                    objs.getString("additionalNo"));
            return 0;
        }
        String procBizId = trafficManageBiz.selectOrderAdditionalMaxProcBizId(orderId);
        if (procBizId == null || procBizId.trim().equals(""))
            procBizId = "CW" + orderId + "-001";
        else {
            String priVal = procBizId.substring(procBizId.lastIndexOf("-") + 1);
            int pviInt = Integer.parseInt(priVal);
            priVal = "000" + (pviInt + 1);
            procBizId = "CW" + orderId + "-" + priVal.substring(priVal.length() - 3);
        }

        if (trafficManageBiz.startAdditionalWorkFlow(procBizId, orderId, additionalNo) == 1) {
            try {
                LinkedHashMap res =
                        startWF(procBizId, orderId, additionalNo, objs.getString("procTaskUser"),
                                objs.getString("operatorRegId"));
                if (res == null || res.get(WFConst.WFInterRetSuccKey) == null
                        || !WFConst.WFInterRetSucc.equals(res.get(WFConst.WFInterRetSuccKey).toString().trim())) {
                    logger.error("启动工作流失败orderId[{}],additionalNo[{}]", orderId, additionalNo);
                    trafficManageBiz.delAdditional(procBizId, orderId, additionalNo, null);
                    throw new RuntimeException("启动工作流失败");
                }
            } catch (Throwable e) {
                logger.error("启动工作流发生故障orderId[" + orderId + "],additionalNo[" + additionalNo + "]", e);
                trafficManageBiz.delAdditional(procBizId, orderId, additionalNo, null);
                throw new RuntimeException("启动工作流发生故障");
            }
            return 1;
        } else {
            logger.error("数据处理异常，未启动工作流orderId[{}],additionalNo[{}]", orderId, additionalNo);
            trafficManageBiz.delAdditional(procBizId, orderId, additionalNo, null);
            throw new RuntimeException("数据处理异常，未启动工作流");
        }
    }

    /**
     * 启动车务资料提交流程
     * 
     * @param procBizId, orderId, additionalNo
     * @return
     * @throws IqbException LinkedHashMap
     */
    @SuppressWarnings("rawtypes")
    private LinkedHashMap startWF(String procBizId, String orderId, String additionalNo
            , String procTaskUser, String operatorRegId)
            throws IqbException {
        LinkedHashMap responseMap = null;
        JSONObject jobj = new JSONObject();
        jobj.put("orderId", orderId);
        jobj.put("procBizId", procBizId);
        Map<String, Object> detail = trafficManageBiz.getTrafficManageDetail(jobj);
        if (detail != null) {
            String procBizMemo =
                    "车务登记;" + detail.get("merchantNo") + ';' + detail.get("realName") + ';' + detail.get("regId");// 摘要添加手机号

            WFConfig wfConfig = wfConfigBiz.getConfigByBizType("9901", 1);

            Map<String, Object> hmProcData = new HashMap<>();
            hmProcData.put("procDefKey", wfConfig.getProcDefKey());

            // Map<String, Object> hmAuthData = new HashMap<>();
            // hmAuthData.put("procAuthType", "1");

            Map<String, Object> hmVariables = new HashMap<>();
            hmVariables.put("procAuthType", "1");
            hmVariables.put("procTokenUser", wfConfig.getTokenUser());
            hmVariables.put("procTokenPass", wfConfig.getTokenPass());
            // hmVariables.put("procTaskUser", procTaskUser);
            hmVariables.put("procTaskUser", operatorRegId);
            // hmVariables.put("procTaskUser", "13521859508");
            hmVariables.put("procTaskRole", wfConfig.getTaskRole());
            hmVariables.put("procApprStatus", "1");
            hmVariables.put("procApprOpinion", "同意");
            hmVariables.put("procAssignee", "");
            hmVariables.put("procAppointTask", "");

            Map<String, Object> hmBizData = new HashMap<>();
            hmBizData.put("procBizId", procBizId);
            hmBizData.put("procBizType", "");
            hmBizData.put("procOrgCode", detail.get("orgCode").toString().trim());
            hmBizData.put("procBizMemo", procBizMemo);
            hmBizData.put("orderId", orderId);
            hmBizData.put("additionalNo", additionalNo);

            Map<String, Map<String, Object>> reqData = new HashMap<>();
            reqData.put("procData", hmProcData);
            reqData.put("variableData", hmVariables);
            reqData.put("bizData", hmBizData);
            // reqData.put("authData", hmAuthData);

            String url = wfConfig.getStartWfurl();
            // url = url.substring(0, url.lastIndexOf("/WfTask/")) +
            // "/WfTask/startAndCommitProcess";
            logger.info("StartWfurl[{}]", url);
            // 发送Https请求

            logger.info("调用工作流接口传入信息：{}" + JSONObject.toJSONString(reqData));
            Long startTime = System.currentTimeMillis();
            try {
                responseMap = SendHttpsUtil.postMsg4GetMap(url, reqData);
            } catch (Exception e) {
                throw new RuntimeException("工作流接口交易失败");
            }
            Long endTime = System.currentTimeMillis();
            logger.info("调用工作流接口返回信息：{}", responseMap);
            logger.info("工作流接口交互花费时间，{}", (endTime - startTime));
        }
        return responseMap;
    }

    /**
     * 
     * 修改补充资料状态
     * 
     * @author chenyong
     * @param objs
     * @param request
     * @return 2018年8月6日
     */
    @Override
    public int updateAdditionalStatus(Map<String, Object> map) {
        return trafficManageBiz.updateAdditionalStatus(map);
    }

}
