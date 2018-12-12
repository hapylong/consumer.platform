package com.iqb.consumer.data.layer.biz.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.Constant;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.common.utils.XlsxUtil;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillQueryRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillRefundRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.HouseBillQueryResponsePojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillQueryRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillQueryResponsePojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillRefundRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillResult;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceRefundPiece;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.house.HouseOrderEntity;
import com.iqb.consumer.data.layer.bean.user.SysUserWechat;
import com.iqb.consumer.data.layer.dao.OrderDao;
import com.iqb.etep.common.utils.SysUserSession;

import jodd.util.StringUtil;

@Component
public class FinanceManager {

    private static final Logger log = LoggerFactory.getLogger(FinanceManager.class);

    @Autowired
    private OrderDao orderDao;

    @Resource
    private ConsumerConfig consumerConfig;

    @Resource
    private SysUserSession sysUserSession;

    public FinanceBillQueryRequestPojo createFBQR(BillQueryRequestPojo bqr, JSONObject objs)
            throws DevDefineErrorMsgException {
        // 获取商户集合
        List<MerchantBean> merchantList = (List<MerchantBean>) objs.get("merList");
        List<String> merList = new ArrayList<String>();
        for (int i = 0; i < merchantList.size(); i++) {
            merList.add(merchantList.get(i).getMerchantNo());
        }
        FinanceBillQueryRequestPojo fbqr = new FinanceBillQueryRequestPojo();
        fbqr.setCurRepayDate(DateUtil.parseYYYYMMDD(bqr.getRepayTime()));
        fbqr.setStartDate(DateUtil.parseYYYYMMDD(bqr.getCreateTime()));
        fbqr.setEndDate(DateUtil.parseYYYYMMDD(bqr.getEndTime()));
        fbqr.setLastRepayDate(DateUtil.parseYYYYMMDD(bqr.getLastRepayTime()));
        fbqr.setMerchantNos(merList);
        fbqr.setOpenId("1");
        fbqr.setOrderId(bqr.getOrderId());
        if (!StringUtil.isEmpty(bqr.getSongsong())) {
            List<String> orderIds = orderDao.getOidsBySongsong(bqr.getSongsong());
            if (orderIds != null && !orderIds.isEmpty()) {
                if (StringUtil.isEmpty(bqr.getOrderId())
                        || (!StringUtil.isEmpty(bqr.getOrderId()) && !orderIds.contains(bqr.getOrderId()))) {
                    fbqr.setOrderId(createFinanceOids(orderIds));
                }
            } else {
                // 如果经纪人没有订单 则直接返回 null
                return null;
            }
        }
        fbqr.setRegId(bqr.getRegId());
        fbqr.setRealName(bqr.getRealName());
        fbqr.setStatus(convertStatus(bqr.getType()));
        fbqr.setNumPerPage(bqr.getPageSize());
        fbqr.setPageNum(bqr.getPageNum());
        return fbqr;
    }

    private String createFinanceOids(List<String> orderIds) {
        if (orderIds == null || orderIds.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String oid : orderIds) {
            sb.append(oid).append(",");
        }
        return sb.toString();
    }

    private String convertStatus(Integer status) throws DevDefineErrorMsgException {
        String result = null;
        if (status == null || status == 0) {
            return null;
        }
        switch (status) {
            case Constant.FINANCE_QUERY_BILL_LIST_DAIHUANKUAN:
                result = Constant.FINANCE_QUERY_STATUS_DAIHUANKUAN;
                break;
            case Constant.FINANCE_QUERY_BILL_LIST_PAGE_DAIHUANKUAN:
                result = Constant.FINANCE_QUERY_STATUS_DAIHUANKUAN;
                break;
            case Constant.FINANCE_QUERY_BILL_LIST_YIHUANKUAN:
                result = Constant.FINANCE_QUERY_STATUS_YIHUANKUAN;
                break;
            case Constant.FINANCE_QUERY_BILL_LIST_PAGE_YIHUANKUAN:
                result = Constant.FINANCE_QUERY_STATUS_YIHUANKUAN;
                break;
            case Constant.FINANCE_QUERY_BILL_LIST_YIYUQI:
                result = Constant.FINANCE_QUERY_STATUS_YIYUQI;
                break;
            case Constant.FINANCE_QUERY_BILL_LIST_PAGE_YIYUQI:
                result = Constant.FINANCE_QUERY_STATUS_YIYUQI;
                break;
            default:
                throw new DevDefineErrorMsgException("invalid param type not define.");
        }
        return result;
    }

