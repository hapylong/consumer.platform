package com.iqb.consumer.manage.front.pledge;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.pledge.PledgeRepayService;

/**
 * Description:
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年4月10日    adam       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/unIntcpt-pledge_repay")
public class PledgeRepayController {
    @Autowired
    private PledgeRepayService pledgeRepayServiceImpl;

    /**
     * 
     * Description:
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年4月10日 下午12:11:46
     */
    private final int INVALID_REQUEST = -1;
    private final int UNKNOWN_REPAY_TYPE = -2;
    private final int DB_NOT_FOUND = -3;
    private final int DB_INVALID_ENTITY = -4;
    private final int ENCODE_DATA_EXCEPTION = -5;
    private final int RESPONSE_ERROR_MSG = -6;
    private final int SUCCESS = 0;

    @ResponseBody
    @RequestMapping(value = "/byType", method = RequestMethod.POST)
    public Object refundAll(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            Object result = pledgeRepayServiceImpl.repayByType(requestMessage);
            if (result instanceof Integer) {
                Map<String, Object> response = new HashMap<String, Object>();
                if (Integer.parseInt(result.toString()) == SUCCESS) {
                    response.put("retCode", "success");
                    response.put("retMsg", "success.");
                } else {
                    response.put("retCode", "error");
                    switch (Integer.parseInt(result.toString())) {
                        case INVALID_REQUEST:
                            response.put("msg", "invalid request.");
                            break;
                        case UNKNOWN_REPAY_TYPE:
                            response.put("msg", "unknown repay type.");
                            break;
                        case DB_NOT_FOUND:
                            response.put("msg", "db not found.");
                            break;
                        case DB_INVALID_ENTITY:
                            response.put("msg", "db invalid entity.");
                            break;
                        case ENCODE_DATA_EXCEPTION:
                            response.put("msg", "encode data exception.");
                            break;
                        case RESPONSE_ERROR_MSG:
                            response.put("msg", "response error msg.");
                            break;
                        default:
                            response.put("msg", "error known error.");
                    }
                }
                return response;
            } else {
                return result;
            }

        } catch (Throwable e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<String, Object>();
            response.put("retCode", "error");
            response.put("retMsg", "error known error.");
            return response;
        }
    }

    public static void main(String[] args) {
        String s =
                "[{\"orderId\":\"订单号\",\"orderDate\":\"1\",\"flag\":\"1,正常 2 提前全部结清\",\"amt\":\"还款金额\",\"time\":\"\"}]";
        JSONArray ja = JSONArray.parseArray(s);
        System.out.println("size :" + ja.size());
        for (int i = 0; i < ja.size(); i++) {
            JSONObject obj = JSONObject.parseObject(ja.get(i).toString());
            System.out.println(obj.get("orderId"));
        }
    }

}
