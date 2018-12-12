/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @date 2016年9月13日 上午10:20:36
 * @version V1.0
 */
package com.iqb.consumer.service.layer.authoritycard;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface AuthorityCardService {

    /**
     * 插入权证信息
     * 
     * @param
     * @return
     */
    int insertAuthorityCard(JSONObject objs);

    /**
     * 查询单条权证信息
     * 
     * @param
     * @return
     */
    AuthorityCardBean selectOneByOrderId(JSONObject objs);

    /**
     * 更新权证信息
     * 
     * @param
     * @return
     */
    int updateAuthorityCard(JSONObject objs);

    /**
     * 
     * 更新车辆权证信息表状态
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    int updateAuthorityCardStatus(JSONObject objs);

    /**
     * 
     * 更新车辆权证信息表颜色、里程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    int updateAuthorityCardInfo(JSONObject objs);
}