    public void convert(HouseBillQueryResponsePojo hbqr, FinanceBillPojo fbp) {
        hbqr.setCurRealRepayamt(fbp.getCurRealRepayamt());
        hbqr.setCurRepayAmt(fbp.getCurRepayAmt());
        hbqr.setCurRepayDate(fbp.getCurRepayDate());
        hbqr.setCurRepayOverdueInterest(fbp.getCurRepayOverdueInterest());
        hbqr.setCutOverdueInterest(fbp.getCutOverdueInterest());
        hbqr.setFix(fbp.getFix());
        hbqr.setInstallTerms(fbp.getInstallTerms());
        hbqr.setLastRepayDate(fbp.getLastRepayDate());
        hbqr.setMerchantNo(fbp.getMerchantNo());
        hbqr.setOrderId(fbp.getOrderId());
        hbqr.setOverdueDays(fbp.getOverdueDays());
        hbqr.setRealName(fbp.getRealName());
        hbqr.setRegId(fbp.getRegId());
        hbqr.setRepayNo(fbp.getRepayNo());

        hbqr.setStatus(fbp.getStatus());
        hbqr.setSubOrderId(fbp.getSubOrderId());

        hbqr.setInstallSumAmt(fbp.getInstallSumAmt());
        hbqr.setInstallAmt(fbp.getInstallAmt());

        BigDecimal dqAmt = (fbp.getCurRepayPrincipal() == null ? BigDecimal.ZERO : fbp.getCurRepayPrincipal())
                .add(fbp.getCurRepayInterest() == null ? BigDecimal.ZERO : fbp.getCurRepayInterest());
        hbqr.setDqAmt(dqAmt);

        try {
            HouseOrderEntity hoe = orderDao.querySongSongByOid(fbp.getOrderId(), fbp.getSubOrderId(), fbp.getRepayNo());
            // 渠道和经纪人姓名通过手机号和用户表关联
            SysUserWechat channelInfo = orderDao.getChannelInfo(fbp.getOrderId());
            hbqr.setSongsong(channelInfo == null ? "" : channelInfo.getUserName());// 渠道经纪人
            hbqr.setChannal(channelInfo == null ? "" : channelInfo.getChannelName());// 渠道名称
            hbqr.setSerialNumber(hoe == null ? "" : hoe.getTransctionNo());// 流水号
        } catch (Exception e) {
            hbqr.setSongsong("");// 渠道经纪人
            hbqr.setChannal("");// 渠道名称
            hbqr.setSerialNumber("");// 流水号
        }

    }

