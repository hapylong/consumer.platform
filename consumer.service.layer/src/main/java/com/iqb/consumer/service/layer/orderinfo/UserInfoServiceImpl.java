package com.iqb.consumer.service.layer.orderinfo;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.bank.BankCardBean;
import com.iqb.consumer.data.layer.bean.user.UserBean;
import com.iqb.consumer.data.layer.biz.bank.BankCardBeanBiz;
import com.iqb.consumer.data.layer.biz.user.UserBeanBiz;

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
 * 2017年12月11日上午11:44:34 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    protected static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Resource
    private UserBeanBiz userBeanBiz;
    @Resource
    private BankCardBeanBiz bankCardBeanBiz;

    /**
     * 批量插入用户信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月11日
     */
    @SuppressWarnings("unused")
    @Override
    public Map<String, Object> userInfoXlsImport(MultipartFile file) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<String[]> errPositions = new ArrayList<String[]>();// 错误位置集合
        List<UserBean> userList = new ArrayList<>();// 用户信息集合
        List<BankCardBean> cardList = new ArrayList<>();// 卡息集合
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
                        UserBean ub = new UserBean();
                        BankCardBean bankCardBean = new BankCardBean();
                        // 遍历每一列
                        for (int c = 0; c < cellCount; c++) {
                            Cell cell = row.getCell(c);
                            String cellValue = this.getCell(cell);
                            // 将value进行校验并存入组装对象,如有错误返回错误位置
                            this.validateXls(r, c, cellValue, errPositions, userList, ub, bankCardBean);// 校验xls并赋值初始ob,ub
                        }
                        if (errPositions.isEmpty()) {
                            userList.add(ub);
                            cardList.add(bankCardBean);
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
                    userBeanBiz.batchInsertUser(userList);
                    count = rowCount - 1;// 总条数
                    // 存储userId的卡信息集合
                    List<BankCardBean> tempCardList = new ArrayList<>();
                    for (UserBean userBean : userList) {
                        JSONObject objs = new JSONObject();
                        objs.put("regId", userBean.getRegId());
                        objs.put("idNo", userBean.getIdNo());
                        List<UserBean> list = userBeanBiz.selectInstUserInfo(objs);
                        if (!CollectionUtils.isEmpty(list)) {
                            UserBean user = list.get(0);

                            BankCardBean bankCardBean = new BankCardBean();

                            bankCardBean.setUserId(user.getId());
                            bankCardBean.setStatus(2);
                            bankCardBean.setBankMobile(user.getRegId());
                            tempCardList.add(bankCardBean);
                        }
                    }
                    List<BankCardBean> finalCardList = new ArrayList<>();// 卡息集合
                    // 合并导入的卡信息与tempCardList
                    for (BankCardBean cardLBean : cardList) {
                        for (BankCardBean tempCardLBean : tempCardList) {
                            if (cardLBean.getBankMobile().equals(tempCardLBean.getBankMobile())) {
                                cardLBean.setUserId(tempCardLBean.getUserId());
                                cardLBean.setStatus(tempCardLBean.getStatus());
                                finalCardList.add(cardLBean);
                            }
                        }
                    }
                    bankCardBeanBiz.batchSaveBankCard(finalCardList);
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
    private void validateXls(int r, int c, String cellValue, List<String[]> errPositions, List<UserBean> userList,
            UserBean ub, BankCardBean bankCardBean) {
        String[] errposition = null;
        // 根据列校验值
        switch (c) {
            case 1:// 姓名
                errposition = this.validateAndSetUserName(r + 1, c + 1, cellValue, ub);
                break;
            case 2:// 手机号码
                errposition = this.validateRegIdByLength(r + 1, c + 1, cellValue, ub, bankCardBean);
                break;
            case 3:// 身份证号码
                errposition = this.validateAndSetIdNo(r + 1, c + 1, cellValue, ub);
                break;
            case 4:// 开户行
                errposition = this.validateAndSetBank(r + 1, c + 1, cellValue, bankCardBean);
                break;
            case 5:// 银行卡卡号
                errposition = this.validateAndSetBankCard(r + 1, c + 1, cellValue, bankCardBean);
                break;
        }
        if (errposition != null) {
            errPositions.add(errposition);
        }
    }

    // 校验并设置用户姓名
    private String[] validateAndSetUserName(int r, int c, String cellValue, UserBean ub) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {Integer.toString(r) + "", Integer.toString(c) + "", "用户姓名为空"};
        }
        ub.setRealName(cellValue);
        return null;
    }

    // 校验并设置手机号信息
    private String[] validateRegIdByLength(int r, int c, String cellValue, UserBean ub, BankCardBean bankCardBean) {
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "手机号为空"};
        }
        if (cellValue.length() != 11) {
            return new String[] {r + "", c + "", "手机号格式有误"};
        }
        ub.setRegId(cellValue);
        ub.setSmsMobile(cellValue);
        bankCardBean.setBankMobile(cellValue);
        return null;
    }

    // 校验并设置身份证
    private String[] validateAndSetIdNo(int r, int c, String cellValue, UserBean ub) {
        Map<String, Object> params = new HashMap<>();
        if (cellValue == null || "".equals(cellValue)) {
            return new String[] {r + "", c + "", "身份证为空"};
        }
        String reg_idNo =
                "^\\d{15}$|^\\d{17}[0-9A-Z]$";
        if (!Pattern.matches(reg_idNo, cellValue)) {
            reg_idNo =
                    "[1-9A-GY]{1}[1239]{1}[1-5]{1}[0-9]{5}[0-9A-Z]{10}";
            if (!Pattern.matches(reg_idNo, cellValue)) {
                return new String[] {r + "", c + "", "身份证格式有误"};
            }
        }
        params.put("regId", ub.getRegId());
        params.put("idNo", cellValue);
        UserBean userBean = userBeanBiz.selectInstUser(params);
        if (userBean != null) {
            return new String[] {r + "", c + "", "用户已存在"};
        }
        ub.setIdNo(cellValue);
        return null;
    }

    /**
     * Description:校验开户行
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetBank(int r, int c, String cellValue, BankCardBean bankCardBean) {
        if (cellValue != null && !"".equals(cellValue)) {
            bankCardBean.setBankName(cellValue);
        }
        return null;
    }

    /**
     * Description:校验银行卡号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年11月6日
     */
    private String[] validateAndSetBankCard(int r, int c, String cellValue, BankCardBean bankCardBean) {
        if (cellValue != null && !"".equals(cellValue)) {
            String reg_bankno = "^\\d+$";
            if (!Pattern.matches(reg_bankno, cellValue)) {
                return new String[] {r + "", c + "", "银行卡号格式不正确"};
            }
            bankCardBean.setBankCardNo(cellValue);
        }
        return null;
    }

    /**
     * 
     * 分页查询用户信息
     * 
     * @param params
     * @return
     * @Author haojinlong Create Date: 2017年12月11日
     */
    @Override
    public PageInfo<UserBean> selectInstUserInfo(JSONObject objs) {
        List<UserBean> userList = userBeanBiz.selectInstUserInfo(objs);
        return new PageInfo<>(userList);
    }
}
