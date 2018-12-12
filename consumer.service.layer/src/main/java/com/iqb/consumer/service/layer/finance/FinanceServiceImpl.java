package com.iqb.consumer.service.layer.finance;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.ConsumerConfig;
import com.iqb.consumer.common.constant.Constant;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.exception.GenerallyException.Layer;
import com.iqb.consumer.common.exception.GenerallyException.Location;
import com.iqb.consumer.common.exception.GenerallyException.Reason;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;
import com.iqb.consumer.common.utils.sign.SignUtil;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillQueryRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillRefundRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.HouseBillQueryResponsePojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillQueryRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillQueryResponsePojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.f.FinanceBillResult;
import com.iqb.consumer.data.layer.bean.order.house.HouseOrderEntity;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.finance.FinanceManager;
import com.iqb.consumer.data.layer.biz.paylog.PaymentLogBiz;
import com.iqb.consumer.service.layer.base.BaseService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.consumer.service.layer.page.PageMsg;
import com.iqb.etep.common.utils.DateTools;

import jodd.util.StringUtil;

@Service
public class FinanceServiceImpl extends BaseService implements FinanceService {

    private static final Logger log = LoggerFactory.getLogger(FinanceService.class);

    @Autowired
    private FinanceManager financeManager;

    @Resource
    private ConsumerConfig consumerConfig;

    @Autowired
    private DictService dictServiceImpl;

    @Resource
    private PaymentLogBiz paymentLogBiz;

    @Autowired
    private OrderBiz orderBiz;

    @Override
    public Object queryBillHandler(BillQueryRequestPojo bqr) throws GenerallyException, DevDefineErrorMsgException {
        // 获取商户集合
        JSONObject objs = new JSONObject();
        objs.put("merchNames", bqr.getMerchNames());
        JSONObject merchLimitObject = super.getMerchLimitObject(objs);
        FinanceBillQueryRequestPojo fbqr = financeManager.createFBQR(bqr, merchLimitObject);

        if (fbqr == null) {
            if (bqr.getType() % 2 == 0) {
                return new PageInfo<>();
            } else {
                return null;
            }
        }

        FinanceBillQueryResponsePojo response = null;
        try {
            String responseStr = SimpleHttpUtils.httpPost(consumerConfig.getFinanceBillQueryUrl(),
                    SignUtil.chatEncode(JSONObject.toJSONString(fbqr), consumerConfig.getCommonPrivateKey()));
            log.info("chat response str: " + responseStr);
            response = JSONObject.parseObject(responseStr, FinanceBillQueryResponsePojo.class);
        } catch (Exception e) {
            log.error("chat response error", e);
            throw new GenerallyException(Reason.CHAT_RESPONSE_ERROR, Layer.SERVICE, Location.A);
        }

        if (response == null || response.getResult() == null || response.getResult().getRecordList() == null
                || response.getResult().getRecordList().isEmpty()) {
            return new PageInfo<>();
        }

        if (response != null && response.getRetCode().equals("error")) {
            throw new DevDefineErrorMsgException(String.format("账务系统返回【%s】", response.getRetMsg()));
        }

        FinanceBillResult fbr = response.getResult();

        if (bqr.getChannal() == Constant.BILL_QUERY_HOUSE) {
            List<FinanceBillPojo> fbps = fbr.getRecordList();
            List<HouseBillQueryResponsePojo> hbqrs = new ArrayList<>();
            for (FinanceBillPojo fbp : fbps) {
                HouseBillQueryResponsePojo hbqr = new HouseBillQueryResponsePojo();
                financeManager.convert(hbqr, fbp);
                hbqrs.add(hbqr);
            }

            if (bqr.getType() % 2 == 0) {
                return new PageMsg<>(hbqrs, 8, fbr.getCurrentPage(), fbr.getTotalCount(),
                        fbr.getNumPerPage());
            } else {
                return hbqrs;
            }
        } else {
            return response;
        }

    }

    @Override
    public Object verifyPayment(BillQueryRequestPojo bqr) throws GenerallyException, DevDefineErrorMsgException {
        String hoId = bqr.getOrderId();
        Integer channal = bqr.getChannal();
        if (StringUtil.isEmpty(hoId) || channal == null) {
            throw new GenerallyException(Reason.INVALID_REQUEST_PARAMS, Layer.SERVICE, Location.A);
        }
        if (channal == Constant.BILL_QUERY_HOUSE) {
            HouseOrderEntity hoe = orderBiz.queryHOEByOid(hoId);
            String openId = dictServiceImpl.getOpenIdByBizType(hoe.getBusinessType());
            FinanceBillPojo fbp = financeManager.getFinanceBill(hoe, null, openId);
        }
        return null;
    }

    @Override
    public void billHandlerRefund(BillRefundRequestPojo brrq) throws GenerallyException, DevDefineErrorMsgException {
        // 平账传参
        HouseOrderEntity hoe = orderBiz.queryHOEByOid(brrq.getOrderId());
        if (hoe == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.A);
        }

        String openId = dictServiceImpl.getOpenIdByBizType(hoe.getBusinessType());
        FinanceBillPojo fbp = financeManager.getFinanceBill(hoe, brrq, openId);

        if (fbp == null) {
            throw new GenerallyException(Reason.DB_NOT_FOUND, Layer.SERVICE, Location.B);
        }

        Map<String, String> mapLog = financeManager.createLog(brrq, fbp);

        // 插入支付日志
        try {
            paymentLogBiz.insertPaymentLog(mapLog);
        } catch (Exception e) {
            log.error("insertPaymentLog error :", e);
        }
        // 开始平账
        financeManager.refund(mapLog, fbp, brrq.getSubOrderId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public String queryBillHandlerExportXlsx(BillQueryRequestPojo bqr, HttpServletResponse response)
            throws GenerallyException, DevDefineErrorMsgException {
        List<HouseBillQueryResponsePojo> xlsx = new ArrayList<>();
        try {
            xlsx = (List<HouseBillQueryResponsePojo>) queryBillHandler(bqr);
        } catch (Exception e) {
            log.error("queryBillHandlerExportXlsx convert list error:", e);
        }

        try {
            // 2.导出excel表格
            HSSFWorkbook workbook = financeManager.convertQBHXlsx(xlsx);
            response.setContentType("application/vnd.ms-excel");
            String fileName = "attachment;filename=orderInfo-" + DateTools.getYmdhmsTime() + ".xls";
            response.setHeader("Content-disposition", fileName);
            OutputStream ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            log.error("导出订单列表出现异常:{}", e);
            return "fail";
        }
        return "success";
    }

}
