/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:21:08
 * @version V1.0
 */
package com.iqb.consumer.service.layer.downpayment;

import java.io.OutputStream;
import java.util.List;
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
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.downpayment.DownPaymentBean;
import com.iqb.consumer.data.layer.biz.downpayment.DownPaymentBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.etep.common.utils.DateTools;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Service("downPaymentService")
public class DownPaymentServiceImpl extends BaseService implements DownPaymentService {

    protected static Logger logger = LoggerFactory.getLogger(DownPaymentServiceImpl.class);

    @Resource
    private DownPaymentBiz downPaymentBiz;
    @Resource
    private MerchantBeanBiz merchantBiz;

    // @Autowired
    // private SysUserSession sysUserSession;

    @Override
    public PageInfo<DownPaymentBean> getDownPaymentList(JSONObject objs) {
        // 获取商户权限的Objs
        objs = getMerchLimitObject(objs);
        if (objs == null) {
            return null;
        }
        List<DownPaymentBean> list = downPaymentBiz.getDownPaymentList(objs);
        for (DownPaymentBean dpb : list) {
            dpb.setSumAmt(this.getSumAmt(dpb));
        }
        return new PageInfo<DownPaymentBean>(list);
    }

    @Override
    public String getDownPaymentListSave(JSONObject objs, HttpServletResponse response) {
        try {
            // 获取商户权限的Objs
            objs = getMerchLimitObject(objs);
            if (objs == null) {
                return null;
            }
            List<DownPaymentBean> list = downPaymentBiz.getDownPaymentListSave(objs);
            for (DownPaymentBean dpb : list) {
                dpb.setSumAmt(this.getSumAmt(dpb));
            }
            // 2.导出excel表格
            HSSFWorkbook workbook = this.exportDownPaymentList(list);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=DownPayment-" + DateTools.getYmdhmsTime() + ".xls";
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

    // 获取首付款总额
    private String getSumAmt(DownPaymentBean downPaymentBean) {
        String result = null;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        // 首付款合计=首付+保证金+服务费+上收息+上收月供
        double downPayment, margin, serviceFee, feeAmount, monthInterest, sumAmt;
        downPayment = margin = serviceFee = feeAmount = monthInterest = sumAmt = 0.0;
        if (downPaymentBean.getDownPayment() != null) {
            downPayment = Double.valueOf(downPaymentBean.getDownPayment());
            sumAmt = sumAmt + downPayment;
        }
        if (downPaymentBean.getMargin() != null) {
            margin = Double.valueOf(downPaymentBean.getMargin());
            sumAmt = sumAmt + margin;
        }
        if (downPaymentBean.getServiceFee() != null) {
            serviceFee = Double.valueOf(downPaymentBean.getServiceFee());
            sumAmt = sumAmt + serviceFee;
        }
        if (downPaymentBean.getFeeAmount() != null) {
            feeAmount = Double.valueOf(downPaymentBean.getFeeAmount());
            sumAmt = sumAmt + feeAmount;
        }
        if (("1".equals(downPaymentBean.getTakePayment())) && (downPaymentBean.getMonthInterest() != null)) {
            monthInterest = Double.valueOf(downPaymentBean.getMonthInterest());
            sumAmt = sumAmt + monthInterest;
        }
        result = df.format(sumAmt);

        return result;
    }

    // 导出
    private HSSFWorkbook exportDownPaymentList(List<DownPaymentBean> list) {
        String[] excelHeader = {"订单号", "客户姓名", "手机号", "门店名称", "资金来源", "资金端上标ID号", "借款总额", "总期数", "业务类型", "首付款", "上收月供",
                "上收息", "保证金", "服务费", "充值费", "订单日期", "订单状态", "月供", "首付款合计"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("首付款信息页");

        HSSFRow row = sheet.createRow((int) 0);
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));

        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            DownPaymentBean dpb = list.get(i);
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");

            row.createCell(0).setCellValue(dpb.getOrderId());
            row.createCell(1).setCellValue(dpb.getRealName());
            row.createCell(2).setCellValue(dpb.getRegId());
            row.createCell(3).setCellValue(dpb.getMerchantShortName());
            row.createCell(4).setCellValue(this.getSourcesFunding(dpb.getSourcesFunding()));
            row.createCell(5).setCellValue(dpb.getFundingId());

            // 订单金额
            row.createCell(6).setCellValue(
                    (dpb.getOrderAmt() == null ? "0.00" : df.format(Double.parseDouble(dpb.getOrderAmt()))));
            // HSSFCellStyle cellStyle6 = wb.createCellStyle();
            // cellStyle6.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // row.createCell(6).setCellStyle(cellStyle6);

            row.createCell(7).setCellValue(dpb.getOrderItems());
            row.createCell(8).setCellValue(this.getBizType(dpb.getBizType()));

            // 首付款
            row.createCell(9).setCellValue(
                    (dpb.getDownPayment() == null ? "0.00" : df.format(Double.valueOf(dpb.getDownPayment()))));
            // HSSFCellStyle cellStyle9 = wb.createCellStyle();
            // cellStyle9.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // row.createCell(9).setCellStyle(cellStyle9);

            row.createCell(10).setCellValue(this.getTakePayment(dpb.getTakePayment()));

            // 手续费
            row.createCell(11).setCellValue(
                    (dpb.getFeeAmount() == null ? "0.00" : df.format(Double.valueOf(dpb.getFeeAmount()))));
            // HSSFCellStyle cellStyle11 = wb.createCellStyle();
            // cellStyle11.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // row.createCell(11).setCellStyle(cellStyle11);

            // 保证金
            row.createCell(12).setCellValue(dpb.getMargin() == null ? 0.00 : Double.valueOf(dpb.getMargin()));
            // HSSFCellStyle cellStyle12 = wb.createCellStyle();
            // cellStyle12.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // row.createCell(12).setCellStyle(cellStyle12);

            // 服务费
            row.createCell(13).setCellValue(
                    (dpb.getServiceFee() == null ? "0.00" : df.format(Double.valueOf(dpb.getServiceFee()))));
            // HSSFCellStyle cellStyle13 = wb.createCellStyle();
            // cellStyle13.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
            // row.createCell(13).setCellStyle(cellStyle13);

            row.createCell(14).setCellValue(dpb.getcZF());
            row.createCell(15).setCellValue(dpb.getRepayDate());
            row.createCell(16).setCellValue(this.getRiskStatus(dpb.getRiskStatus()));
            // 月供
            row.createCell(17).setCellValue(
                    (dpb.getMonthInterest() == null ? "0.00" : df.format(Double.valueOf(dpb.getMonthInterest()))));
            // 首付款合计
            row.createCell(18)
                    .setCellValue((dpb.getSumAmt() == null ? "0.00" : df.format(Double.valueOf(dpb.getSumAmt()))));
        }

        // 设置列宽
        sheet.setColumnWidth(0, 5000);// 订单ID
        sheet.setColumnWidth(1, 2000);// 姓名
        sheet.setColumnWidth(2, 3500);// 手机号
        for (int j = 3; j < excelHeader.length; j++) {
            sheet.autoSizeColumn(j);
        }

        return wb;
    }

