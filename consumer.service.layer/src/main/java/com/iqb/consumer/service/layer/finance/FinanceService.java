package com.iqb.consumer.service.layer.finance;

import javax.servlet.http.HttpServletResponse;

import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillQueryRequestPojo;
import com.iqb.consumer.data.layer.bean.finance.pojo.c.BillRefundRequestPojo;

public interface FinanceService {

    Object queryBillHandler(BillQueryRequestPojo bqr) throws GenerallyException, DevDefineErrorMsgException;

    Object verifyPayment(BillQueryRequestPojo bqr) throws GenerallyException, DevDefineErrorMsgException;

    void billHandlerRefund(BillRefundRequestPojo brrq) throws GenerallyException, DevDefineErrorMsgException;

    String queryBillHandlerExportXlsx(BillQueryRequestPojo bqr, HttpServletResponse response)
            throws GenerallyException, DevDefineErrorMsgException;

}
