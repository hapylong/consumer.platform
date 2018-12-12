package com.iqb.consumer.data.layer.biz.dandelion;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.common.utils.BigDecimalUtil;
import com.iqb.consumer.data.layer.bean.credit_product.entity.InstOrderInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.entity.InstCreditInfoEntity;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetDesignedPersionInfoResponseMessage;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.GetInfoByOidResponsePojo;
import com.iqb.consumer.data.layer.bean.dandelion.pojo.RecalculateAmtPojo;
import com.iqb.consumer.data.layer.bean.plan.PlanBean;
import com.iqb.consumer.data.layer.biz.QrCodeAndPlanBiz;
import com.iqb.consumer.data.layer.dao.dandelion.DandelionRepository;
import com.iqb.etep.common.base.biz.BaseBiz;

/**
 * 
 * Description: 蒲公英数据业务中心
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月15日    adam       1.0        1.0 Version 
 * </pre>
 */
@Component
public class DandelionManager extends BaseBiz {

    @Autowired
    private DandelionRepository dandelionRepository;

    @Autowired
    private QrCodeAndPlanBiz qrCodeAndPlanBiz;

    /**
     * 
     * Description: 4.3基本信息接口：通过订单号，获取信息
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月15日 下午6:19:44
     */
    public GetInfoByOidResponsePojo getInfoByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return dandelionRepository.getInfoByOid(orderId);
    }

    /**
     * 
     * Description: 4.6保存指派人信息 ： 保存id, designCode, designName 到 inst_creditinfo
     * 
     * @param
     * @return void
     * @throws
     * @Author adam Create Date: 2017年5月15日 下午6:19:44
     */
    public void persistDesignPersion3elements(InstCreditInfoEntity icie) {
        super.setDb(0, super.MASTER);
        dandelionRepository.persistSubletInfo(icie);
    }

    public void updateDesignPersion3elements(InstCreditInfoEntity icie) {
        super.setDb(0, super.MASTER);
        dandelionRepository.updateSubletInfo(icie);
    }

    public InstCreditInfoEntity getInstCreditInfoEntityByOid(String orderId) {
        super.setDb(0, super.SLAVE);
        return dandelionRepository.getInstCreditInfoEntityByOid(orderId);
    }

    public Integer updateToAddGuaranteeNo(InstCreditInfoEntity icie) {
        super.setDb(0, super.MASTER);
        return dandelionRepository.updateToAddGuaranteeNo(icie);
    }

    public int updateCreditTypeByT(InstCreditInfoEntity icie) {
        Integer type = icie.getType();
        super.setDb(0, super.MASTER);
        switch (type) {
            case InstCreditInfoEntity.TYPE_SAVE_BORROW_INFO:
                return dandelionRepository.updateBorrowInfo(icie);
            case InstCreditInfoEntity.TYPE_SAVE_PERSION_INFO:
                return dandelionRepository.updatePersionInfo(icie);
            case InstCreditInfoEntity.TYPE_SAVE_MORTGAGE_INFO:
                icie.setBorrTogetherName(icie.getBorrowTogether() == 0 ? "" : icie.getBorrTogetherName());
                return dandelionRepository.updateMortgateInfo(icie);
            default:
                return InstCreditInfoEntity.TYPE_UNDEFINE;
        }
    }

    public RecalculateAmtPojo getRecalculateAmtPojo(BigDecimal orderAmt, Long planId, PlanBean pb,
            GetDesignedPersionInfoResponseMessage gdpi)
            throws GenerallyException {
        if (orderAmt == null || planId == null) {
            return null;
        }
        if (pb == null) {
            pb = new PlanBean();
        }
        pb.copy(qrCodeAndPlanBiz.getPlanByID(planId));
        if (gdpi != null) {
            gdpi.setPlanShortName(pb.getPlanShortName());
            gdpi.setOrderAmt(orderAmt);
        }
        return getRecalculateAmtPojo(orderAmt, pb);
    }

    public int updateDandelionEntityByOid(InstOrderInfoEntity ioie) {
        super.setDb(0, super.MASTER);
        return dandelionRepository.updateDandelionEntityByOid(ioie);
    }

    /**
     * 
     * Description: RecalculateAmtPojo 计算规则 History:2017-08-24 蒲公英行流程 保证金 上标金额计算规则调整 保证金 = 借款金额
     * *(保证金比例+上浮保证金比例) 上标金额 = 借款金额*(1+服务费比例+上浮保证金比例)
     * 
     * @param
     * @return RecalculateAmtPojo
     * @throws
     * @Author adam Create Date: 2017年5月16日 下午3:42:41
     */
    private RecalculateAmtPojo getRecalculateAmtPojo(BigDecimal orderAmt, PlanBean pb) {
        RecalculateAmtPojo rap = new RecalculateAmtPojo();

        BigDecimal sbAmt =
                orderAmt.multiply(new BigDecimal(1 + pb.getServiceFeeRatio() / 100).add(new BigDecimal(pb
                        .getFloatMarginRatio() / 100))); // 上标金额=客户申请金额*（1+服务费率+上浮保证金比例（28.2%））

        BigDecimal bzAmt =
                orderAmt.multiply(new BigDecimal(pb.getMarginRatio() / 100).add(new BigDecimal(
                        pb.getFloatMarginRatio() / 100))); // 保证金=客户申请金额*（保证金率+上浮保证金比例）10%

        BigDecimal bbzAmt =
                orderAmt.multiply(new BigDecimal(pb.getMarginRatio() / 100)); // 保证金=客户申请金额*（保证金率）10%

        BigDecimal fkAmt = orderAmt.subtract(bbzAmt); // 放款金额=客户申请金额-保证金(不加上浮保证金比例)

        BigDecimal fwAmt = orderAmt.multiply(new BigDecimal(pb.getServiceFeeRatio() / 100));// 服务费=客户申请金额*（服务费率）28.2%

        BigDecimal ygAmt =
                sbAmt.divide(new BigDecimal(pb.getInstallPeriods()), 5, BigDecimal.ROUND_HALF_UP).add(
                        sbAmt.multiply(new BigDecimal(pb.getFeeRatio()).divide(new BigDecimal(100), 5,
                                BigDecimal.ROUND_HALF_UP)));
        // 月供=上标金额/期数+上标金额*1.81%

        BigDecimal sfkAmt =
                orderAmt.multiply(new BigDecimal(pb.getDownPaymentRatio()).divide(new BigDecimal(100), 5,
                        BigDecimal.ROUND_HALF_UP));

        BigDecimal sfAmt =
                orderAmt.multiply(new BigDecimal(pb.getDownPaymentRatio()).divide(new BigDecimal(100), 5,
                        BigDecimal.ROUND_HALF_UP)); // 首付款

        BigDecimal syAmt = orderAmt.subtract(sfAmt); // // 剩余金额 = (总金额-首付)

        BigDecimal ylAmt =
                syAmt.multiply(new BigDecimal(pb.getFeeRatio())
                        .divide(new BigDecimal(100), 5, BigDecimal.ROUND_HALF_UP));// 月利息
                                                                                   // =上标金额*1.81%

        BigDecimal ssxAmt = ylAmt.multiply(new BigDecimal(pb.getFeeYear())); // 上收息

        BigDecimal sfkTotalAmt = sfAmt.add(ssxAmt).add(fwAmt).add(bzAmt); // 首付款合计 = 首付款 + 上收息 + 服务费
                                                                          // + 保证金。

        rap.setBzAmt(BigDecimalUtil.format(bzAmt));
        rap.setFkAmt(BigDecimalUtil.format(fkAmt));
        rap.setFwAmt(BigDecimalUtil.format(fwAmt));
        rap.setQs(pb.getInstallPeriods());
        rap.setSbAmt(BigDecimalUtil.format(sbAmt));
        rap.setSfkAmt(BigDecimalUtil.format(sfkAmt));
        rap.setSfkTotalAmt(BigDecimalUtil.format(sfkTotalAmt));
        rap.setSsxAmt(BigDecimalUtil.format(ssxAmt));
        rap.setYgAmt(BigDecimalUtil.format(ygAmt));

        return rap;
    }

    public int saveUpdateMortgageInfo(String string) {

        return dandelionRepository.saveUpdateMortgageInfo(string);
    }
}
