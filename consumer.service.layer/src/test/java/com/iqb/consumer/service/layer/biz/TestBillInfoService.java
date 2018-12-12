/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月19日 下午3:21:52
 * @version V1.0
 */
package com.iqb.consumer.service.layer.biz;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.service.layer.bill.BillInfoService;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public class TestBillInfoService extends AbstractService {

    protected static final Logger logger = LoggerFactory.getLogger(TestBillInfoService.class);

    @Resource
    private BillInfoService billInfoService;

    @Test
    public void queryBill() {

    }

    @SuppressWarnings("rawtypes")
    protected Float getAmtSum(List<Map> allList) {
        Float amtSum = 0.00f;
        int i = allList.size() - 1;
        amtSum =
                string2Float((String) allList.get(i).get("currcStmtAmt"))
                        + string2Float((String) allList.get(i).get("currLatefeeIn"))
                        - string2Float((String) allList.get(i).get("currRepayAmtSumIn"));
        return amtSum * 100;
    }

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal("263").divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        String realAmount = b.toString();
        System.out.println(realAmount);
    }

    protected Float string2Float(String indata) {
        Float v;
        if (Float.parseFloat(indata) == 0) {
            v = 0.00f;
        }
        else {
            v = Float.parseFloat(indata) / 100.00f;
        }
        return v;
    }

}
