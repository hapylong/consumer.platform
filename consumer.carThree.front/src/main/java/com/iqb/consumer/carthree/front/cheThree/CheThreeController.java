package com.iqb.consumer.carthree.front.cheThree;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.carthree.front.BasicService;
import com.iqb.consumer.common.utils.httputils.SimpleHttpUtils;

/**
 * 
 * Description: 车300接口汇总
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
* Modification History:
* Date         Author      Version     Description
------------------------------------------------------------------
* 2018年1月24日     chengzhen       1.0        1.0 Version
* </pre>
 */
@Controller
@RequestMapping("/unIntcpt-cheThreeHunder")
public class CheThreeController extends BasicService {
    protected static final Logger logger = LoggerFactory.getLogger(CheThreeController.class);

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-城市接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeCity"}, method = {RequestMethod.POST})
    public JSONObject carThreeCity(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost = SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getAllCity", map);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-品牌接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeBind"}, method = {RequestMethod.POST})
    public JSONObject carThreeBind(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost = SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getCarBrandList", map);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车系接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeries"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeries(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getCarSeriesList", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车型接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeriesAll"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeriesAll(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/getCarModelList", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-定价接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeFixPrice"}, method = {RequestMethod.POST})
    public JSONObject carThreeFixPrice(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/eval/getUsedCarPriceAnalysis",
                            requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-VIN码接口
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeVINUrl"}, method = {RequestMethod.POST})
    public JSONObject carThreeVINUrl(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/identifyModelByVIN", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-VIN码接口Eval
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeVINUrlEval"}, method = {RequestMethod.POST})
    public JSONObject carThreeVINUrlEval(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost = SimpleHttpUtils.httpGet("http://testapi.che300.com/service/common/eval", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车型详细参数
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeriesDetail"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeriesDetail(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "03a6741f94109d1d590af94715cf983a");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://api.che300.com/service/getModelParameters", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-车型详细参数更新时间
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeSeriesDetailUpdateTime"}, method = {RequestMethod.POST})
    public JSONObject carThreeSeriesDetailUpdateTime(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "03a6741f94109d1d590af94715cf983a");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://api.che300.com/service/getModelParamsUpdateTime", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-新车价格和当地优惠价
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeNewCarPrice"}, method = {RequestMethod.POST})
    public JSONObject carThreeNewCarPrice(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "03a6741f94109d1d590af94715cf983a");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://api.che300.com/service/getNewCarPrice", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911
     * 车300接口FINANCE-2912车300金融服务数据接口文档汇总---车型库接口文档：www.che300.com/open-年月残值
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeMonthlyYearResidual"}, method = {RequestMethod.POST})
    public JSONObject carThreeMonthlyYearResidual(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/common/eval", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    /**
     * 
     * Description:FINANCE-2911 1 增量获取车300车型库1.1 功能说明返回车300增量车型库。
     * 
     * @param
     * @return Object
     * @throws @Author chengzhen Create Date: 2018年1月24日 11:01:30
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @ResponseBody
    @RequestMapping(value = {"/carThreeMonthlyYearResidual1"}, method = {RequestMethod.POST})
    public JSONObject carThreeMonthlyYearResidual1(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            requestMessage.put("token", "529806af00002af1d150109e22fb9043");
            String httpPost =
                    SimpleHttpUtils.httpGet("http://testapi.che300.com/service/common/eval", requestMessage);
            JSONObject jsonObj = JSON.parseObject(httpPost);
            return jsonObj;
        } catch (Exception e) {}
        return requestMessage;
    }

    @Override
    public int getGroupCode() {
        return 0;
    }
}
