package com.iqb.consumer.service.layer.pushRecord.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.bean.pushRecord.PushRecordBean;
import com.iqb.consumer.data.layer.biz.pushRecord.PushRecordBiz;
import com.iqb.consumer.service.layer.business.service.impl.OrderService;
import com.iqb.consumer.service.layer.ownerloan.impl.OwnerLoanServiceImpl;
import com.iqb.consumer.service.layer.pushRecord.IPushRecordService;

@Service
public class PushRecordServiceImpl implements IPushRecordService {

    protected static final Logger logger = LoggerFactory.getLogger(PushRecordServiceImpl.class);
    /**
     * 根据订单号获取推标记录 2018年2月27日 10:39:49 chengzhen
     */
    @Autowired
    private PushRecordBiz pushRecordBiz;
    @Autowired
    private OrderService orderServiceImpl;

    @Override
    public PageInfo<PushRecordBean> pushRecordByOrderIdList(JSONObject obj) {
        return new PageInfo<>(pushRecordBiz.pushRecordByOrderIdList(obj));
    }

    /**
     * 根据id删除推标记录 2018年2月26日 19:00:28 chengzhen
     */
    @Override
    public int delPushRecordById(JSONObject objs) {
        // 判断资金来源是否是交易所,如果是交易所,到jys_orderinfo看状态是否为4,5如果是就提示不难删除
        logger.debug("根据id删除推标记录入参", objs);
        HashMap<String, Long> delIdMap = new HashMap<>();
        delIdMap.put("id", Long.parseLong((String) objs.get("id")));
        if (objs.get("sourcesFunding").equals("3")) {
            List<Integer> statusList = pushRecordBiz.getJSYStateByOrderId(objs);
            if (statusList.size() == 0) {
                return 2;// 2表示是老数据，提示用户洗数据后再进行删除
            }
            for (Integer status : statusList) {
                if (status == 4 || status == 5) {
                    return 0;// 0表示该推送已经打包或拆分完毕不能删除
                }
            }
        }

        // 保存删除原因以及其他信息
        pushRecordBiz.insertDelPRRemark(objs);
        logger.debug("根据id删除推标前保存信息已完成", objs);
        pushRecordBiz.delPushRecordById(delIdMap);
        if (objs.get("sourcesFunding").equals("3")) {
            pushRecordBiz.delJSYPushRecordById(delIdMap);
        }
        OrderBean ob1 = orderServiceImpl.getOrderInfo(objs);
        Object object = objs.get("applyItems");
        int parseInt = Integer.parseInt(object.toString());
        if (ob1 != null) {
            ob1.setLeftInstIMonth(ob1.getLeftInstIMonth() + parseInt);
        }

        orderServiceImpl.updateOrderInfo1(ob1);
        return 1;
    }

}
