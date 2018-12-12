package com.iqb.consumer.data.layer.dao.contract;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.contract.InstOrderContractEntity;

/**
 * 
 * Description: 此仓库用来存储对InstOrderContract表的所有操作
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月19日    adam       1.0        1.0 Version 
 * </pre>
 */
public interface InstOrderContractRepository {

    InstOrderContractEntity getIOCEByOid(String orderId);

    void saveIOCE(InstOrderContractEntity ioce);

    void updateIOCE(InstOrderContractEntity ioce);

    /**
     * 
     * Description: 校验 loanContractNo 借款合同编号
     * 
     * @param
     * @return InstOrderContractEntity
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午3:15:52
     */
    InstOrderContractEntity checkLoanContractNo(@Param("orderId") String orderId,
            @Param("loanContractNo") String loanContractNo);

    /**
     * 
     * Description: 校验 guarantyContractNo 担保合同编号
     * 
     * @param
     * @return InstOrderContractEntity
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午3:15:58
     */
    InstOrderContractEntity checkGuarantyContractNo(@Param("orderId") String orderId,
            @Param("guarantyContractNo") String guarantyContractNo);

    /**
     * 
     * Description: 校验 leaseContractNo租赁合同编号
     * 
     * @param
     * @return InstOrderContractEntity
     * @throws
     * @Author adam Create Date: 2017年5月19日 下午3:16:02
     */
    InstOrderContractEntity checkLeaseContractNo(@Param("orderId") String orderId,
            @Param("leaseContractNo") String leaseContractNo);

}
