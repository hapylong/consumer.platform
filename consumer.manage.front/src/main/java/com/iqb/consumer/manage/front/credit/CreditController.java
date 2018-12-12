package com.iqb.consumer.manage.front.credit;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.data.layer.bean.order.InstAssetStockBean;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.credit.CreditService;
import com.iqb.consumer.service.layer.dict.DictService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/credit")
public class CreditController extends BasicService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(CreditController.class);
    @Resource
    private CreditService creditService;
    @Autowired
    private DictService dictServiceImpl;

    /**
     * 导出存量统计报表数据
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/exportStockStatisticsList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportStockStatisticsList(HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.debug("开始导出存量统计报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para;
                para = request.getParameter(paraName);

                data.put(paraName, para.trim());
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = creditService.exportStockStatisticsList(objs, response);
            logger.debug("导出存量统计报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 存量报表数据查询(分页)
     */
    @ResponseBody
    @RequestMapping(value = "/listStockStatisticsPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Map listStockStatisticsPage(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("存量报表数据查询,入参：{}", objs);
            PageInfo<InstAssetStockBean> result = creditService.listStockStatisticsPageNew(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 导出财务应收明细报表数据
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/exportShouldDebtDetailList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map exportShouldDebtDetailList(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            logger.debug("开始导出财务应收明细报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, para.trim());
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = creditService.exportShouldDebtDetailList(objs, response);
            logger.debug("导出财务应收明细报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 财务应付明细查询(分页)
     */
    @ResponseBody
    @RequestMapping(value = "/getShouldDebtDetail", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getShouldDebtDetail(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---财务应付明细查询...");
            Map<String, Object> result = creditService.getShouldDebtDetail(objs);
            logger.info("IQB信息---财务应付明细查询...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 还款信息查询
     */
    @ResponseBody
    @RequestMapping(value = "/repayment/{path}", method = {RequestMethod.POST, RequestMethod.GET})
    public Map repayment(@PathVariable String path, @RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("IQB信息---还款信息查询...");
            String openId = dictServiceImpl.getOpenIdByPath(path);
            objs.put("openId", openId);
            PageInfo<Map<String, Object>> list = creditService.getRepaymentList(objs);
            logger.info("IQB信息---还款信息查询...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/export/{path}", method = RequestMethod.GET)
    public Object queryBillHandlerExport(@PathVariable String path, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            request.setCharacterEncoding("utf-8");
            logger.debug("开始导出财务应收明细报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, para.trim());
            }
            String openId = dictServiceImpl.getOpenIdByPath(path);
            data.put("openId", openId);
            JSONObject jo = JSONObject.parseObject(JSONObject.toJSONString(data));
            Object list = creditService.queryBillExportXlsx(jo, response);
            logger.info("IQB信息---还款信息查询...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @Override
    public int getGroupCode() {
        return 0;
    }

    /**
     * 
     * Description:资产存量数据入库功能
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年8月1日
     */
    @ResponseBody
    @RequestMapping(value = "/unIntcpt-listStockStatisticsPage", method = {RequestMethod.POST, RequestMethod.GET})
    public Map unIntcptListStockStatisticsPage(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("资产存量数据入库功能,入参：{}", objs);
            Map<String, Object> result = creditService.listStockStatisticsPage(objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>(result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
