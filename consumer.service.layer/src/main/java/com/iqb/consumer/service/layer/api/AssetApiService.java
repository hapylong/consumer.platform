package com.iqb.consumer.service.layer.api;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.api.PicInformationPojo;
import com.iqb.consumer.service.layer.api.dto.CarAssetDto;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;
import com.iqb.etep.common.exception.IqbException;

public interface AssetApiService {

    /**
     * 车类资产进件服务接口
     * 
     * @param assetDto
     * @return
     * @throws IqbException
     */
    Map<String, Object> intoCarAsset(CarAssetDto assetDto) throws IqbException;

    /**
     * 信用贷资产进件服务接口
     * 
     * @param creditLoanDto
     * @return
     * @throws IqbException
     */
    Map<String, Object> intoCreditLoan(CreditLoanDto creditLoanDto) throws IqbException;

    /**
     * 
     * 根据regId orderId merchantNo bizType获取最近三期待还款账单
     * 
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> getLastThreeOrderInfo(JSONObject json) throws IqbException;

    /**
     * 
     * 根据orderId获取全部账单
     * 
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> getAllOrderInfo(JSONObject json) throws IqbException;

    /**
     * 
     * Description:预付款
     * 
     * @param objs
     * @param request
     * @return
     */
    void prePayment(String key, JSONObject json) throws IqbException;

    /**
     * 
     * Description:正常还款
     * 
     * @param objs
     * @param request
     * @return
     */
    void normalPayment(String key, JSONObject json) throws IqbException;

    /**
     * 
     * Description:支付ID校验,金额校验
     * 
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> validateAmount(String traceNo, String paymentId);

    /**
     * 
     * Description:正常还款校验
     * 
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> validateRepay(Map<String, Object> billInfo, Map<String, String> signParameters);

    /**
     * 
     * Description:获取正常支付校验参数
     * 
     * @param objs
     * @param request
     * @return
     */
    String getRepayParams(Map<String, Object> billInfo, Map<String, String> signParameters);

    /**
     * 
     * Description:正常还款平账
     * 
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> repay(Map<String, Object> billInfo, Map<String, String> signParameters);

    /**
     * 
     * Description:自动切换解绑银行卡
     * 
     * @param objs
     * @param request
     * @return
     */
    Map<String, Object> autoSwitchUnBindBankCard(Map<String, String> objs);

    Map<String, Object> getInfoByRegId(String regId);

    PicInformationPojo getPIPByOid(String orderId);

    String generateOrderId(String merchantNo, String bizType) throws IqbException;
}
