/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:20:36
 * @version V1.0
 */
package com.iqb.consumer.service.layer.contract;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface ContractService {

    /**
     * 生成合同
     * 
     * @param
     * @return
     */
    int shopCallBack(JSONObject objs);

    /**
     * 合同签署完成之后回调
     * 
     * @param objs
     * @return
     */
    int contractReturn(JSONObject objs);
}
