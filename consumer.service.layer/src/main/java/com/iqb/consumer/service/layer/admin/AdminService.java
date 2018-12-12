package com.iqb.consumer.service.layer.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.DevDefineErrorMsgException;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.admin.entity.IqbCustomerPermissionEntity;
import com.iqb.consumer.data.layer.bean.admin.pojo.MerchantTreePojo;
import com.iqb.consumer.data.layer.bean.admin.request.AuthoritySeekResponseMessage;
import com.iqb.consumer.data.layer.bean.contract.InstOrderContractEntity;

public interface AdminService {

    /**
     * 
     * Description: 通过订单号获取 IOCE,InstOrderContractEntity 驼峰缩写
     * 
     * @param
     * @return InstOrderContractEntity
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午1:51:55
     */
    InstOrderContractEntity getIOCEByOid(String orderId);

    String saveOrUpdateIOCE(InstOrderContractEntity ioce) throws GenerallyException;

    String generateKey(JSONObject requestMessage) throws GenerallyException;

    Object getKeyPairsByCondition(JSONObject requestMessage) throws GenerallyException;

    /**
     * 根据订单号查询审批相关信息
     * 
     * @param orderId
     * @return
     */
    Map<String, String> getApproveHtml(JSONObject objs);

    String updateKey(JSONObject requestMessage) throws GenerallyException;

    String changePlan(JSONObject requestMessage) throws GenerallyException;

    void getMerchantNos(JSONObject requestMessage);

    void merchantNos(JSONObject requestMessage);

    /**
     * 
     * Description: 更新白名单
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年6月13日 下午3:55:39
     */
    void updateIpsById(JSONObject requestMessage) throws GenerallyException;

    boolean isAccountExit(String openId, String regId) throws GenerallyException;

    void applyNewAccount(String regId, String openId, String bankCard, String realName, String idCard)
            throws DevDefineErrorMsgException, GenerallyException;

    void casOrgCode(JSONObject requestMessage) throws DevDefineErrorMsgException, GenerallyException;

    void authorityHandlerChange(JSONObject requestMessage) throws DevDefineErrorMsgException, GenerallyException;

    AuthoritySeekResponseMessage authorityHandlerSeek(JSONObject requestMessage)
            throws DevDefineErrorMsgException, GenerallyException;

    IqbCustomerPermissionEntity authorityHandlerQuery(JSONObject requestMessage)
            throws DevDefineErrorMsgException, GenerallyException;

    void getMerchantListByOrgCode(JSONObject requestMessage) throws DevDefineErrorMsgException;

    List<MerchantTreePojo> authorityHandlerTree1();

    Object analysisData(JSONObject requestMessage, Boolean isInside, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException;

    void analysisXlsx(JSONObject requestMessage, Boolean isInside, HttpServletResponse response)
            throws GenerallyException, DevDefineErrorMsgException;

    void riskManagementXlsx(JSONObject requestMessage, String type, HttpServletResponse response)
            throws GenerallyException, DevDefineErrorMsgException;

    LinkedHashMap<String, Object> analysisDataNew(JSONObject requestMessage, String type, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException;

    Object analysisDataNewTotal(JSONObject requestMessage, String type, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException;

    Object analysisDataNewElx(JSONObject requestMessage, String type, Boolean isPage)
            throws DevDefineErrorMsgException, GenerallyException;

}
