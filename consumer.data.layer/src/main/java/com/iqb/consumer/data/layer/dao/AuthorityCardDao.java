/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @date 2016年9月20日 下午3:47:11
 * @version V1.0
 */
package com.iqb.consumer.data.layer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.iqb.consumer.data.layer.bean.api.CfImagePojo;
import com.iqb.consumer.data.layer.bean.api.PicInformationPojo;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface AuthorityCardDao {

    /**
     * 插入权证信息
     * 
     * @param
     * @return
     */
    int insertAuthorityCard(AuthorityCardBean authorityCardBean);

    /**
     * 查询单条权证信息
     * 
     * @param
     * @return
     */
    AuthorityCardBean selectOneByOrderId(@Param("orderId") String orderId);

    /**
     * 更新权证信息
     * 
     * @param
     * @return
     */
    int updateAuthorityCard(AuthorityCardBean authorityCardBean);

    PicInformationPojo getPIPByOid(String orderId);

    List<CfImagePojo> getCFPByOid(String orderId);

    /**
     * 
     * 更新车辆权证信息表状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    int updateAuthorityCardStatus(AuthorityCardBean authorityCardBean);

    /**
     * 
     * 更新车辆权证信息表颜色、里程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    int updateAuthorityCardInfo(AuthorityCardBean authorityCardBean);
}