    public FinanceBillPojo getFinanceBill(HouseOrderEntity hoe, BillRefundRequestPojo brrq, String openId)
            throws GenerallyException, DevDefineErrorMsgException {
        FinanceBillQueryRequestPojo fbqr = new FinanceBillQueryRequestPojo();
        if (hoe == null) {
            throw new GenerallyException(Reason.INVALID_ENTITY, Layer.MANAGER, Location.A);
        }

        fbqr.setId(brrq.getId());
        fbqr.setOpenId(openId);
        fbqr.setOrderId(hoe.getOrderNo());
        fbqr.setRegId(hoe.getMobile());
        fbqr.setSubOrderId(brrq.getSubOrderId());
        fbqr.setStatus("5");
        fbqr.setRepayNo(brrq.getRepayNo());

        FinanceBillQueryResponsePojo response = null;
        try {
            String responseStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillGetPagelistUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(fbqr), consumerConfig.getCommonPrivateKey()));
            log.info("chat response str: " + responseStr);
            response = JSONObject.parseObject(responseStr, FinanceBillQueryResponsePojo.class);
        } catch (Exception e) {
            log.error("chat response error", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.MANAGER, Location.A);
        }

        if (response == null || response.getResult() == null || response.getResult().getRecordList() == null
                || response.getResult().getRecordList().isEmpty()) {
            return null;
        }

        if (response != null && response.getRetCode().equals("error")) {
            throw new DevDefineErrorMsgException(String.format("账务系统返回【%s】", response.getRetMsg()));
        }

        FinanceBillResult fbr = response.getResult();

        List<FinanceBillPojo> fbps = fbr.getRecordList();
        if (fbps != null && !fbps.isEmpty()) {
            return fbps.get(0);
        }
        return null;
    }

    public Map<String, String> createLog(BillRefundRequestPojo brrq, FinanceBillPojo fbp) {
        Map<String, String> map = new HashMap<>();

        String account = sysUserSession.getUserCode();

        BigDecimal curRepayAmt = BigDecimalUtil.mul(fbp.getCurRepayAmt(), new BigDecimal(100));
        map.put("tranTime", DateUtil.getDateString(brrq.getRepayDate(), DateUtil.SIMPLE_DATE_FORMAT));// 还款日期
        map.put("tradeNo", brrq.getTradeNo());// 流水号
        map.put("orderId", brrq.getOrderId());
        map.put("subOrderId", brrq.getSubOrderId());
        map.put("repayNo", brrq.getRepayNo() == null ? "" : brrq.getRepayNo().toString());
        map.put("merchantNo", fbp.getMerchantNo());
        map.put("regId", fbp.getRegId());
        map.put("amount", curRepayAmt.toString());// 金额
        map.put("remark", "(" + account + ")" + "平账原因 : " + brrq.getReason());
        map.put("flag", "23");// 线下平账分期付款
        map.put("repayType", "23");// 给账户系统的标识

        return map;
    }

    public void refund(Map<String, String> mapLog, FinanceBillPojo fbp, String subOrderId)
            throws GenerallyException, DevDefineErrorMsgException {

        if (mapLog == null || mapLog.isEmpty() || fbp == null) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        List<FinanceRefundPiece> repayList = new ArrayList<>();
        FinanceRefundPiece frp = new FinanceRefundPiece();
        frp.setAmt(fbp.getCurRepayAmt());
        frp.setRepayNo(fbp.getRepayNo());
        repayList.add(frp);
        // 组装平账参数
        FinanceBillRefundRequestPojo fbrrp = new FinanceBillRefundRequestPojo();
        fbrrp.setMerchantNo(fbp.getMerchantNo());
        fbrrp.setOpenId(fbp.getOpenId());
        fbrrp.setOrderId(fbp.getOrderId());
        fbrrp.setRegId(fbp.getRegId());
        fbrrp.setRepayDate(DateUtil.parseDate(mapLog.get("tranTime"), DateUtil.SIMPLE_DATE_FORMAT));
        fbrrp.setRepayList(repayList);
        fbrrp.setRepayModel("normal");
        fbrrp.setSumAmt(fbp.getCurRepayAmt());
        fbrrp.setTradeNo(mapLog.get("tradeNo"));
        fbrrp.setBankCardNo(mapLog.get("bankCardNo"));
        fbrrp.setBankName(mapLog.get("bankName"));
        fbrrp.setRepayType(mapLog.get("repayType"));
        fbrrp.setSubOrderId(subOrderId);
        List<FinanceBillRefundRequestPojo> requestMessage = new ArrayList<>();
        requestMessage.add(fbrrp);

        JSONObject jo = null;
        try {
            String responseStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillRefundUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(requestMessage), consumerConfig.getCommonPrivateKey()));
            log.info("chat response str: " + responseStr);

            jo = JSONObject.parseObject(responseStr);
        } catch (Exception e) {
            log.error("chat response error", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.MANAGER, Location.A);
        }

        if (jo != null && jo.getString("retCode").equals("error")) {
            throw new DevDefineErrorMsgException(String.format("账务系统返回【%s】", jo.getString("retMsg")));
        }

    }

    public HSSFWorkbook convertQBHXlsx(List<HouseBillQueryResponsePojo> list) {
        String[] header =
        {"渠道名称", "渠道经纪人", "订单号", "子订单", "手机号", "借款人", "商户", "期数", "总期数", "最后还款日", "逾期天数", "本期应还", "减免罚息", "罚息",
                "违约金",
                "还款金额", "实际还款日期", "账单状态", "借款金额", "分期金额", "当期月供", "资金流水号"};
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
            xu.append(hr, 0, list.get(i).getChannal());
            xu.append(hr, 1, list.get(i).getSongsong());
            xu.append(hr, 2, list.get(i).getOrderId());
            xu.append(hr, 3, list.get(i).getSubOrderId());
            xu.append(hr, 4, list.get(i).getRegId());
            xu.append(hr, 5, list.get(i).getRealName());
            xu.append(hr, 6, list.get(i).getMerchantNo());
            xu.append(hr, 7, list.get(i).getRepayNo());
            xu.append(hr, 8, list.get(i).getInstallTerms());
            xu.append(hr, 9, list.get(i).getLastRepayDate());

            xu.append(hr, 10, list.get(i).getOverdueDays());
            xu.append(hr, 11, list.get(i).getCurRepayAmt());
            xu.append(hr, 12, list.get(i).getCutOverdueInterest());
            xu.append(hr, 13, list.get(i).getFix());
            xu.append(hr, 14, list.get(i).getCurRealRepayamt());
            xu.append(hr, 15, list.get(i).getCurRepayDate());
            xu.append(hr, 16, list.get(i).getStatus());
            xu.append(hr, 17, list.get(i).getInstallSumAmt());
            xu.append(hr, 17, list.get(i).getInstallAmt());
            xu.append(hr, 17, list.get(i).getDqAmt());
            xu.append(hr, 17, list.get(i).getSerialNumber());
        }
        // 设置列宽
        hs.setColumnWidth(0, 5000);// 订单ID
        hs.setColumnWidth(1, 2000);// 姓名
        hs.setColumnWidth(2, 3500);// 手机号
        hs.setColumnWidth(3, 10000);// 订单名称
        hs.setColumnWidth(4, 4000);// 订单时间
        for (int j = 5; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    public HSSFWorkbook convertCSIXlsx(List<HouseBillQueryResponsePojo> list) {
        String[] header =
        {"订单号", "手机号", "借款人", "商户", "期数", "总期数", "最后还款日", "逾期天数", "本期应还", "减免罚息", "罚息", "违约金", "还款金额",
                "实际还款日期", "账单状态"};
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
            xu.append(hr, 0, list.get(i).getOrderId());
            xu.append(hr, 1, list.get(i).getRegId());
            xu.append(hr, 2, list.get(i).getRealName());
            xu.append(hr, 3, list.get(i).getMerchantNo());
            xu.append(hr, 4, list.get(i).getRepayNo());
            xu.append(hr, 5, list.get(i).getInstallTerms());
            xu.append(hr, 6, list.get(i).getLastRepayDate());

            xu.append(hr, 7, list.get(i).getOverdueDays());
            xu.append(hr, 8, list.get(i).getCurRepayAmt());
            xu.append(hr, 9, list.get(i).getCutOverdueInterest());
            xu.append(hr, 10, list.get(i).getCurRepayOverdueInterest());
            xu.append(hr, 11, list.get(i).getFix());
            xu.append(hr, 12, list.get(i).getCurRealRepayamt());
            xu.append(hr, 13, list.get(i).getCurRepayDate());
            xu.append(hr, 14, status(list.get(i).getStatus()));
        }
        /*
         * // 设置列宽 hs.setColumnWidth(0, 5000);// 订单ID hs.setColumnWidth(1, 2000);// 姓名
         * hs.setColumnWidth(2, 3500);// 手机号 hs.setColumnWidth(3, 10000);// 订单名称
         * hs.setColumnWidth(4, 4000);// 订单时间
         */
        for (int j = 0; j < header.length; j++) {
            hs.autoSizeColumn(j);
        }
        return hwb;
    }

    public String status(Integer i) {
        if (i == null) {
            return null;
        }
        switch (i) {
            case 0:
                return "已逾期";
            case 1:
                return "待还款";
            case 3:
                return "已还款";
            default:
                return null;
        }
    }

}
