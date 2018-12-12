package com.iqb.consumer.service.layer.dandelion;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetDesignedPersionInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetInfoByOidResponsePojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RecalculateAmtPojo;
import com.iqb.consumer.service.layer.api.dto.CreditLoanDto;

public interface DandelionCenterService {

    /**
     * 
     * Description: 4.3基本信息接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月15日 下午3:03:31
     */
    GetInfoByOidResponsePojo getInfoByOid(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.4风控信息(紧急联系人)接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月15日 下午3:35:19
     */
    CreditLoanDto getCheckInfoByOid(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.6保存指派人信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 上午10:00:52
     */
    void persistDesignPersion(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.7查询指派人担保人个数,信贷类型，共借人信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 上午10:39:28
     */
    GetDesignedPersionInfoResponseMessage getInstCreditInfoEntityByOid(JSONObject requestMessage)
            throws GenerallyException;

    /**
     * 
     * Description: 4.8担保人数累加接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午2:37:11
     */
    Integer addGuaranteeNoByOid(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.9保存信用贷类型，共借人等
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午2:21:31
     */
    void updateCreditType(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.11重算接口 .通过planBean重算金额
     * 
     * @param
     * @return RecalculateAmtPojo
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午3:35:54
     */
    RecalculateAmtPojo recalculateAmt(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.13回调业务处理
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午5:14:43
     */
    void resolveRiskNotice(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description: 4.12保存接口信息
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月17日 下午2:13:18
     */
    void persistOrderDetails(JSONObject requestMessage) throws GenerallyException;

    void chatWithRAR(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description:根据蒲公英订单号获取返回标识
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月17日
     */
    int getInstOrderBackFlag(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description:保存风控信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    long saveRiskInfo(JSONObject requestMessage) throws GenerallyException;

    /**
     * 
     * Description:更新订单表手机号码以及用户表regid realName idNo
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年7月18日
     */
    long updateOrderInfo(JSONObject requestMessage) throws GenerallyException;
    
    /**
     * 
     * Description:更新订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月28日
     */
    int updateDandelionEntityByOid(InstOrderInfoEntity instOrderBean);
    
    void saveUpdateMortgageInfo(JSONObject requestMessage) throws GenerallyException;

}