    // 获取资金来源
    private String getSourcesFunding(String sourcesFunding) {
        String result = null;
        if ("1".equals(sourcesFunding)) {
            result = "爱钱帮";
        } else if ("2".equals(sourcesFunding)) {
            result = "饭饭金服";
        }
        return result;
    }

    // 获取业务类型
    private String getBizType(String bizType) {
        String result = null;
        if ("2001".equals(bizType)) {
            result = "以租代售新车";
        } else if ("2002".equals(bizType)) {
            result = "以租代售二手车";
        } else if ("2100".equals(bizType)) {
            result = "抵押车";
        } else if ("2200".equals(bizType)) {
            result = "质押车";
        } else if ("1100".equals(bizType)) {
            result = "易安家";
        } else if ("1000".equals(bizType)) {
            result = "医美";
        } else if ("1200".equals(bizType)) {
            result = "旅游";
        }
        return result;
    }

    // 获取上收月供
    private String getTakePayment(String takePayment) {
        String result = null;
        if ("1".equals(takePayment)) {
            result = "是";
        } else if ("0".equals(takePayment)) {
            result = "否";
        }
        return result;
    }

    // 获取订单状态
    private String getRiskStatus(String riskStatus) {
        String result = null;
        if ("0".equals(riskStatus)) {
            result = "审核通过";
        } else if ("1".equals(riskStatus)) {
            result = "审核拒绝";
        } else if ("2".equals(riskStatus)) {
            result = "审核中";
        } else if ("3".equals(riskStatus)) {
            result = "已分期";
        } else if ("4".equals(riskStatus)) {
            result = "待支付";
        } else if ("5".equals(riskStatus)) {
            result = "待确认";
        }
        return result;
    }

    // // 获取商户权限的Objs
    // private JSONObject getMerchLimitObject(JSONObject objs) {
    // // 权限树修改
    // String merchName = objs.getString("merchName");
    // if (merchName == null || "".equals(merchName) ||
    // "全部商户".equals(merchName)) { // 未选择从session中获取
    // List<MerchantBean> merchList = getAllMerListByOrgCode(objs);
    // if (merchList == null || merchList.size() == 0) {
    // return null;
    // }
    // objs.put("merchList", merchList);
    // } else {
    // objs.put("merchantShortName", objs.getString("merchName"));
    // String merchantNo = merchantBiz.getMerCodeByMerSortName(objs);
    // MerchantBean mb = new MerchantBean();
    // mb.setMerchantNo(merchantNo);
    // List<MerchantBean> merchList = new ArrayList<MerchantBean>();
    // merchList.add(mb);
    // objs.put("merchList", merchList);
    // }
    // return objs;
    // }
    //
    // // 获取登录商户下所有子商户列表
    // private List<MerchantBean> getAllMerListByOrgCode(JSONObject objs) {
    // String orgCode = sysUserSession.getOrgCode();
    // objs.put("id", orgCode);
    // List<MerchantBean> list = merchantBiz.getAllMerByID(objs);
    // return list;
    // }
}
