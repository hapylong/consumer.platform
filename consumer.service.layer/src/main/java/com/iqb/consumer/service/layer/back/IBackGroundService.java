package com.iqb.consumer.service.layer.back;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.order.OrderBean;

public interface IBackGroundService {

    public Map<String, Object> singleInstall(JSONObject objs);

    public Map<String, Object> goToRefund(JSONObject objs);

    /**
     * 
     * Description: 交易所分期接口
     * 
     * @param requestMessage
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年6月14日 上午11:06:32
     */
    public Object jsyOrderBreak(JSONObject requestMessage);

    /**
     * 
     * Description:保存交易所订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年9月20日
     */
    public long saveJysOrder(JSONObject objs);

    /**
     * 判断是否开户，如果没有则开户
     * 
     * @param orderBean
     * @return
     */
    public Object openAccount(OrderBean orderBean);

}
