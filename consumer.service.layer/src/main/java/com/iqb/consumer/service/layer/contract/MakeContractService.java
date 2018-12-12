/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月13日 上午10:20:36
 * @version V1.0
 */
package com.iqb.consumer.service.layer.contract;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
public interface MakeContractService {

    /**
     * 生成合同
     * 
     * @param
     * @return
     */
    Map<String, Object> makeContract(JSONObject objs);

    /**
     * 提交合同
     * 
     * @param
     * @return
     */
    int submitContract(JSONObject objs);

    /**
     * 初始化需要生成合同的订单列表
     * 
     * @param objs
     * @return
     */
    public Object orderContractInit(JSONObject objs);

    public Object selContractList(JSONObject objs);

    /**
     * 获取所有正在签约或已完成签约的订单
     * 
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param objs
     * @return 设定文件 Object 返回类型
     * @throws
     * @author guojuan 2017年10月11日下午5:37:49
     */
    Object orderContractFinish(JSONObject objs);
}
