/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月18日 下午2:12:52
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.xfpay.PayInfoBean;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface PayInfoDao {

    List<PayInfoBean> getByOrgId(int orgId);

    long insertPayInfo(PayInfoBean payInfoBean);

    PayInfoBean getByCardNo(@Param("cardNo") String cardNo, @Param("orgId") String orgId);

    PayInfoBean getByIdAndOrgId(Map<String, Object> params);

    /**
     * 修改绑卡支付信息
     * 
     * @param id
     */
    void unBindCardStatus(Map<String, Object> params);

    int updateBindCardInfo(PayInfoBean payInfoBean);

    /**
     * 删除卡信息
     * 
     * @param bankId
     */
    void delBindCardInfo(@Param("id") int bankId);
}
