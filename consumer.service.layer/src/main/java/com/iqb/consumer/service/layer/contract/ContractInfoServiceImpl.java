package com.iqb.consumer.service.layer.contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.utils.DateUtil;
import com.iqb.consumer.data.layer.bean.contract.ContractInfoBean;
import com.iqb.consumer.data.layer.bean.merchant.MerchantBean;
import com.iqb.consumer.data.layer.bean.order.OrderBean;
import com.iqb.consumer.data.layer.biz.OrderBiz;
import com.iqb.consumer.data.layer.biz.contract.ContractInfoBiz;
import com.iqb.consumer.data.layer.biz.merchant.MerchantBeanBiz;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.common.utils.StringUtil;

/**
 * @author guojuan
 */
@Service("contractInfoService")
public class ContractInfoServiceImpl implements ContractInfoService {

    protected static Logger logger = LoggerFactory.getLogger(ContractInfoServiceImpl.class);

    @Resource
    private ContractInfoBiz contractInfoBiz;
    @Resource
    private OrderBiz orderBiz;
    @Resource
    private MerchantBeanBiz merchantBiz;

    @Override
    public int insertContractInfo(JSONObject objs) {
        logger.debug("IQB信息---【服务层】插入合同补录信息，开始...");
        int result = contractInfoBiz.insertContractInfo(objs);
        logger.debug("IQB信息---【服务层】插入合同补录信息，结束...");
        return result;
    }

    @Override
    public JSONObject justMerchantType(JSONObject objs) {
        JSONObject jsonObject = new JSONObject();
        OrderBean orderBean = orderBiz.selByOrderId(objs.getString("orderId"));
        jsonObject = JSONUtil.filteNullValue(orderBean);
        MerchantBean mb = merchantBiz.getMerByMerNo(orderBean.getMerchantNo());
        jsonObject.put("parentId", mb.getParentId());

        // 判断该订单是都存在已经生成的合同信息
        ContractInfoBean bean = contractInfoBiz.selContractInfo(objs.getString("orderId"));
        jsonObject.put("contractInfo", bean);
        return jsonObject;
    }

    @Override
    public ContractInfoBean selContractInfo(String orderId) {
        return contractInfoBiz.selContractInfo(orderId);
    }

    /**
     * 生成电子合同还款明细信息 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2017年12月19日
     */
    public Map<String, String> createRepaymentDetail(String orderId) {
        Map<String, String> returnMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        OrderBean orderBean = orderBiz.selByOrderId(orderId);
        if (orderBean != null) {
            String orderItems = orderBean.getOrderItems();
            if (!StringUtil.isNull(orderItems)) {
                List<List<String>> list = new ArrayList<>();
                List<String> templist = null;
                if (orderBean.getTakePayment() > 0) {
                    for (int i = 0; i < Integer.parseInt(orderItems); i++) {
                        templist = new ArrayList<>();
                        int item = i + 1;
                        templist.add("第" + item + "期");
                        templist.add(sdf.format(DateUtil.fromDate(addMonths(new Date(), i)).getTime()));
                        templist.add(orderBean.getMonthInterest().toString());
                        list.add(templist);
                    }
                } else {
                    for (int i = 1; i <= Integer.parseInt(orderItems); i++) {
                        templist = new ArrayList<>();
                        templist.add("第" + i + "期");
                        templist.add(sdf.format(DateUtil.fromDate(addMonths(new Date(), i)).getTime()));
                        templist.add(orderBean.getMonthInterest().toString());
                        list.add(templist);
                    }
                }

                returnMap.put("WORD_TABLE_STAGES", JSONObject.toJSONString(list));
            }
        } else {
            List<List<String>> list = new ArrayList<>();
            returnMap.put("WORD_TABLE_STAGES", JSONObject.toJSONString(list));
        }

        return returnMap;
    }

    public static Date addMonths(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, days);
        return calendar.getTime();
    }
}
