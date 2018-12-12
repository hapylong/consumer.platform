package com.iqb.consumer.service.layer.exchange;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.jys.JYSCreditInfoBean;
import com.iqb.consumer.data.layer.bean.jys.JYSDictInfo;
import com.iqb.consumer.data.layer.bean.jys.JYSOrderBean;
import com.iqb.consumer.data.layer.bean.jys.JYSPackInfo;
import com.iqb.consumer.data.layer.bean.jys.JYSUserBean;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.bean.plan.SysDictItem;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.consumer.data.layer.biz.order.JysCreditInfoBiz;
import com.iqb.consumer.data.layer.biz.order.JysDictInfoBiz;
import com.iqb.consumer.data.layer.biz.order.JysOrderBiz;
import com.iqb.consumer.data.layer.biz.order.JysPackInfoBiz;
import com.iqb.consumer.data.layer.biz.sys.SysCoreDictItemBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;
import com.iqb.consumer.service.layer.common.CalculateService;

@Service
public class AssetServiceImpl implements IAssetService {

    protected static Logger logger = LoggerFactory.getLogger(AssetServiceImpl.class);

    @Resource
    private MerchantBeanBiz merchantBeanBiz;
    @Resource
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private JysPackInfoBiz jysPackInfoBiz;
    @Resource
    private JysDictInfoBiz jysDictInfoBiz;
    @Resource
    private JysOrderBiz jysOrderBiz;
    @Resource
    private SysCoreDictItemBiz sysCoreDictItemBiz;
    @Resource
    private CalculateService calculateService;
    @Resource
    private JysCreditInfoBiz jysCreditInfoBiz;

    @Override
    @Transactional
    public Map<String, Object> assetXlsImport(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String[]> errPositions = new ArrayList<String[]>();// 错误位置集合
        List<JYSOrderBean> orderList = new ArrayList<>();// 交易所订单集合
        List<JYSUserBean> userList = new ArrayList<>();// 交易所用户集合
        long count = 0;// 导入数据条数
        String batchNo = System.currentTimeMillis() + "";// 每次导入的批次号相同

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
                        JYSOrderBean ob = new JYSOrderBean();
                        ob.setBatchNo(batchNo);// 统一的批次号
                        JYSUserBean ub = new JYSUserBean();
                        // 遍历每一列
                        for (int c = 0; c < cellCount; c++) {
                            Cell cell = row.getCell(c);
                            String cellValue = this.getCell(cell);
                            // 将value进行校验并存入组装对象,如有错误返回错误位置
                            this.validateXls(r, c, cellValue, errPositions, orderList, userList, ob, ub);// 校验xls并赋值初始ob,ub
                        }
                        if (errPositions.size() == 0) {
                            PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(Long.parseLong(ob.getPlanId()));
                            Map<String, BigDecimal> detail =
                                    calculateService.calculateAmt(planBean, new BigDecimal(ob.getOrderAmt()));// this.getDetail(new
                                                                                                              // BigDecimal(ob.getOrderAmt()),
                                                                                                              // planBean);
                            this.fullTheOrderBean(detail, ob, ub);// 完善订单bean
                            orderList.add(ob);
                            userList.add(ub);
                        }
                    }

                    if (errPositions.size() > 0) {
                        // 转换List为错误信息
                        String msg = transferErrPositionsToMsg(errPositions);
                        result.put("retMsg", msg);
                        result.put("retCode", "error");
                        return result;
                    }

                    // 将订单和人员集合批量插入
                    orderBiz.batchInsertJysOrder(orderList);
                    // userBeanBiz.batchInsertJysUser(userList);
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

    // 补全订单表
    private void fullTheOrderBean(Map<String, BigDecimal> detailMap, JYSOrderBean ob, JYSUserBean ub) {
        BigDecimal downPayment = detailMap.get("downPayment");// 首付
        BigDecimal serviceFee = detailMap.get("serviceFee");// 服务费
        BigDecimal margin = detailMap.get("margin");// 保证金
        // BigDecimal leftAmt = detailMap.get("leftAmt");// 剩余期数
        BigDecimal feeAmount = detailMap.get("feeAmount");// 上收利息
        BigDecimal monthMake = detailMap.get("monthMake");// 月供
        // BigDecimal monthAmount = detailMap.get("monthAmount");// 上收月供
        BigDecimal preAmount = detailMap.get("preAmount");// 预付金

        ob.setDownPayment(downPayment + "");
        ob.setServiceFee(serviceFee + "");
        ob.setMargin(margin + "");
        ob.setFeeAmount(feeAmount);
        ob.setMonthInterest(monthMake);
        ob.setPreAmt(preAmount + "");

        String orderId = orderBiz.generateOrderId(ob.getMerchantNo(), ob.getBizType());
        ob.setOrderId(orderId);
        ob.setStatus("1");

        // 插入user表并返回主键
        long userId = userBeanBiz.insertJysUser(ub);
        ob.setUserId(userId + "");
        /**
         * 资产导入时增加债权人信息 FINANCE-2283 交易所---债权人信息优化 2017-11-06 haojinlong
         */
        long creditorId = saveCreditInfo(orderId, ub);
        ob.setCreditorId(creditorId);
    }

