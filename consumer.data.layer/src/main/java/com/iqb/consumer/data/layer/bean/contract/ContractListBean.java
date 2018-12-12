/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月20日 下午3:39:35
 * @version V1.0
 */
package com.iqb.consumer.data.layer.bean.contract;

import com.iqb.consumer.data.layer.bean.BaseEntity;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@SuppressWarnings("serial")
public class ContractListBean extends BaseEntity {

    private String orderId;// 订单号
    private int type;// 合同类型(1,未签约合同，2门店签约合同列表，3，多方签约完毕后的合同列表)
    private String contractUrl;// 合同URl
    private String contractName;// 合同名称
    private String ecId;// 合同ID
    private Integer status;// 状态

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContractUrl() {
        return contractUrl;
    }

    public void setContractUrl(String contractUrl) {
        this.contractUrl = contractUrl;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getEcId() {
        return ecId;
    }

    public void setEcId(String ecId) {
        this.ecId = ecId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
