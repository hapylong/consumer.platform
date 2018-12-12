package com.iqb.consumer.service.layer.orderinfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.crm.customer.bean.CustomerBean;
import com.iqb.consumer.crm.customer.biz.CustomerBiz;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderAmtDetailBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.order.OrderOtherInfo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.sys.SysCoreDictItemBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.data.layer.biz.wf.OrderOtherInfoBiz;
import com.iqb.consumer.service.layer.common.CalculateService;
import com.iqb.etep.common.utils.DateTools;

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
 * 2017年12月7日下午4:52:18 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    protected static Logger logger = LoggerFactory.getLogger(OrderInfoServiceImpl.class);
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private SysCoreDictItemBiz sysCoreDictItemBiz;
    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private CalculateService calculateService;
    @Resource
    private OrderOtherInfoBiz orderOtherInfoBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private CustomerBiz customerBiz;

    /**
     * 保存订单信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月7日
     */
    @Override
    public int saveOrderInfo(OrderBean orderBean) {

        return orderBiz.saveOrderInfo(orderBean);
    }

    /**
     * 订单信息导入分页查询
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月7日
     */
    @Override
    public PageInfo<OrderBean> selectOrderInfoForImport(JSONObject objs) {
        List<OrderBean> orderList = orderBiz.selectOrderInfoForImport(objs);
        return new PageInfo<>(orderList);
    }

    /**
     * 
     * Description:订单信息导入
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月7日
     */
    @SuppressWarnings("unused")
    @Override
    @Transactional
    public Map<String, Object> orderInfoXlsImport(MultipartFile file, JSONObject objs) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String[]> errPositions = new ArrayList<String[]>();// 错误位置集合
        List<OrderBean> orderList = new ArrayList<>();// 订单信息集合
        List<OrderOtherInfo> orderOtherList = new ArrayList<>();
        long count = 0;// 导入数据条数
        if (file.isEmpty()) {
            result.put("retCode", "error");
            result.put("retMsg", "上传文件不存在");
            return result;
        } else {
            InputStream is = null;
            try {
                is = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(is); // 这种方式
                                                                // Excel2003/2007/2010都是可以处理的
                int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
                // 遍历每个Sheet
                for (int s = 0; s < sheetCount; s++) {
                    Sheet sheet = workbook.getSheetAt(s);
                    int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数
                    int cellCount = sheet.getColumnWidth(0); // 获取总列数
                    // 遍历每一行
                    for (int r = 1; r < rowCount; r++) {
                        Row row = sheet.getRow(r);
                        OrderBean ob = new OrderBean();
                        ob.setMerchantNo(objs.getString("merchantNo"));
                        ob.setBizType(objs.getString("bizType"));
                        ob.setImportFlag(1);
                        OrderOtherInfo ooi = new OrderOtherInfo();
                        ooi.setProjectPrefix(objs.getString("projectPrefix"));
                        // 遍历每一列
                        for (int c = 0; c < cellCount; c++) {
                            Cell cell = row.getCell(c);
                            String cellValue = this.getCell(cell);
                            // 将value进行校验并存入组装对象,如有错误返回错误位置
                            this.validateXls(r, c, cellValue, errPositions, orderList, orderOtherList, ob, ooi);// 校验xls并赋值初始ob,ub
                        }
                        if (errPositions.isEmpty()) {
                            Map<String, BigDecimal> detail = null;
                            if (ob.getPlanId() != null) {
                                PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(ob.getPlanId()));
                                detail = calculateService.calculateAmt(planBean, new BigDecimal(ob.getOrderAmt()));
                            }

                            this.fullTheOrderBean(detail, ob, ooi);// 完善订单bean

                            orderList.add(ob);
                            orderOtherList.add(ooi);
                        }
                    }

                    if (!errPositions.isEmpty()) {
                        // 转换List为错误信息
                        String msg = transferErrPositionsToMsg(errPositions);
                        result.put("retMsg", msg);
                        result.put("retCode", "error");
                        return result;
                    }

                    // 将订单集合批量插入
                    try {
                        orderBiz.batchInsertOrderInfo(orderList);
                    } catch (Exception e) {
                        logger.error("---订单信息批量导入报错---{}", e);
                    }

                    orderOtherInfoBiz.batchInsertOrderOtherInfo(orderOtherList);
                    count = rowCount - 1;// 总条数

                    result.put("retCode", "success");
                    result.put("totalCount", count);
                    return result;
                }
            } catch (Exception e) {
                logger.error("导入xls数据异常:{}", e);
                result.put("retCode", "error");
                result.put("retMsg", "导入xls数据");
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        logger.error("关闭文件流异常:{}", e);
                    }
                }
            }
        }

        return null;
    }

    // 补全订单表
    private void fullTheOrderBean(Map<String, BigDecimal> detailMap, OrderBean ob, OrderOtherInfo ooi) {
        if (!CollectionUtils.isEmpty(detailMap)) {
            BigDecimal downPayment =
                    detailMap.get("downPayment") != null ? detailMap.get("downPayment") : BigDecimal.ZERO;// 首付
            BigDecimal serviceFee = detailMap.get("serviceFee") != null ? detailMap.get("serviceFee") : BigDecimal.ZERO;// 服务费
            BigDecimal margin = detailMap.get("margin") != null ? detailMap.get("margin") : BigDecimal.ZERO;// 保证金
            BigDecimal feeAmount = detailMap.get("feeAmount") != null ? detailMap.get("feeAmount") : BigDecimal.ZERO;// 上收利息
            BigDecimal monthMake = detailMap.get("monthMake") != null ? detailMap.get("monthMake") : BigDecimal.ZERO;// 月供
            BigDecimal preAmount =
                    detailMap.get("preAmount") != detailMap.get("preAmount")
                            ? detailMap.get("preAmount")
                            : BigDecimal.ZERO;// 预付金

            ob.setDownPayment(downPayment + "");
            ob.setServiceFee(serviceFee + "");
            ob.setMargin(margin + "");
            ob.setFeeAmount(feeAmount);
            ob.setMonthInterest(monthMake);
            ob.setPreAmt(preAmount + "");
        } else {
            ob.setDownPayment("0.00");
            ob.setServiceFee("0.00");
            ob.setMargin("0.00");
            ob.setFeeAmount(BigDecimal.ZERO);
            ob.setMonthInterest(BigDecimal.ZERO);
            ob.setPreAmt("0.00");
        }

        String orderId = orderBiz.generateOrderIdForImport(ob.getMerchantNo(), ob.getBizType());
        ob.setOrderId(orderId);
        ob.setStatus("1");
        ob.setWfStatus(100);
        ob.setRiskStatus(0);

        /** 获取担保人信息 **/
        MerchantBean merchantBean = merchantBeanBiz.getMerByMerNo(ob.getMerchantNo());
        JSONObject objs = new JSONObject();
        objs.put("customerCode", merchantBean.getId());
        CustomerBean customerBean = customerBiz.getCustomerStoreInfoByCode(objs);

        String projectName = orderBiz.generateProjectNameByOrderId(ob.getMerchantNo(), ooi.getProjectPrefix());
        ooi.setOrderId(orderId);
        ooi.setProjectName(projectName);
        ooi.setProjectNo(getProjectNo(ob.getMerchantNo(), projectName));
        ooi.setGuarantee(customerBean.getCustomerName());
        ooi.setGuaranteeName(customerBean.getCorporateName());

    }

    // 获取元素数据
    private String getCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        int cellType = cell.getCellType();
        String cellValue = null;
        switch (cellType) {
            case Cell.CELL_TYPE_STRING: // 文本
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC: // 数字、日期
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = fmt.format(cell.getDateCellValue()); // 日期型
                } else {
                    cell.getNumericCellValue();
                    cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN: // 布尔型
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK: // 空白
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_ERROR: // 错误
                cellValue = "错误";
                break;
            case Cell.CELL_TYPE_FORMULA: // 公式
                cellValue = "错误";
                break;
            default:
                cellValue = "错误";
        }
        return cellValue;
    }

    // 转换List为错误信息
    private String transferErrPositionsToMsg(List<String[]> errPositions) {
        StringBuffer sb = new StringBuffer();
        for (String[] obj : errPositions) {
            sb.append("第").append(obj[0]).append("行,")
                    .append("第").append(obj[1]).append("列,")
                    .append(obj[2]).append(";");
        }
        String result = sb.toString().substring(0, sb.lastIndexOf(";"));
        return result;
    }

    // 校验Xls内容格式
    private void validateXls(int r, int c, String cellValue, List<String[]> errPositions, List<OrderBean> orderList,
            List<OrderOtherInfo> orderOtherList, OrderBean ob, OrderOtherInfo ub) {
        String[] errposition = null;
        // 根据列校验值
        switch (c) {
            case 1:// 姓名
                errposition = this.validateAndSetUserName(r + 1, c + 1, cellValue, ob);
                break;
            case 2:// 手机号码
                errposition = this.validateRegIdByLength(r + 1, c + 1, cellValue, ob);
                break;
            case 3:// 金额
                errposition = this.validateAndSetOrderAmt(r + 1, c + 1, cellValue, ob);
                break;
            case 4:// 期数
                errposition = this.validateAndSetOrderItems(r + 1, c + 1, cellValue, ob);
                break;
            case 5:// 订单名称
                errposition = this.validateAndSetOrderName(r + 1, c + 1, cellValue, ob);
                break;
            case 6:// 计划id
                errposition = this.validateAndSetPlanId(r + 1, c + 1, cellValue, ob);
                break;
            case 7:// 申请金额
                errposition = this.validateAndSetApplyAmt(r + 1, c + 1, cellValue, ob);
                break;
            case 8:// 评估金额
                errposition = this.validateAndSetAssessPrice(r + 1, c + 1, cellValue, ob);
                break;
        }
        if (errposition != null) {
            errPositions.add(errposition);
        }
    }

    // 校验期数
    @SuppressWarnings("unused")
    private String[] validateAndSetOrderItems(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "期数为空"};
        }
        long l = 0;
        try {
            l = Long.parseLong(cellValue);
        } catch (NumberFormatException e) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "期数不为数字"};
        }
        ob.setOrderItems(cellValue);
        return null;
    }

    // 校验产品计划
    private String[] validateAndSetPlanId(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue != null && !"".equals(cellValue)) {
            long l = 0;
            try {
                l = Long.parseLong(cellValue);
                ob.setPlanId(cellValue);
            } catch (NumberFormatException e) {
                return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "期数不为数字"};
            }
            PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(l);
            if (planBean == null) {
                return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "分期方案Id不存在"};
            }
        }
        return null;
    }

    // 校验并设置订单金额
    private String[] validateAndSetOrderAmt(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "订单金额为空"};
        }
        String reg_amt = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
        if (!Pattern.matches(reg_amt, cellValue)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "订单金额信息正确"};
        }
        ob.setOrderAmt(cellValue);
        return null;
    }

    // 校验并设置用户姓名
    private String[] validateAndSetUserName(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "用户姓名为空"};
        }
        ob.setRealName(cellValue);
        return null;
    }

    // 校验并设置手机号信息
    private String[] validateRegIdByLength(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "手机号为空"};
        }
        if (cellValue.length() != 11) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "手机号格式有误"};
        }
        JSONObject objs = new JSONObject();
        objs.put("regId", cellValue);
        objs.put("realName", ob.getRealName());
        List<UserBean> userList = userBeanBiz.selectInstUserInfo(objs);
        if (CollectionUtils.isEmpty(userList)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "该手机号对应的信息未导入"};
        }
        ob.setRegId(cellValue);
        ob.setUserId(String.valueOf(userList.get(0).getId()));
        return null;
    }

    // 校验并设置订单名称
    private String[] validateAndSetOrderName(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue != null && !"".equals(cellValue)) {
            ob.setOrderName(cellValue);
        }
        return null;
    }

    // 校验并设置申请金额
    private String[] validateAndSetApplyAmt(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue != null && !"".equals(cellValue)) {
            String reg_amt = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
            if (!Pattern.matches(reg_amt, cellValue)) {
                return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "申请金额正确"};
            }
            ob.setApplyAmt(new BigDecimal(cellValue));
        }
        return null;
    }

    // 校验并设置评估
    private String[] validateAndSetAssessPrice(int r, int c, String cellValue, OrderBean ob) {
        if (cellValue != null && !"".equals(cellValue)) {
            String reg_amt = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
            if (!Pattern.matches(reg_amt, cellValue)) {
                return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "申请金额正确"};
            }
            ob.setAssessPrice(cellValue);
        }
        return null;
    }

    private static String getProjectNo(String merchantNo, String projectName) {
        String prefix = merchantNo.toUpperCase().substring(0, 1);
        String projectNo = projectName.substring(projectName.indexOf(prefix), projectName.length());
        return projectNo;
    }

    /**
     * 
     * Description:批量删除订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月11日
     */
    public long updateLoanDateByOrderIds(JSONObject objs) {
        objs.put("status", "2");
        return orderBiz.updateLoanDateByOrderIds(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月14日
     */
    @Override
    public List<OrderBean> selectAllOrderList() {
        JSONObject objs = new JSONObject();
        return orderBiz.selectAllOrderList(objs);
    }

    /**
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年3月14日
     */
    @Override
    public int updateOrderItemsByOrderId(String orderId) {
        return orderBiz.updateOrderItemsByOrderId(orderId);
    }

    /**
     * 
     * Description:根据条件查询以租代购费用明细
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年5月10日
     */
    public PageInfo<OrderAmtDetailBean> selectOrderAmtDetailList(JSONObject objs) {
        List<OrderAmtDetailBean> orderList = orderBiz.selectOrderAmtDetailList(objs);
        if (!CollectionUtils.isEmpty(orderList)) {
            for (OrderAmtDetailBean bean : orderList) {
                OrderAmtDetailBean orderAmtDetailBean = orderBiz.getOrderAmtDetail(bean.getOrderId() + "X");
                if (orderAmtDetailBean != null) {
                    bean.setCarGpsAmt(orderAmtDetailBean.getCarGpsAmt() != null
                            ? orderAmtDetailBean.getCarGpsAmt()
                            : BigDecimal.ZERO);
                    bean.setCarRiskAmt(orderAmtDetailBean.getCarRiskAmt() != null
                            ? orderAmtDetailBean.getCarRiskAmt()
                            : BigDecimal.ZERO);
                    bean.setCarBusinessTaxAmt(orderAmtDetailBean.getCarBusinessTaxAmt() != null ? orderAmtDetailBean
                            .getCarBusinessTaxAmt() : BigDecimal.ZERO);
                    bean.setCarOtherAmt(orderAmtDetailBean.getCarOtherAmt() != null ? orderAmtDetailBean
                            .getCarOtherAmt() : BigDecimal.ZERO);
                    bean.setCarTaxAmt(orderAmtDetailBean.getCarTaxAmt() != null
                            ? orderAmtDetailBean.getCarTaxAmt()
                            : BigDecimal.ZERO);
                    bean.setCarserverAmt(orderAmtDetailBean.getCarserverAmt() != null ? orderAmtDetailBean
                            .getCarserverAmt() : BigDecimal.ZERO);
                }
            }
        }
        return new PageInfo<>(orderList);
    }

    /**
     * 以租代购费用明细导出
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年6月1日
     */
    @Override
    public String exportOrderAmtDetailList(JSONObject objs, HttpServletResponse response) {
        try {
            objs.put("isPageHelper", false);

            List<OrderAmtDetailBean> orderList = orderBiz.selectOrderAmtDetailList(objs);
            if (!CollectionUtils.isEmpty(orderList)) {
                for (OrderAmtDetailBean bean : orderList) {
                    OrderAmtDetailBean orderAmtDetailBean = orderBiz.getOrderAmtDetail(bean.getOrderId() + "X");
                    if (orderAmtDetailBean != null) {
                        bean.setCarGpsAmt(orderAmtDetailBean.getCarGpsAmt() != null
                                ? orderAmtDetailBean.getCarGpsAmt()
                                : BigDecimal.ZERO);
                        bean.setCarRiskAmt(orderAmtDetailBean.getCarRiskAmt() != null
                                ? orderAmtDetailBean.getCarRiskAmt()
                                : BigDecimal.ZERO);
                        bean.setCarBusinessTaxAmt(orderAmtDetailBean.getCarBusinessTaxAmt() != null
                                ? orderAmtDetailBean
                                        .getCarBusinessTaxAmt()
                                : BigDecimal.ZERO);
                        bean.setCarOtherAmt(orderAmtDetailBean.getCarOtherAmt() != null ? orderAmtDetailBean
                                .getCarOtherAmt() : BigDecimal.ZERO);
                        bean.setCarTaxAmt(orderAmtDetailBean.getCarTaxAmt() != null
                                ? orderAmtDetailBean.getCarTaxAmt()
                                : BigDecimal.ZERO);
                        bean.setCarserverAmt(orderAmtDetailBean.getCarserverAmt() != null ? orderAmtDetailBean
                                .getCarserverAmt() : BigDecimal.ZERO);
                    }
                }
            }
            // 2.导出excel表格
            HSSFWorkbook workbook = exportOrderList(orderList);
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
    private HSSFWorkbook exportOrderList(List<OrderAmtDetailBean> list) {
        String[] excelHeader =
        {"订单号", "商户名称", "姓名", "手机号", "订单时间", "金额", "产品方案", "期数", "GPS金额", "商业险",
                "交强险", "购置税", "其他费用", "支付GPS", "支付商业险", "支付交强险", "支付购置税", "车商服务费", "评估管理费", "考察费",
                "支付其他费用", "支付GPS", "支付商业险", "支付交强险", "支付购置税", "车商服务费", "支付其他费用"};
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("费用明细");

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
            OrderAmtDetailBean orderAmtDetailBean = (OrderAmtDetailBean) list.get(i);

            row.createCell(0).setCellValue(orderAmtDetailBean.getOrderId());
            row.createCell(1).setCellValue(orderAmtDetailBean.getMerchantName());
            row.createCell(2).setCellValue(orderAmtDetailBean.getRealName());
            row.createCell(3).setCellValue(orderAmtDetailBean.getRegId());
            row.createCell(4).setCellValue(orderAmtDetailBean.getCreateTime());

            row.createCell(5).setCellValue(
                    orderAmtDetailBean.getOrderAmt() == null ? 0 : orderAmtDetailBean.getOrderAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(6).setCellValue(orderAmtDetailBean.getPlanFullName());
            row.createCell(7).setCellValue(orderAmtDetailBean.getOrderItems());
            row.createCell(8).setCellValue(
                    orderAmtDetailBean.getGpsAmt() == null ? 0 : orderAmtDetailBean.getGpsAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(9).setCellValue(
                    orderAmtDetailBean.getBusinessTaxAmt() == null ? 0 : orderAmtDetailBean.getBusinessTaxAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            row.createCell(10).setCellValue(orderAmtDetailBean.getInsAmt() == null ? 0 : orderAmtDetailBean.getInsAmt()
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(11).setCellValue(orderAmtDetailBean.getTaxAmt() == null ? 0 : orderAmtDetailBean.getTaxAmt()
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(12).setCellValue(
                    orderAmtDetailBean.getOtherAmt() == null ? 0 : orderAmtDetailBean.getOtherAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(13).setCellValue(
                    orderAmtDetailBean.getPreGpsAmt() == null ? 0 : orderAmtDetailBean.getPreGpsAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(14).setCellValue(
                    orderAmtDetailBean.getPreBusinessTaxAmt() == null ? 0 : orderAmtDetailBean.getPreBusinessTaxAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            row.createCell(15).setCellValue(
                    orderAmtDetailBean.getPreRiskAmt() == null ? 0 : orderAmtDetailBean.getPreRiskAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(16).setCellValue(
                    orderAmtDetailBean.getPreTaxAmt() == null ? 0 : orderAmtDetailBean.getPreTaxAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(17).setCellValue(
                    orderAmtDetailBean.getServerAmt() == null ? 0 : orderAmtDetailBean.getServerAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(18).setCellValue(
                    orderAmtDetailBean.getAssessMsgAmt() == null ? 0 : orderAmtDetailBean.getAssessMsgAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(19).setCellValue(
                    orderAmtDetailBean.getInspectionAmt() == null ? 0 : orderAmtDetailBean.getInspectionAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            row.createCell(20).setCellValue(
                    orderAmtDetailBean.getPreOtherAmt() == null ? 0 : orderAmtDetailBean.getPreOtherAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(21).setCellValue(
                    orderAmtDetailBean.getCarGpsAmt() == null ? 0 : orderAmtDetailBean.getCarGpsAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(22).setCellValue(
                    orderAmtDetailBean.getCarBusinessTaxAmt() == null ? 0 : orderAmtDetailBean.getCarBusinessTaxAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(23).setCellValue(
                    orderAmtDetailBean.getCarRiskAmt() == null ? 0 : orderAmtDetailBean.getCarRiskAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            row.createCell(24).setCellValue(
                    orderAmtDetailBean.getCarTaxAmt() == null ? 0 : orderAmtDetailBean.getCarTaxAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            row.createCell(25).setCellValue(
                    orderAmtDetailBean.getCarserverAmt() == null ? 0 : orderAmtDetailBean.getCarserverAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            row.createCell(26).setCellValue(
                    orderAmtDetailBean.getCarOtherAmt() == null ? 0 : orderAmtDetailBean.getCarOtherAmt()
                            .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
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
}
