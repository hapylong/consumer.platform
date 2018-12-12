package com.iqb.consumer.service.layer.cheegu;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.utils.encript.EncryptUtils;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyBeanPojo;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyDetailBean;
import com.iqb.consumer.data.layer.bean.cheegu.InstCarPeccancyDetailPojo;
import com.iqb.consumer.data.layer.biz.cheegu.InstCarPeccancyBiz;
import com.iqb.consumer.data.layer.biz.cheegu.InstCarPeccancyDetailBiz;
import com.iqb.consumer.service.layer.admin.AdminService;
import com.iqb.etep.common.utils.DateTools;
import com.iqb.etep.common.utils.StringUtil;
import com.iqb.etep.common.utils.SysUserSession;
import com.iqb.etep.common.utils.https.SimpleHttpUtils;

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
 * 2018年5月28日下午3:40:40 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class InstCarPeccancyServiceImpl implements InstCarPeccancyService {
    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(InstCarPeccancyServiceImpl.class);
    @Autowired
    private InstCarPeccancyBiz instCarPeccancyBiz;
    @Autowired
    private InstCarPeccancyDetailBiz instCarPeccancyDetailBiz;
    @Autowired
    private AdminService adminServiceImpl;
    @Autowired
    private SysUserSession sysUserSession;
    @Autowired
    private ConsumerConfig consumerConfig;

    private final String cheeguBuy = "WeiZhang.Buy";
    private final String cheeguGetData = "WeiZhang.getData";

    /**
     * 
     * Description:根据条件查询车辆违章信息表
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    @Override
    public PageInfo<InstCarPeccancyBean> selectInstCarPeccancyList(JSONObject objs) {
        return new PageInfo<>(instCarPeccancyBiz.selectInstCarPeccancyList(objs));
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月28日
     */
    @Override
    public PageInfo<InstCarPeccancyDetailBean> selectInstCarPeccancyDetailList(JSONObject objs) {
        return new PageInfo<>(instCarPeccancyDetailBiz.selectInstCarPeccancyDetailList(objs));
    }

    /**
     * 更新车辆违章信息表
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月28日
     */
    @Override
    public int updateInstCarPeccancyStatusByOrderId(JSONObject objs) {
        int resultValue = 0;
        objs.put("processStatus", "2");
        objs.put("operateUser", sysUserSession.getUserCode());

        resultValue = instCarPeccancyBiz.updateInstCarPeccancyStatusByOrderId(objs);
        objs.put("status", "1");
        resultValue += instCarPeccancyDetailBiz.updateInstCarPeccancyDetailStatusByOrderId(objs);
        return resultValue;
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年5月30日
     */
    @Override
    public String exportInstCarPeccancyList(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            adminServiceImpl.merchantNos(objs);
            objs.put("isPageHelper", true);
            List<InstCarPeccancyBean> orderList = instCarPeccancyBiz.selectInstCarPeccancyList(objs);
            // 2.导出excel表格
            HSSFWorkbook workbook = exportOrderList(orderList);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=carPeccancyList-" + DateTools.getYmdhmsTime() + ".xls";
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
    private HSSFWorkbook exportOrderList(List<InstCarPeccancyBean> list) {
        String[] excelHeader = {"商户", "订单编号", "姓名", "手机号", "订单时间", "总期数", "当前期数", "账单状态", "车辆状态", "车牌号", "车架号", "发动机号",
                "发送时间", "总扣款", "总扣分", "违章处理时间", "违章状态", "处理状态"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单信息页");

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
            InstCarPeccancyBean carPeccancyBean = list.get(i);

            row.createCell(0).setCellValue(carPeccancyBean.getMerchantName());
            row.createCell(1).setCellValue(carPeccancyBean.getOrderId());
            row.createCell(2).setCellValue(carPeccancyBean.getRealName());
            row.createCell(3).setCellValue(carPeccancyBean.getRegId());
            HSSFCell cell = row.createCell(4);
            cell.setCellValue(carPeccancyBean.getOrderTime());
            cell.setCellStyle(style);
            row.createCell(5).setCellValue(carPeccancyBean.getOrderItems());
            row.createCell(6).setCellValue(carPeccancyBean.getCurItems());
            row.createCell(7).setCellValue(convertBillStatus(carPeccancyBean.getBillStatus()));
            row.createCell(8).setCellValue(convertCarStatus(carPeccancyBean.getCarStatus()));
            row.createCell(9).setCellValue(carPeccancyBean.getLicenseNo());
            row.createCell(10).setCellValue(carPeccancyBean.getVin());
            row.createCell(11).setCellValue(carPeccancyBean.getEngineNumber());
            row.createCell(12).setCellValue(carPeccancyBean.getSendTime());
            row.createCell(13).setCellValue(carPeccancyBean.getTotalPeccancyAmt());
            row.createCell(14).setCellValue(carPeccancyBean.getTotalDegree());
            row.createCell(15).setCellValue(carPeccancyBean.getProcessDate());
            row.createCell(16).setCellValue(convertStatus(carPeccancyBean.getStatus()));
            row.createCell(17).setCellValue(convertProcessStatus(carPeccancyBean.getProcessStatus()));

        }
        return wb;
    }

    private String convertBillStatus(String billStatus) {
        if (!StringUtil.isNull(billStatus)) {
            if (billStatus.equals("0")) {
                return "已逾期";
            } else if (billStatus.equals("1")) {
                return "待还款";
            } else if (billStatus.equals("2")) {
                return "部分还款";
            } else if (billStatus.equals("3")) {
                return "已结清";
            }
        }
        return "";
    }

    private String convertCarStatus(String catStatus) {
        if (!StringUtil.isNull(catStatus)) {
            if (catStatus.equals("5")) {
                return "贷后处置中";
            } else if (catStatus.equals("10")) {
                return "贷后处置中";
            } else if (catStatus.equals("15")) {
                return "待失联处置";
            } else if (catStatus.equals("20")) {
                return "已失联";
            } else if (catStatus.equals("25")) {
                return "待入库";
            } else if (catStatus.equals("30")) {
                return "已入库";
            } else if (catStatus.equals("35")) {
                return "待出售";
            } else if (catStatus.equals("40")) {
                return "已出售";
            } else if (catStatus.equals("45")) {
                return "待转租";
            } else if (catStatus.equals("50")) {
                return "已转租";
            } else if (catStatus.equals("55")) {
                return "待返还";
            } else if (catStatus.equals("60")) {
                return "已返还";
            } else if (catStatus.equals("65")) {
                return "待外包处理";
            } else if (catStatus.equals("70")) {
                return "正常";
            }
        }
        return "正常";
    }

    private String convertStatus(String status) {
        if (!StringUtil.isNull(status)) {
            if (status.equals("1")) {
                return "未违章";
            } else if (status.equals("2")) {
                return "已违章";
            }
        }
        return "";
    }

    private String convertProcessStatus(String processStatus) {
        if (!StringUtil.isNull(processStatus)) {
            if (processStatus.equals("1")) {
                return "未处理";
            } else if (processStatus.equals("2")) {
                return "已处理";
            }
        }
        return "";
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月4日
     */
    @Override
    public int doSendAndGetInstCarPeccancyDetail(JSONObject objs) {
        int resultValue = 0;
        InstCarPeccancyBeanPojo carPer = JSONObject.toJavaObject(objs, InstCarPeccancyBeanPojo.class);

        // 将要查询车辆违章信息的订单号 车架号保存到车辆违章信息表中
        List<InstCarPeccancyBean> instCarPeccancyList = carPer.getOrderList();
        if (!CollectionUtils.isEmpty(instCarPeccancyList)) {
            // 保存数据到违章信息表
            for (InstCarPeccancyBean instCarPeccancyBean : instCarPeccancyList) {
                objs.put("orderId", instCarPeccancyBean.getOrderId());
                objs.put("vin", instCarPeccancyBean.getVin());
                if (instCarPeccancyBiz.selectInstCarPeccancyInfo(objs) == null) {
                    instCarPeccancyBiz.insertInstCarPeccancy(instCarPeccancyBean);
                } else {
                    instCarPeccancyBiz.updateInstCarPeccancyStatusByOrderId(objs);
                }
            }
        }
        // 查询违章车辆信息列表
        if (!CollectionUtils.isEmpty(instCarPeccancyList)) {
            for (InstCarPeccancyBean instCarPeccancyBean : instCarPeccancyList) {
                String cheeguOrderId = getCarPeccancyBuy(instCarPeccancyBean);
                if (!StringUtil.isNull(cheeguOrderId)) {
                    objs.clear();
                    objs.put("cheeguOrderId", cheeguOrderId);
                    objs.put("orderId", instCarPeccancyBean.getOrderId());
                    objs.put("vin", instCarPeccancyBean.getVin());
                    resultValue += instCarPeccancyBiz.updateInstCarPeccancyStatusByOrderId(objs);
                }
            }
        }
        return resultValue;
    }

    /**
     * 
     * Description:获取车辆违章信息下单接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    private String getCarPeccancyBuy(InstCarPeccancyBean instCarPeccancyBean) {
        String cheeguOrderId = "";

        Map<String, String> params = new HashMap<>();
        params.put("vin", instCarPeccancyBean.getVin());
        params.put("engineNumber", instCarPeccancyBean.getEngineNumber());
        params.put("licenseNo", instCarPeccancyBean.getLicenseNo());
        params.put("user", consumerConfig.getCheeguPeccancyUser());
        params.put("callback", consumerConfig.getCheeguPeccancyCallBack());
        String sign = EncryptUtils.getSign(params, consumerConfig.getCheeguPeccancyKey());
        params.put("sign", sign);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        // 数据组装
        logger.info("---调用车易估违章信息下单接口开始---{}---", params);
        String result = null;
        try {
            result =
                    SimpleHttpUtils.httpRequest(consumerConfig.getCheeguPeccancyUrl() + cheeguBuy, params, "post",
                            "UTF-8",
                            headers);
        } catch (Exception e) {
            logger.error("---调用车易估车辆违章接口报错---{}", e);
        }
        logger.info("---调用车易估违章信息下单接口结束---{}---", result);
        if (!StringUtil.isNull(result)) {
            JSONObject json = JSONObject.parseObject(result);

            String data = json.getString("success");
            // 调用接口成功
            if (data.equals("1")) {
                String message = json.getString("message");
                JSONObject dataJson = JSONObject.parseObject(message);
                String state = dataJson.getString("state");
                // 报告生成成功
                if (state.equals("1")) {
                    cheeguOrderId = dataJson.getString("orderid");
                }
            }
        }
        return cheeguOrderId;
    }

    /**
     * 
     * Description:保存车辆违章明细信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月29日
     */
    private int saveInstCarPeccancyDetailInfo(List<InstCarPeccancyBean> instCarPeccancyList) {
        int resultValue = 0;
        JSONObject objs = new JSONObject();
        if (!CollectionUtils.isEmpty(instCarPeccancyList)) {
            for (InstCarPeccancyBean instCarPeccancyBean : instCarPeccancyList) {
                // 没有车易估orderId无法直接获取车辆违章明细信息
                if (!StringUtil.isNull(instCarPeccancyBean.getCheeguOrderId())) {
                    // 2.逐条获取车辆违章明细信息
                    InstCarPeccancyDetailPojo instCarPojo = getCarPeccancyGetData(instCarPeccancyBean);
                    // 获取违章信息集合
                    if (!CollectionUtils.isEmpty(instCarPojo.getInstCarPeccancyDetailBeanList())) {

                        // 设置违章处理状态
                        objs.put("processStatus", "1");

                        List<InstCarPeccancyDetailBean> instCarPeccancyDetailBeanList =
                                instCarPojo.getInstCarPeccancyDetailBeanList();
                        for (InstCarPeccancyDetailBean detailBean : instCarPeccancyDetailBeanList) {
                            detailBean.setOrderId(instCarPeccancyBean.getOrderId());
                            detailBean.setVin(instCarPeccancyBean.getVin());

                            objs.put("orderId", instCarPeccancyBean.getOrderId());
                            objs.put("vin", instCarPeccancyBean.getVin());
                            objs.put("peccancyTime", detailBean.getPeccancyTime());
                            InstCarPeccancyDetailBean bean =
                                    instCarPeccancyDetailBiz.selectInstCarPeccancyDetailInfo(objs);
                            // 3.根据订单号 车架号 违章id获取违章信息,即bean,明细信息中不存在,则入库;存在则更新状态
                            if (bean == null) {
                                // 保存违章明细信息
                                resultValue +=
                                        instCarPeccancyDetailBiz.insertInstCarPeccancyDetail(detailBean);
                            } else {
                                objs.put("status", detailBean.getStatus());
                                instCarPeccancyDetailBiz.updateInstCarPeccancyDetailStatusByOrderId(objs);
                            }
                        }
                    }
                }
                // 4.将上月已违章且本月未返回的违章信息的状态改为已处理
                objs.put("orderId", instCarPeccancyBean.getOrderId());
                objs.put("vin", instCarPeccancyBean.getVin());
                // 查询上月已违章，本月未返回的违章信息
                List<InstCarPeccancyDetailBean> list = instCarPeccancyDetailBiz.selectInstCarDetailList(objs);
                if (!CollectionUtils.isEmpty(list)) {
                    for (InstCarPeccancyDetailBean bean : list) {
                        objs.put("status", "1");
                        objs.put("cOrderId", bean.getcOrderId());
                        // 更改违章处理状态为 已处理
                        instCarPeccancyDetailBiz.updateInstCarPeccancyDetailStatusByOrderId(objs);
                    }
                }
                // 5.根据订单号对违章信息的总计扣分 总计罚款金额进行修改
                InstCarPeccancyBean instBeanSum = instCarPeccancyDetailBiz.selectInstCarDetailSum(objs);
                if (null != instBeanSum) {
                    objs.put("status", "2");
                    objs.put("totalDegree", instBeanSum.getTotalDegree());
                    objs.put("totalPeccancyAmt", instBeanSum.getTotalPeccancyAmt());
                    instCarPeccancyBiz.updateInstCarPeccancyStatusByOrderId(objs);
                } else {
                    objs.put("status", "1");
                    objs.put("totalDegree", "0");
                    objs.put("totalPeccancyAmt", "0");
                    instCarPeccancyBiz.updateInstCarPeccancyStatusByOrderId(objs);
                }
            }
        }
        return resultValue;
    }

    /**
     * 
     * Description:车e估违章数据主动获取接口
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月28日
     */
    private InstCarPeccancyDetailPojo getCarPeccancyGetData(InstCarPeccancyBean instCarPeccancyBean) {
        InstCarPeccancyDetailPojo instCarPojo = null;

        Map<String, String> params = new HashMap<>();
        params.put("id", instCarPeccancyBean.getCheeguOrderId());
        params.put("user", consumerConfig.getCheeguPeccancyUser());
        String sign = EncryptUtils.getSign(params, consumerConfig.getCheeguPeccancyKey());
        params.put("sign", sign);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        // 数据组装
        logger.info("---调用车易估违章数据主动获取接口开始---{}---", params);
        String result = null;
        try {
            result =
                    SimpleHttpUtils.httpRequest(consumerConfig.getCheeguPeccancyUrl() + cheeguGetData, params, "post",
                            "UTF-8",
                            headers);
        } catch (Exception e) {
            logger.error("---车e估违章数据主动获取接口报错---{}", e);
        }
        logger.info("---调用车易估违章数据主动获取接口结束---{}---", result);
        if (!StringUtil.isNull(result)) {
            JSONObject json = JSONObject.parseObject(result);
            String data = json.getString("success");
            // 调用接口成功
            if (data.equals("1")) {
                String message = json.getString("message");
                JSONObject dataJson = JSONObject.parseObject(message);
                String state = dataJson.getString("state");
                // 报告生成成功
                if (state.equals("1")) {
                    JSONArray jsonArray = dataJson.getJSONArray("data");
                    // 有违章记录
                    if (!jsonArray.isEmpty()) {
                        instCarPojo = JSONObject.toJavaObject(dataJson, InstCarPeccancyDetailPojo.class);
                    } else {
                        instCarPojo = new InstCarPeccancyDetailPojo();
                    }
                }
            }
        }
        return instCarPojo;
    }

    /**
     * 车易估-中阁毁回调接口-完成违章录入与违章总金额 总罚款计算
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年7月17日
     */
    @Override
    public int callbackForCheegu(JSONObject objs) {
        logger.info("---车易估-中阁毁回调接口-完成违章录入与违章总金额 总罚款计算---参数---{}", objs);
        String cheeguOrderid = objs.getString("orderid");
        int resultValue = 0;
        if (!StringUtil.isNull(objs.getString("orderid"))) {
            List<InstCarPeccancyBean> instCarPeccancyList = new ArrayList<>();
            InstCarPeccancyBean bean = new InstCarPeccancyBean();
            bean.setCheeguOrderId(cheeguOrderid);
            instCarPeccancyList.add(bean);
            // 根据订单号查询违章信息
            instCarPeccancyList = instCarPeccancyBiz.selectInstCarPeccancyListByOrderIds(instCarPeccancyList);
            resultValue = saveInstCarPeccancyDetailInfo(instCarPeccancyList);
        }
        logger.info("---车易估-中阁毁回调接口-完成违章录入与违章总金额 总罚款计算---完成---{}", resultValue);
        return resultValue;
    }
}
