package com.iqb.consumer.data.layer.biz.admin;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.UrlConfig;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.admin.entity.CASOrgCodeRecordEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.InstOperateEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbSysOrganizationInfoEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.NewRoverdueEntity;
import com.iqb.consumer.data.layer.bean.admin.entity.RoverdueEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;
import com.iqb.consumer.data.layer.bean.admin.pojo.SpecialTableColumn;
import com.iqb.consumer.data.layer.bean.admin.pojo.TableColumnPair;
import com.iqb.consumer.data.layer.bean.admin.request.ChangePlanRequestMessage;
import com.iqb.consumer.data.layer.bean.admin.request.ChatToChangePlanRequestPojo;
import com.iqb.consumer.data.layer.bean.admin.request.ChatToChangePlanResponsePojo;
import com.iqb.consumer.data.layer.bean.contract.InstOrderContractEntity;
import com.iqb.consumer.data.layer.dao.OrderDao;
import com.iqb.consumer.data.layer.dao.admin.AdminRepository;
import com.iqb.consumer.data.layer.dao.contract.InstOrderContractRepository;
import com.iqb.etep.common.base.biz.BaseBiz;
import com.iqb.etep.common.utils.JSONUtil;

import jodd.util.StringUtil;

@Repository
public class AdminManager extends BaseBiz {

    private static final Logger logger = LoggerFactory.getLogger(AdminManager.class);
    @Autowired
    private InstOrderContractRepository instOrderContractRepository;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AdminRepository adminRepository;

    @Resource
    private UrlConfig urlConfig;

    private static Map<String, String> PLAN_CHANGE_MAP = new HashMap<>();
    static {
        PLAN_CHANGE_MAP.put(ChangePlanRequestMessage.PLAN_TARGET_RESET, "重置");
        PLAN_CHANGE_MAP.put(ChangePlanRequestMessage.PLAN_TARGET_STOP, "禁止");
    }

    private static Map<String, String> MERCHANT_NAME = new HashMap<>();

    public InstOrderContractEntity getIOCEByOid(String orderId) {
        return instOrderContractRepository.getIOCEByOid(orderId);
    }

    public String saveOrUpdateIOCE(InstOrderContractEntity ioce) {
        if (ioce.getContractDate() != null && ioce.getContractEndDate() != null) {
            if (ioce.getContractEndDate().getTime() <= ioce.getContractDate().getTime()) {
                return "请检查合同到期日.";
            }
        }
        InstOrderContractEntity ioce2 = instOrderContractRepository.getIOCEByOid(ioce.getOrderId());
        if (ioce2 == null) {
            // step 1 : verify
            String verifyInfo = checkLGLContractNo(ioce);
            if (!StringUtil.isEmpty(verifyInfo)) {
                return verifyInfo;
            }
            // step 2 : save
            InstOrderContractEntity ioce3 = new InstOrderContractEntity();
            ioce3.createIOCE(ioce);
            instOrderContractRepository.saveIOCE(ioce3);
            return "success";
        } else {
            // step 1 : verify
            String verifyInfo = checkLGLContractNo(ioce);
            if (!StringUtil.isEmpty(verifyInfo)) {
                return verifyInfo;
            }
            // step 2 : update
            ioce2.updateIOIE(ioce);
            instOrderContractRepository.updateIOCE(ioce2);
            return "success";
        }
    }

    /**
     * 
     * Description: 校验 loanContractNo 借款合同编号 || guarantyContractNo 担保合同编号 || leaseContractNo租赁合同编号
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午2:30:29
     */
    public String checkLGLContractNo(InstOrderContractEntity ioce) {
        if (!StringUtil.isEmpty(ioce.getLoanContractNo())
                && instOrderContractRepository.checkLoanContractNo(ioce.getOrderId(), ioce.getLoanContractNo()) != null) {
            return "借款合同编号已存在.";
        }
        if (!StringUtil.isEmpty(ioce.getGuarantyContractNo())
                && instOrderContractRepository.checkGuarantyContractNo(ioce.getOrderId(), ioce.getGuarantyContractNo()) != null) {
            return "担保合同编号已存在.";
        }
        if (!StringUtil.isEmpty(ioce.getLeaseContractNo())
                && instOrderContractRepository.checkLeaseContractNo(ioce.getOrderId(), ioce.getLeaseContractNo()) != null) {
            return "租赁合同编号已存在.";
        }
        return null;
    }