    /**
     * Description:保存债权人信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private long saveCreditInfo(String orderId, JYSUserBean ub) {
        JYSCreditInfoBean creditInfoBean = new JYSCreditInfoBean();
        BeanUtils.copyProperties(ub, creditInfoBean);
        creditInfoBean.setOrderId(orderId);
        return jysCreditInfoBiz.insertJysCreditInfo(creditInfoBean);
    }

    // 校验Xls内容格式
    private void validateXls(int r, int c, String cellValue, List<String[]> errPositions, List<JYSOrderBean> orderList,
            List<JYSUserBean> userList, JYSOrderBean ob, JYSUserBean ub) {
        String[] errposition = null;// {"3","2", "errMsg"} -> 第三行第二列和errMsg信息
        // 根据列校验值
        switch (c) {
            case 1:// 手机号
                errposition = this.validateRegIdByLength(r + 1, c + 1, cellValue, ob, ub);
                break;
            case 2:// 姓名
                errposition = this.validateAndSetUserName(r + 1, c + 1, cellValue, ub);
                break;
            case 3:// 身份证
                errposition = this.validateAndSetIdNo(r + 1, c + 1, cellValue, ub);
                break;
            case 4:// 订单名称
                ob.setOrderName(cellValue);
                break;
            case 5:// 金额
                errposition = this.validateAndSetOrderAmt(r + 1, c + 1, cellValue, ob);
                break;
            case 6:// 业务类型
                errposition = this.validateAndSetBizType(r + 1, c + 1, cellValue, ob, ub);
                break;
            case 7:// 商户
                errposition = this.validateAndSetMerchantNo(r + 1, c + 1, cellValue, ob);
                break;
            case 8:// 到期日
                errposition = this.validateDatePattern(r + 1, c + 1, cellValue, ob, "expireDate");
                break;
            case 9:// 订单时间
                errposition = this.validateDatePattern(r + 1, c + 1, cellValue, ob, "createTime");
                break;
            case 10:// 分期方案ID
                errposition = this.validateAndSetPlanId(r + 1, c + 1, cellValue, ob);
                break;
            case 12:// 银行名称
                ub.setBankName(cellValue);
                break;
            case 13:// 银行卡号
                errposition = this.validateAndSetBankCardNo(r + 1, c + 1, cellValue, ub);
                break;
            case 14:// 债权人姓名
                errposition = this.validateAndSetCreditName(r + 1, c + 1, cellValue, ub);
                break;
            case 15:// 债权人身份证号
                errposition = this.validateAndSetCreditCardNo(r + 1, c + 1, cellValue, ub);
                break;
            case 16:// 债权人银行卡号
                errposition = this.validateAndSetCreditBankCard(r + 1, c + 1, cellValue, ub);
                break;
            case 17:// 债权人开户行
                errposition = this.validateAndSetCreditBank(r + 1, c + 1, cellValue, ub);
                break;
            case 18:// 债权人手机号
                errposition = this.validateAndSetCreditPhone(r + 1, c + 1, cellValue, ub);
                break;
        }
        if (errposition != null) {
            errPositions.add(errposition);
        }
    }

    // 校验并设置银行卡号
    private String[] validateAndSetBankCardNo(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "银行卡号为空"};
        }
        // String reg_bankno = "^(\\d{16}|\\d{17}|\\d{18}|\\d{19})$";
        String reg_bankno = "^\\d+$";
        if (!Pattern.matches(reg_bankno, cellValue)) {
            return new String[] {r + "", c + "", "银行卡号格式不正确"};
        }
        ub.setBankCardNo(cellValue);
        return null;
    }

    // 校验并设置planID
    private String[] validateAndSetPlanId(int r, int c, String cellValue, JYSOrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "分期方案Id为空"};
        }
        // List<Long> list = qrCodeAndPlanBiz.getAllPlanIdList();
        long l = 0;
        try {
            l = Long.parseLong(cellValue);
        } catch (NumberFormatException e) {
            return new String[] {r + "", c + "", "分期方案Id不为数字"};
        }
        PlanBean planBean = qrCodeAndPlanBiz.getPlanByID(l);
        if (planBean == null) {
            return new String[] {r + "", c + "", "分期方案Id不存在"};
        }
        // if (!list.contains(l)) {
        // return new String[] { r + "", c + "", "分期方案Id不存在" };
        // }
        ob.setPlanId(cellValue);
        ob.setOrderItems(planBean.getInstallPeriods() + "");
        ob.setFee(planBean.getFeeRatio());
        return null;
    }

    // 校验日期格式
    private String[] validateDatePattern(int r, int c, String cellValue, JYSOrderBean ob, String dateType) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "日期为空"};
        }
        Date date = null;
        try {
            date = com.iqb.consumer.common.utils.DateUtil.parseDate(cellValue, "yyyyMMdd");
        } catch (Exception e) {
            return new String[] {r + "", c + "", "日期格式有误"};
        }
        if ("expireDate".equals(dateType)) {
            ob.setExpireDate(date);
        } else {
            ob.setCreateTime(date);
        }
        return null;
    }

    // 校验并设置商户号
    private String[] validateAndSetMerchantNo(int r, int c, String cellValue, JYSOrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "商户号为空"};
        }
        // 从redis中读取商户列表，查找赋值
        List<String> list = merchantBeanBiz.getAllMerchantNoList();
        if (!list.contains(cellValue)) {
            return new String[] {r + "", c + "", "商户号有误"};
        }
        ob.setMerchantNo(cellValue);
        return null;
    }

    // 校验并设置BizType
    private String[] validateAndSetBizType(int r, int c, String cellValue, JYSOrderBean ob, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "业务类型为空"};
        }
        List<SysDictItem> list = sysCoreDictItemBiz.selBizType(cellValue);
        if (list == null || list.size() == 0) {
            return new String[] {r + "", c + "", "业务类型不存在"};
        }
        ob.setBizType(list.get(0).getDictValue());
        return null;
    }

    // 校验并设置订单金额
    private String[] validateAndSetOrderAmt(int r, int c, String cellValue, JYSOrderBean ob) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "订单金额为空"};
        }
        String reg_amt = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
        if (!Pattern.matches(reg_amt, cellValue)) {
            return new String[] {r + "", c + "", "订单金额信息正确"};
        }
        ob.setOrderAmt(cellValue);
        return null;
    }

    // 校验并设置身份证
    private String[] validateAndSetIdNo(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "身份证为空"};
        }
        String reg_idNo =
                "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$)";
        if (!Pattern.matches(reg_idNo, cellValue)) {
            return new String[] {r + "", c + "", "身份证格式有误"};
        }
        ub.setIdNo(cellValue);
        return null;
    }

    // 校验并设置用户姓名
    private String[] validateAndSetUserName(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "用户姓名为空"};
        }
        ub.setRealName(cellValue);
        return null;
    }

    // 校验并设置手机号信息
    private String[] validateAndSetRegId(int r, int c, String cellValue, JYSOrderBean ob, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "手机号为空"};
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(cellValue);
        if (!m.matches()) {
            return new String[] {r + "", c + "", "手机号格式有误"};
        }
        ob.setRegId(cellValue);
        ub.setRegId(cellValue);
        return null;
    }

    // 校验并设置手机号信息
    private String[] validateRegIdByLength(int r, int c, String cellValue, JYSOrderBean ob, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "手机号为空"};
        }
        if (cellValue.length() != 11) {
            return new String[] {r + "", c + "", "手机号格式有误"};
        }
        ob.setRegId(cellValue);
        ub.setRegId(cellValue);
        return null;
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
                    // cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
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

    /**
     * FINANCE-3187 交易所打包时双击造成订单重复
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2018年4月3日
     */
    @Override
    public int insertPackInfo(JSONObject objs) {
        JYSOrderBean JYSOrderBean = jysOrderBiz.getSingleOrderInfo(objs);
        if (JYSOrderBean != null) {
            if (Integer.parseInt(JYSOrderBean.getStatus()) != 4) {
                JYSPackInfo jysPackInfo = JSONObject.toJavaObject(objs, JYSPackInfo.class);
                jysOrderBiz.updateOrderStatus("4", objs.getString("id"));
                jysPackInfo.setJysOrderId(objs.getLongValue("id"));
                return jysPackInfoBiz.insertPackInfo(jysPackInfo);
            } else {
                return -2;
            }
        }
        return -1;
    }

