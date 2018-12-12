/**
 * Description:
 * 
 * @author crw
 * @version 1.0
 * 
 * <pre>
* Modification History:
* Date         Author      Version     Description
------------------------------------------------------------------
* 2018年1月24日 21:33:51 chengzhen   1.0        1.0 Version
* </pre>
 */
package com.iqb.consumer.manage.front.carThree;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.iqb.consumer.manage.front.test.penalty.derate.util.HttpClientUtil;

/**
 * @author chengzhen
 * 
 */
public class TestCheThreeController {
    protected static final Logger logger = LoggerFactory.getLogger(TestCheThreeController.class);
    private static final String BASEURL = "http://localhost:8080/consumer.manage.front/unIntcpt-cheThreeHunder";

    @Test
    public void testCarThreeCity() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeCity", json);
        logger.info("返回结果:{}", result);
        /**
         * { "prov_id": "1", "city_list": [{ "admin_code": "659002", "city_name": "阿拉尔", "prov_id":
         * "32", "initial": "A", "prov_name": "新疆", "city_id": "117" }, { "admin_code": "410500",
         * "city_name": "安阳", "prov_id": "17", "initial": "A", "prov_name": "河南", "city_id": "131"
         * }, { "admin_code": "340800", "city_name": "安庆", "prov_id": "13", "initial": "A",
         * "prov_name": "安徽", "city_id": "209" }, { "admin_code": "610900", "city_name": "安康",
         * "prov_id": "28", "initial": "A", "prov_name": "陕西", "city_id": "248" },
         */
    }

    @Test
    public void testCarThreeBind() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeBind", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "brand_list": [ { "update_time": "2016-01-27 14:27:29", "initial": "A",
         * "brand_name": "奥迪", "brand_id": "1" }, { "update_time": "2016-01-27 13:41:17", "initial":
         * "A", "brand_name": "阿尔法·罗密欧", "brand_id": "3" },
         */
    }

    @Test
    public void testcarThreeSeries() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("brandId", 1);
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeSeries", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "series_list": [{ "series_group_name": "阿尔法·罗密欧",//车系组名 "update_time":
         * "2018-01-22 14:31:52",//更新时间 "maker_type": "进口", "series_name": "156(进口)",//车系名称
         * "series_id": "2284"//车系ID }, { "series_group_name": "阿尔法·罗密欧", "update_time":
         * "2018-01-22 14:31:52", "maker_type": "进口", "series_name": "166(进口)", "series_id": "46" },
         */
    }

    @Test
    public void carThreeSeriesAll() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("seriesId", 46);
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeSeriesAll", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { { "model_list": [ { "update_time": "2018-01-23 22:20:13",//更新时间 "min_reg_year":
         * "2002",//最小上牌年份 "model_name": "2003款 166(进口) 3.0 自动",//车型名称 "model_price": "58.8",//指导价
         * "seat_number": "5",//座位数 "liter": "3.0L",//排量 "model_year": "2003",//年款 "short_name":
         * "2003款 3.0 自动", "model_id": "546",//车型ID "max_reg_year": "2007",//最大上牌年份
         * "discharge_standard": "欧3",//排放标准 "gear_type": "自动"//变速箱 } ], "url":
         * "http://www.che300.com";, "status": "1" }
         */
    }

    @Test
    public void carThreeFixPrice() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("modelId", 546);// 车型ID
        params.put("regDate", "2012-02");// 车辆上牌日期
        params.put("makeDate", "2011-09");// 车辆出厂日期 可选参数
        params.put("mile", 5);// 车辆行驶里程 单位万里
        params.put("zone", 117);// 城市ID
        params.put("color", "黑色");// 车辆颜色（中文），颜色列表：米色，棕色，金色，紫色，巧克力色，黑色，蓝色，灰色，绿色，红色，橙色，白色，香槟色，银色，黄色
        params.put("interior", "优");// 内饰状况（中文），可选列表：优、良、中、差
        params.put("surface", "优");// 漆面状况（中文），可选列表：优、良、中、差
        params.put("work_state", "优");// 工况状况（中文），可选列表：优、良、中、差
        params.put("transfer_times", 1);// 过户次数 可选参数
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeFixPrice", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "eval_prices": { "b2b_price": 5.6,//批发价 "c2b_price": 5.46//收车价 }, "status": "1" }
         */
    }

    @Test
    public void carThreeVINUrl() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vin", "WBANA71030B600054");// 过户次数 可选参数
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeVINUrl", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "modelInfo": [{ "series_group_name": "一汽丰田", "min_reg_year": 2010,//最小上牌年份 "color":
         * "",//颜色 "model_liter": "4.0L",//排量 "model_year": 2010,//年款 "brand_name": "丰田",//品牌名称
         * "model_id": 6608,//车型ID "max_reg_year": 2014,//最大上牌年份 "brand_id": 36,//品牌ID "series_id":
         * 441,//车系ID "model_name": "2010款 普拉多 4.0L V6 TX",//车型名称 "model_price": 53.8,//车型指导价
         * "model_emission_standard": "国4",//排放标准 "model_gear": "自动",//变速箱类型 "series_name":
         * "普拉多",//车系名称 "ext_model_id": 6608//如果合作伙伴的车型在车300这边做了映射的话，该字段会返回合作伙伴的车型ID，
         * 该字段为0就表示没有映射上去；如果双方没有映射那么该字段就是车300的车型ID。 }], "status": "1" }
         */
    }

    @Test
    public void carThreeVINUrlEval() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("vin", "WBANA71030B600054");// 车辆识别码
        params.put("modelId", "6608");// 车300车型ID
        params.put("oper", "notifyVINResult");// 接口名称，此接口是notifyVINResult
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeVINUrlEval", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "modelInfo": [{ "series_group_name": "一汽丰田", "min_reg_year": 2010,//最小上牌年份 "color":
         * "",//颜色 "model_liter": "4.0L",//排量 "model_year": 2010,//年款 "brand_name": "丰田",//品牌名称
         * "model_id": 6608,//车型ID "max_reg_year": 2014,//最大上牌年份 "brand_id": 36,//品牌ID "series_id":
         * 441,//车系ID "model_name": "2010款 普拉多 4.0L V6 TX",//车型名称 "model_price": 53.8,//车型指导价
         * "model_emission_standard": "国4",//排放标准 "model_gear": "自动",//变速箱类型 "series_name":
         * "普拉多",//车系名称 "ext_model_id": 6608//如果合作伙伴的车型在车300这边做了映射的话，该字段会返回合作伙伴的车型ID，
         * 该字段为0就表示没有映射上去；如果双方没有映射那么该字段就是车300的车型ID。 }], "status": "1" }
         */
    }

    @Test
    public void carThreeSeriesDetail() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("modelId", "6608");// 车300车型ID
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeSeriesDetail", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "update_time": "2017-12-28 13:30:51", "paramters": { "操控配置": { "前桥限滑差速器/差速锁": "",
         * "中央差速器锁止功能": "", "车身稳定控制": "1", "上坡辅助": "1", "随速助力转向调节(EPS)": "", "陡坡缓降": "1", "空气悬架":
         * "", "可变转向比": "", "盲点检测": "", "ABS防抱死": "1", "刹车辅助(EBA/BAS/BA等)": "1", "后桥限滑差速器/差速锁": "",
         */
    }

    @Test
    public void carThreeSeriesDetailUpdateTime() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("modelId", "6608");// 车300车型ID
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeSeriesDetailUpdateTime", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 {"update_time":"2017-12-28 13:30:51","status":"1"}
         */
    }

    @Test
    public void carThreeNewCarPrice() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("modelId", "110");// 车300车型ID
        params.put("zone", "11");// 城市ID
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeNewCarPrice", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "status": 1,//0表示失败 1表示成功 "new_car_price": { "price": "41.38",//新车最低价 "code": 1,
         * "avg_price": "41.38",//平均优惠价 "dealer_name": "南京永达奥迪",//4S店名称 "report_date":
         * "2016年08月31日",//报价日期 "model_name": "2016款 奥迪A6L 45 TFSI quattro 运动型"//车型名称 } }
         */
    }

    @Test
    public void carThreeMonthlyYearResidual() throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oper", "getMonthResidualTrend");// 年月分别年getUsedCarResidualAnalysis月getMonthResidualTrend
        params.put("modelId", "1");// 车300车型ID
        params.put("zone", "11");// 城市ID
        params.put("regDate", "2016-07");// 城市ID //车辆上牌日期
        params.put("mile", "1");// 公里数
        // 以下是月度残值所需要的参数(都不是必须的)

        params.put("color", "黑色");// 车辆颜色（中文），颜色列表：米色，棕色，金色，紫色，巧克力色，黑色，蓝色，灰色，绿色，红色，橙色，白色，香槟色，银色，黄色
        params.put("interior", "优");// 内饰状况（中文），可选列表：优、良、中、差 params.put("surface",
                                    // "优");//漆面状况（中文），可选列表：优、良、中、差
        params.put("workState", "优");// 工况状况（中文），可选列表：优、良、中、差
        params.put("transferTimes", 1);// 过户次数 可选参数
        String json = JSON.toJSONString(params);
        logger.info("请求数据:{}", json);
        String result = HttpClientUtil.httpPost(BASEURL + "/carThreeMonthlyYearResidual", json);
        logger.info("返回结果:{}", result);
        /**
         * 出参 { "residual_rate": [{ "year": "2019",//残值年份 "rate": 0.65,//残值率数据 "price":
         * "17.791"//当前残值（车商收购价） }, { "year": "2020", "rate": 0.59, "price": "16.128" }, { "year":
         * "2021", "rate": 0.53, "price": "14.546" }, { "year": "2022", "rate": 0.47, "price":
         * "12.890" }, { "year": "2023", "rate": 0.42, "price": "11.523" }], "status": 1 }
         */

        /**
         * 月度残值出参: { "trends": [{ "trend_date": "2018-01",//残值日期 "eval_price": "18.806"//当前残值（车商收购价）
         * }, { "trend_date": "2018-04", "eval_price": "18.519" }, { "trend_date": "2018-07",
         * "eval_price": "17.785" }, { "trend_date": "2018-10", "eval_price": "17.313" }, {
         * "trend_date": "2019-01", "eval_price": "17.049" }, { "trend_date": "2019-04",
         * "eval_price": "16.788" }, { "trend_date": "2019-07", "eval_price": "16.123" }, {
         * "trend_date": "2019-10", "eval_price": "15.695" }, { "trend_date": "2020-01",
         * "eval_price": "15.455" }, { "trend_date": "2020-04", "eval_price": "15.207" }, {
         * "trend_date": "2020-07", "eval_price": "14.574" }, { "trend_date": "2020-10",
         * "eval_price": "14.168" }, { "trend_date": "2021-01", "eval_price": "13.939" }], "status":
         * "1" }
         * 
         */

    }
}