    public String chatToChangePlan(ChangePlanRequestMessage cprm, String openId) throws GenerallyException {
        ChatToChangePlanRequestPojo ctcp = orderDao.getCTCPByOid(cprm.getOrderId());
        ctcp.setOpenId(openId);
        if (ctcp == null || !ctcp.checkPojo()) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.SERVICE, Location.A);
        }
        String response;
        String url;
        switch (cprm.getTarget()) {
            case ChangePlanRequestMessage.PLAN_TARGET_RESET:
                ctcp.setBeginDate(cprm.getBeginDate());
                url = urlConfig.getPlanResetUrl();
                break;
            case ChangePlanRequestMessage.PLAN_TARGET_STOP:
                url = urlConfig.getPlanStopUrl();
                break;
            default:
                throw new GenerallyException(Reason.UNKNOW_TYPE, Layer.SERVICE, Location.A);
        }
        try {
            logger.info(String.format("AdminManager[chatToChangePlan.%s] request: %s", cprm.getTarget(),
                    JSONObject.toJSONString(ctcp)));
            response = SimpleHttpUtils.httpPost(
                    url,
                    SignUtil.chatEncode(JSONObject.toJSONString(ctcp), urlConfig.getCommonPrivateKey()));
            logger.info(String.format("AdminManager[chatToChangePlan.%s] response: %s", cprm.getTarget(),
                    JSONObject.toJSONString(response)));

            if (StringUtil.isEmpty(response)) {
                throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
            }
        } catch (Throwable e) {
            logger.error(String.format("AdminManager[chatToChangePlan.%s] error.", cprm.getTarget()), e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }
        ChatToChangePlanResponsePojo ctc = null;
        try {
            ctc = JSONObject.parseObject(response, ChatToChangePlanResponsePojo.class);
        } catch (Exception e) {
            logger.error("AdminManager[chatToChangePlan]  response anaysis error :", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.B);
        }
        if (ctc == null || !ctc.checkResponse()) {
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.C);
        }
        if (ctc.success()) {
            return String.format("分期%s-成功.", PLAN_CHANGE_MAP.get(cprm.getTarget()));
        } else {
            return String.format("账务系统返回【 %s 】", ctc.getRetMsg());
        }
    }

    /**
     * 
     * Description: 保存信息到 inst_operate 表中
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月2日 上午10:49:27
     */
    public void persistIOE(InstOperateEntity ioe) {
        setDb(0, super.MASTER);
        orderDao.persistIOE(ioe);
    }

    public IqbSysOrganizationInfoEntity getIDIEByOrgCodeAndOrgLevel(String orgCode) {
        setDb(0, super.MASTER);
        return adminRepository.getIDIEByOrgCodeAndOrgLevel(orgCode);
    }

    public int casChangeOrgCodeTCP(TableColumnPair tcp) {
        setDb(0, super.MASTER);
        return adminRepository.casChangeOrgCodeTCP(tcp);
    }

    public int casChangeOrgCodeSTC(SpecialTableColumn stc) {
        setDb(0, super.MASTER);
        return adminRepository.casChangeOrgCodeSTC(stc);
    }

    public int casUpdateMgLogMsg(String codeA, String codeB) {
        setDb(0, super.MASTER);
        return adminRepository.casUpdateMgLogMsg(codeA, codeB);
    }

    public void persistCOCRE(CASOrgCodeRecordEntity cre) {
        setDb(0, super.MASTER);
        adminRepository.persistCOCRE(cre);
    }

    public int casUpdateISOI(CASOrgCodeRecordEntity cre) {
        setDb(0, super.MASTER);
        return adminRepository.casUpdateISOI(cre);
    }

    public IqbCustomerPermissionEntity getICPEByMerchantNo(String merchantNo) {
        setDb(0, super.SLAVE);
        return adminRepository.getICPEByMerchantNo(merchantNo);
    }

    public List<MerchantTreePojo> getMerchantTreeList(Integer ConcatId) {
        setDb(0, super.SLAVE);
        return adminRepository.getMerchantTreeList(ConcatId);
    }

    public int saveOrUpdateICPE(IqbCustomerPermissionEntity icpe) {
        setDb(0, super.MASTER);
        return adminRepository.saveOrUpdateICPE(icpe);
    }

    public boolean isMerchantNoExit(String merchantNo) {
        setDb(0, super.SLAVE);
        return adminRepository.isMerchantNoExit(merchantNo) != 0;
    }

    public HSSFWorkbook convertInsideXlsx(List<RoverdueEntity> list) {

        String[] header =
        {"门店简称", "逾期月供", "逾期罚息", "逾期违约金", "资产总存量", "逾期率(%)", "逾期应还本金", "资产总存量", "逾期率（%）", "逾期应还本金", "资产总存量",
                "逾期率（%）", "逾期剩余本金", "资产总存量", "呆账率（%）", "逾期剩余本金", "资产总存量", "坏账率（%）"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }
        for (int i = 0; i < list.size(); i++) {
            hr = hs.createRow(i + 1);
            xu.append(hr, 0, convertMerchantName(list.get(i).getMerchantNo())); // 门店简称
            xu.append(hr, 1, list.get(i).getOverdueMonthMd5());// 逾期月供
            xu.append(hr, 2, list.get(i).getOverdueInterestMd5());// 逾期罚息
            xu.append(hr, 3, list.get(i).getFixedOverdueAmtMd5());// 逾期违约金
            xu.append(hr, 4, list.get(i).getCurRepayAmtMd5());// 本期应还总额
            xu.append(
                    hr,
                    5,
                    BigDecimalUtil.format(list.get(i).getOverdueMonthMd5()
                            .divide(list.get(i).getCurRepayAmtMd5(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal(100))));// OverdueRateMd5
            // 逾期率(%)
            // =
            // 逾期月供
            // /
            // 本期应还总额

            xu.append(hr, 6, list.get(i).getOverduePrincipalMb1()); // 逾期应还本金
            xu.append(hr, 7, list.get(i).getStockAmtMb1());// 资产总存量
            xu.append(
                    hr,
                    8,
                    BigDecimalUtil.format(list.get(i).getOverduePrincipalMb1()
                            .divide(list.get(i).getStockAmtMb1(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal(100))));// OverdueRateMb1
            // 逾期率（%）
            // =
            // 逾期应还本金/资产总存量

            xu.append(hr, 9, list.get(i).getOverduePrincipalMb2());// 逾期应还本金
            xu.append(hr, 10, list.get(i).getStockAmtMb2());// 资产总存量
            xu.append(
                    hr,
                    11,
                    BigDecimalUtil.format(list.get(i).getOverduePrincipalMb2()
                            .divide(list.get(i).getStockAmtMb2(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal(100))));// OverdueRateMb2
            // 逾期率（%）
            // =
            // 逾期应还本金
            // /
            // 资产总存量

            xu.append(hr, 12, list.get(i).getOverduePrincipalMb3());// 逾期剩余本金
            xu.append(hr, 13, list.get(i).getStockAmtMb3());// 资产总存量
            xu.append(
                    hr,
                    14,
                    BigDecimalUtil.format(list.get(i).getOverduePrincipalMb3()
                            .divide(list.get(i).getStockAmtMb3(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal(100)))); // OverdueRateMb3
            // 呆账率（%）
            // =
            // 逾期剩余本金
            // /
            // 资产总存量

            xu.append(hr, 15, list.get(i).getOverduePrincipalMb4()); // 逾期剩余本金
            xu.append(hr, 16, list.get(i).getStockAmtMb4()); // 资产总存量
            xu.append(
                    hr,
                    17,
                    BigDecimalUtil.format(list.get(i).getOverduePrincipalMb4()
                            .divide(list.get(i).getStockAmtMb4(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(new BigDecimal(100))));// OverdueRateMb4
            // 坏账率（%）
            // =
            // 逾期剩余本金/
            // 资产总存量
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    public HSSFWorkbook convertOutSideXlsx(List<RoverdueEntity> list) {
        String[] header =
        {"门店简称", "逾期月供", "逾期罚息", "逾期违约金", "资产总存量", "逾期率(%)", "逾期月供", "逾期罚息", "逾期违约金", "资产总存量", "逾期率（%）",
                "逾期应还本金", "资产总存量", "逾期率（%）", "逾期剩余本金", "资产总存量", "呆账率（%）", "逾期剩余本金", "资产总存量", "坏账率（%）"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }
        for (int i = 0; i < list.size(); i++) {
            hr = hs.createRow(i + 1);
            xu.append(hr, 0, convertMerchantName(list.get(i).getMerchantNo())); // 门店简称
            xu.append(hr, 1, list.get(i).getOverdueMonthMd5());// 逾期月供
            xu.append(hr, 2, list.get(i).getOverdueInterestMd5());// 逾期罚息
            xu.append(hr, 3, list.get(i).getFixedOverdueAmtMd5());// 逾期违约金
            xu.append(hr, 4, list.get(i).getCurRepayAmtMd5());// 本期应还总额
            xu.append(hr, 5, BigDecimalUtil.format(list.get(i).getOverdueMonthMd5()
                    .divide(list.get(i).getCurRepayAmtMd5(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100))));// OverdueRateMd5
                                                     // 逾期率(%)
                                                     // =
                                                     // 逾期月供/本期应还总额

            xu.append(hr, 6, list.get(i).getOverdueMonthMb1()); // 逾期月供
            xu.append(hr, 7, list.get(i).getOverdueInterestMb1());// 逾期罚息
            xu.append(hr, 8, list.get(i).getFixedOverdueAmtMb1());// 逾期违约金
            xu.append(hr, 9, list.get(i).getCurRepayAmtMb1());// 本期应还总额
            xu.append(hr, 10, BigDecimalUtil.format(list.get(i).getOverdueMonthMb1()
                    .divide(list.get(i).getCurRepayAmtMb1(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100))));// OutOverdueRateMb1逾期率（%）
                                                     // =
                                                     // 逾期月供/本期应还总额

            xu.append(hr, 11, list.get(i).getOverduePrincipalMb2());// 逾期应还本金
            xu.append(hr, 12, list.get(i).getStockAmtMb2());// 资产总存量
            xu.append(hr, 13, BigDecimalUtil.format(list.get(i).getOverduePrincipalMb2()
                    .divide(list.get(i).getStockAmtMb2(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))));// OverdueRateMb2逾期率（%）
            // =
            // 逾期应还本金/资产总存量

            xu.append(hr, 14, list.get(i).getOverduePrincipalMb3()); // 逾期剩余本金
            xu.append(hr, 15, list.get(i).getStockAmtMb3()); // 资产总存量
            xu.append(hr, 16, BigDecimalUtil.format(list.get(i).getOverduePrincipalMb3()
                    .divide(list.get(i).getStockAmtMb3(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)))); // OverdueRateMb3
            // 呆账率（%）
            // =
            // 逾期剩余本金/资产总存量
            xu.append(hr, 17, list.get(i).getOverduePrincipalMb4());// 逾期剩余本金
            xu.append(hr, 18, list.get(i).getStockAmtMb4());// 资产总存量
            xu.append(hr, 19, BigDecimalUtil.format(list.get(i).getOverduePrincipalMb4()
                    .divide(list.get(i).getStockAmtMb4(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100))));// OverdueRateMb4
            // 坏账率（%）=
            // 逾期剩余本金/资产总存量
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    public String convertMerchantName(String merchantNo) {
        String name = MERCHANT_NAME.get(merchantNo);
        if (StringUtil.isEmpty(name)) {
            name = adminRepository.getMerchantNameByNo(merchantNo);
            MERCHANT_NAME.put(merchantNo, name);
        }
        return name;
    }

    public HSSFWorkbook riskInsideXlsx(List<NewRoverdueEntity> newRoverdueEntityList,
            NewRoverdueEntity newRoverdueEntityTotal) {

        String[] header =
        {"商户", "门店逾期剩余本金", "门店资产存量", "逾期率", "剩余本金（逾期1-5天）", "逾期率(%)", "剩余本金（逾期6-30天）", "逾期率（%）", "剩余本金（逾期31-60天）",
                "逾期率（%）", "剩余本金（逾期61-90天）", "逾期率（%）", "剩余本金（逾期大于90天）", "逾期率（%）"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }

        /*
         * String string = JSON.toJSONString(xlsx); JSONObject parseObject =
         * JSON.parseObject(string); String string2 = parseObject.get("resultInside").toString();
         * NewRoverdueEntity newRoverdueEntityTotal = JSON.parseObject(string2.toString(),
         * NewRoverdueEntity.class);
         * 
         * String string3 = parseObject.get("result").toString(); JSONObject parseObject2 =
         * JSON.parseObject(string3); String jsonArray = parseObject2.get("list").toString();
         * List<NewRoverdueEntity> newRoverdueEntityList = JSON.parseArray(jsonArray,
         * NewRoverdueEntity.class);
         */
        if (newRoverdueEntityList != null && newRoverdueEntityList.size() != 0 && newRoverdueEntityList.get(0) != null
                && newRoverdueEntityTotal != null) {
            logger.debug("查询SIZE", newRoverdueEntityList.size());
            for (int i = 0; i < newRoverdueEntityList.size(); i++) {

                hr = hs.createRow(i + 1);
                hr.createCell(0).setCellValue(convertMerchantName(newRoverdueEntityList.get(i).getMerchantNo()));
                hr.createCell(1).setCellValue(
                        newRoverdueEntityList.get(i).getOverduePrincipal().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(2).setCellValue(
                        newRoverdueEntityList.get(i).getStockAmt().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                hr.createCell(3).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverduePrincipal(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(4).setCellValue(
                        newRoverdueEntityList.get(i).getOverduePrincipalMb5().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(5).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverduePrincipalMb5(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(6).setCellValue(
                        newRoverdueEntityList.get(i).getOverduePrincipalMb1().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(7).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverduePrincipalMb1(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(8).setCellValue(
                        newRoverdueEntityList.get(i).getOverduePrincipalMb2().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(9).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverduePrincipalMb2(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(10).setCellValue(
                        newRoverdueEntityList.get(i).getOverduePrincipalMb3().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(11).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverduePrincipalMb3(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(12).setCellValue(
                        newRoverdueEntityList.get(i).getOverduePrincipalMb4().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(13).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverduePrincipalMb4(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
            }
            // 总计列
            hr = hs.createRow(newRoverdueEntityList.size() + 1);
            hr.createCell(0).setCellValue("平台逾期总额");
            hr.createCell(1).setCellValue(
                    newRoverdueEntityTotal.getTotalOverduePrincipal().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(2).setCellValue(
                    newRoverdueEntityTotal.getTotalStockAmt().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(3).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueRate().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(4).setCellValue(
                    newRoverdueEntityTotal.getTotalOverduePrincipalMb5().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(5).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueRateMb5().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(6).setCellValue(
                    newRoverdueEntityTotal.getTotalOverduePrincipalMb1().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(7).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueRateMb1().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(8).setCellValue(
                    newRoverdueEntityTotal.getTotalOverduePrincipalMb2().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(9).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueRateMb2().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(10).setCellValue(
                    newRoverdueEntityTotal.getTotalOverduePrincipalMb3().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(11).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueRateMb3().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(12).setCellValue(
                    newRoverdueEntityTotal.getTotalOverduePrincipalMb4().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(13).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueRateMb4().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }

        // 设置列宽

        hs.setColumnWidth(0, 5000);
        for (int j = 1; j < header.length; j++) {
            hs.setColumnWidth(0, 3000);
        }
        return hwb;
    }

    public String exlToRate(BigDecimal biDecimal, BigDecimal totalBiDecimal) {
        if (biDecimal.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        return BigDecimalUtil.format(biDecimal.divide(
                totalBiDecimal, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String exlToIntRate(int biDecimal, int totalBiDecimal) {
        if (totalBiDecimal == 0) {
            return "0";
        }
        return BigDecimalUtil.formatInt((float) biDecimal
                / totalBiDecimal * 100).toString();
    }

    public HSSFWorkbook riskNOInsideXlsx(List<NewRoverdueEntity> newRoverdueEntityList,
            NewRoverdueEntity newRoverdueEntityTotal) {
        String[] header =
        {"商户", "门店逾期月还", "门店资产存量", "逾期率", "月供（逾期1-5天）", "逾期率(%)", "月供（逾期6-30天）", "逾期率（%）", "月供（逾期31-60天）",
                "逾期率（%）", "月供（逾期61-90天）", "逾期率（%）", "月供（逾期大于90天）", "逾期率（%）"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }

        /*
         * String string = JSON.toJSONString(xlsx); JSONObject parseObject =
         * JSON.parseObject(string); String string2 = parseObject.get("resultInside").toString();
         * NewRoverdueEntity newRoverdueEntityTotal = JSON.parseObject(string2.toString(),
         * NewRoverdueEntity.class);
         * 
         * String string3 = parseObject.get("result").toString(); JSONObject parseObject2 =
         * JSON.parseObject(string3); String jsonArray = parseObject2.get("list").toString();
         * List<NewRoverdueEntity> newRoverdueEntityList = JSON.parseArray(jsonArray,
         * NewRoverdueEntity.class);
         */
        if (newRoverdueEntityList != null && newRoverdueEntityList.size() != 0 && newRoverdueEntityList.get(0) != null
                && newRoverdueEntityTotal != null) {
            for (int i = 0; i < newRoverdueEntityList.size(); i++) {
                hr = hs.createRow(i + 1);
                hr.createCell(0).setCellValue(convertMerchantName(newRoverdueEntityList.get(i).getMerchantNo()));
                hr.createCell(1)
                        .setCellValue(
                                newRoverdueEntityList.get(i).getOverdueMonth().setScale(2, BigDecimal.ROUND_HALF_UP)
                                        .toString());
                hr.createCell(2).setCellValue(
                        newRoverdueEntityList.get(i).getStockAmt().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                hr.createCell(3).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverdueMonth(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(4).setCellValue(
                        newRoverdueEntityList.get(i).getOverdueMonthMb5().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(5).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverdueMonthMb5(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(6).setCellValue(
                        newRoverdueEntityList.get(i).getOverdueMonthMb1().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(7).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverdueMonthMb1(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(8).setCellValue(
                        newRoverdueEntityList.get(i).getOverdueMonthMb2().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(9).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverdueMonthMb2(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(10).setCellValue(
                        newRoverdueEntityList.get(i).getOverdueMonthMb3().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(11).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverdueMonthMb3(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
                hr.createCell(12).setCellValue(
                        newRoverdueEntityList.get(i).getOverdueMonthMb4().setScale(2, BigDecimal.ROUND_HALF_UP)
                                .toString());
                hr.createCell(13).setCellValue(
                        exlToRate(newRoverdueEntityList.get(i).getOverdueMonthMb4(), newRoverdueEntityList.get(i)
                                .getStockAmt()));
            }
            // 总计列
            hr = hs.createRow(newRoverdueEntityList.size() + 1);
            hr.createCell(0).setCellValue("平台逾期总额");
            hr.createCell(1).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonth().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(2).setCellValue(
                    newRoverdueEntityTotal.getTotalStockAmt().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(3).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthRate().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(4).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthMb5().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(5).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthRateMb5().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(6).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthMb1().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(7).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthRateMb1().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(8).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthMb2().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(9).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthRateMb2().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(10).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthMb3().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(11).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthRateMb3().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
            hr.createCell(12).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthMb4().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            hr.createCell(13).setCellValue(
                    newRoverdueEntityTotal.getTotalOverdueMonthRateMb4().setScale(2, BigDecimal.ROUND_HALF_UP)
                            .toString());
        }

        // 设置列宽

        hs.setColumnWidth(0, 5000);
        for (int j = 1; j < header.length; j++) {
            hs.setColumnWidth(0, 3000);
        }
        return hwb;
    }

    public HSSFWorkbook riskIntXlsx(List<NewRoverdueEntity> newRoverdueEntityList,
            NewRoverdueEntity newRoverdueEntityTotal) {
        String[] header =
        {"商户", "门店逾期案件个数", "门店案件存量", "逾期率", "逾期订单个数（逾期1-5天）", "逾期率(%)", "逾期订单个数（逾期6-30天）", "逾期率（%）",
                "逾期订单个数（逾期31-60天）",
                "逾期率（%）", "逾期订单个数（逾期61-90天）", "逾期率（%）", "逾期订单个数（逾期大于90天）", "逾期率（%）"};
        HSSFWorkbook hwb = new HSSFWorkbook();
        HSSFSheet hs = hwb.createSheet("账单信息");

        HSSFRow hr = hs.createRow(0);
        XlsxUtil xu = new XlsxUtil(hwb);
        for (int i = 0; i < header.length; i++) {
            HSSFCell cell = hr.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(xu.getTimeFormat());
            hs.autoSizeColumn(i);
        }

        /*
         * String string = JSON.toJSONString(xlsx); JSONObject parseObject =
         * JSON.parseObject(string); String string2 = parseObject.get("resultInside").toString();
         * NewRoverdueEntity newRoverdueEntityTotal = JSON.parseObject(string2.toString(),
         * NewRoverdueEntity.class);
         * 
         * String string3 = parseObject.get("result").toString(); JSONObject parseObject2 =
         * JSON.parseObject(string3); String jsonArray = parseObject2.get("list").toString();
         * List<NewRoverdueEntity> newRoverdueEntityList = JSON.parseArray(jsonArray,
         * NewRoverdueEntity.class);
         */
        if (newRoverdueEntityList != null && newRoverdueEntityList.size() != 0 && newRoverdueEntityList.get(0) != null
                && newRoverdueEntityTotal != null) {
            for (int i = 0; i < newRoverdueEntityList.size(); i++) {
                hr = hs.createRow(i + 1);
                hr.createCell(0).setCellValue(convertMerchantName(newRoverdueEntityList.get(i).getMerchantNo()));
                hr.createCell(1).setCellValue(newRoverdueEntityList.get(i).getOverdueNum());
                hr.createCell(2).setCellValue(newRoverdueEntityList.get(i).getOverdueStockNum());
                hr.createCell(3).setCellValue(
                        exlToIntRate(newRoverdueEntityList.get(i).getOverdueNum(), newRoverdueEntityList.get(i)
                                .getOverdueStockNum()));
                hr.createCell(4).setCellValue(newRoverdueEntityList.get(i).getOverdueNumMb5());
                hr.createCell(5).setCellValue(
                        exlToIntRate(newRoverdueEntityList.get(i).getOverdueNumMb5(), newRoverdueEntityList.get(i)
                                .getOverdueStockNum()));
                hr.createCell(6).setCellValue(newRoverdueEntityList.get(i).getOverdueNumMb1());
                hr.createCell(7).setCellValue(
                        exlToIntRate(newRoverdueEntityList.get(i).getOverdueNumMb1(), newRoverdueEntityList.get(i)
                                .getOverdueStockNum()));
                hr.createCell(8).setCellValue(newRoverdueEntityList.get(i).getOverdueNumMb2());
                hr.createCell(9).setCellValue(
                        exlToIntRate(newRoverdueEntityList.get(i).getOverdueNumMb2(), newRoverdueEntityList.get(i)
                                .getOverdueStockNum()));
                hr.createCell(10).setCellValue(newRoverdueEntityList.get(i).getOverdueNumMb3());
                hr.createCell(11).setCellValue(
                        exlToIntRate(newRoverdueEntityList.get(i).getOverdueNumMb3(), newRoverdueEntityList.get(i)
                                .getOverdueStockNum()));
                hr.createCell(12).setCellValue(newRoverdueEntityList.get(i).getOverdueNumMb4());
                hr.createCell(13).setCellValue(
                        exlToIntRate(newRoverdueEntityList.get(i).getOverdueNumMb4(), newRoverdueEntityList.get(i)
                                .getOverdueStockNum()));
            }
            // 总计列
            hr = hs.createRow(newRoverdueEntityList.size() + 1);
            hr.createCell(0).setCellValue("平台逾期个数总额");
            hr.createCell(1).setCellValue(newRoverdueEntityTotal.getTotalOverdueNum());
            hr.createCell(2).setCellValue(newRoverdueEntityTotal.getTotalOverdueStockNum());
            hr.createCell(3).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumRate().toString());
            hr.createCell(4).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumMb5());
            hr.createCell(5).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumRateMb5().toString());
            hr.createCell(6).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumMb1());
            hr.createCell(7).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumhRateMb1().toString());
            hr.createCell(8).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumMb2());
            hr.createCell(9).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumRateMb2().toString());
            hr.createCell(10).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumMb3());
            hr.createCell(11).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumRateMb3().toString());
            hr.createCell(12).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumMb4());
            hr.createCell(13).setCellValue(newRoverdueEntityTotal.getTotalOverdueNumRateMb4().toString());
        }

        // 设置列宽

        hs.setColumnWidth(0, 5000);
        for (int j = 1; j < header.length; j++) {
            hs.setColumnWidth(0, 3000);
        }

        return hwb;
    }

}