    @Override
    public List<JYSDictInfo> queryAllByKey(JSONObject objs) {
        return jysDictInfoBiz.queryDictByKey(objs.getString("key"));
    }

    /**
     * Description:校验债权人姓名
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetCreditName(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "债权人姓名为空"};
        }
        ub.setCreditName(cellValue);
        return null;
    }

    /**
     * Description:校验债权人手机号码
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetCreditPhone(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "债权人手机号为空"};
        }
        if (cellValue.length() != 11) {
            return new String[] {r + "", c + "", "债权人手机号格式有误"};
        }
        ub.setCreditPhone(cellValue);
        return null;
    }

    /**
     * Description:校验债权人开户行
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetCreditBank(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "债权人开户行为空"};
        }
        ub.setCreditBank(cellValue);
        return null;
    }

    /**
     * Description:校验债权人银行卡号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetCreditBankCard(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "债权人银行卡号为空"};
        }
        String reg_bankno = "^\\d+$";
        if (!Pattern.matches(reg_bankno, cellValue)) {
            return new String[] {r + "", c + "", "债权人银行卡号格式不正确"};
        }
        ub.setCreditBankCard(cellValue);
        return null;
    }

    /**
     * Description:校验债权人身份证号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetCreditCardNo(int r, int c, String cellValue, JYSUserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "债权人身份证为空"};
        }
        String reg_idNo =
                "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$)";
        if (!Pattern.matches(reg_idNo, cellValue)) {
            return new String[] {r + "", c + "", "债权人身份证格式有误"};
        }
        ub.setCreditCardNo(cellValue);
        return null;
    }

}
