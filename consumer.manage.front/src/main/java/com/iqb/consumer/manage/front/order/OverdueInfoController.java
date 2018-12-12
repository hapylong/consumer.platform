package com.iqb.consumer.manage.front.order;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.config.YMForeignConfig;
import com.iqb.consumer.common.exception.OverDueReturnInfo;
import com.iqb.consumer.common.utils.RedisUtils;
import com.iqb.consumer.data.layer.bean.order.ArchivesBean;
import com.iqb.consumer.data.layer.bean.order.OverdueInfoBean;
import com.iqb.consumer.service.layer.orderinfo.OverdueInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * Description:
 * 
 * @author haojinlong
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         			Author      	Version     Description 
------------------------------------------------------------------
 * 2018年3月16日下午4:28:08 	haojinlong   	1.0        	1.0 Version 
 * </pre>
 */
@Component
@RequestMapping("/overdueInfo")
public class OverdueInfoController extends BaseService {
    private static Logger logger = LoggerFactory.getLogger(OverdueInfoController.class);
    @Resource
    private OverdueInfoService overdueInfoService;
    @Resource
    private YMForeignConfig ymForeignConfig;

    /**
     * 
     * Description:批量插入违约信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = {"/batchInsertOverdueInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map batchInsertOverdueInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---批量插入违约信息开始...{}", objs);
            int result = overdueInfoService.batchInsertOverdueInfo(objs);
            logger.debug("---批量插入违约信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:客户履约保证金结算分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/query", method = {RequestMethod.POST, RequestMethod.GET})
    public Map selectInstSettleConfigResultByParams(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始查询客户履约保证金结算信息...{}", objs);
            PageInfo<OverdueInfoBean> list = overdueInfoService.selectOverdueInfoSettlementList(objs);
            logger.debug("---查询客户履约保证金结算信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据batchId修改付款时间、付款流水号
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/updateOverdueInfoBybatchId", method = {RequestMethod.POST, RequestMethod.GET})
    public Map updateOverdueInfoBybatchId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---根据batchId修改付款时间、付款流水号信息...{}", objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            OverdueInfoBean overdueInfoBean = JSONObject.toJavaObject(objs, OverdueInfoBean.class);
            if (overdueInfoBean.checkOverdueInfo()) {
                overdueInfoBean.setSettlementDate(overdueInfoBean.getRepayDate());
                overdueInfoBean.setStatus(3);
                int result = overdueInfoService.updateOverdueInfoByBatchId(overdueInfoBean);
                linkedHashMap.put("result", result);
            } else {
                linkedHashMap.put("result", "fail");
            }
            logger.debug("---根据batchId修改付款时间、付款流水号完成...");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:保证金结算查询分页查询
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月16日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/marginQuery", method = {RequestMethod.POST, RequestMethod.GET})
    public Map marginQuery(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始查询客户履约保证金结算信息...");
            PageInfo<OverdueInfoBean> list = overdueInfoService.selectOverdueInfoSettlementList(objs);
            logger.debug("---查询客户履约保证金结算信息完成...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:根据批次号查询总结算金额、总保证金、总笔数、订单信息
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月19日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/getOverdueInfoByBatchId", method = {RequestMethod.POST, RequestMethod.GET})
    public Map getOverdueInfoByBatchId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始根据批次号查询总结算金额、总保证金、总笔数、订单信息...");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            PageInfo<OverdueInfoBean> list = overdueInfoService.selectOverdueInfoSettlementList(objs);
            OverdueInfoBean overdueInfoBean = overdueInfoService.getOverdueInfoByBatchId(objs);
            linkedHashMap.put("result", list);

            if (overdueInfoBean != null) {
                linkedHashMap.put("batchId", overdueInfoBean.getBatchId());
                linkedHashMap.put("sumSettlement", overdueInfoBean.getSumSettlement());
                linkedHashMap.put("sumMarginAmt", overdueInfoBean.getSumMarginAmt());
                linkedHashMap.put("totalNum", overdueInfoBean.getTotalNum());
                linkedHashMap.put("repayDate", overdueInfoBean.getRepayDate());
                linkedHashMap.put("serialNum", overdueInfoBean.getSerialNum());
            } else {
                linkedHashMap.put("batchId", objs.getString("batchId"));
                linkedHashMap.put("sumSettlement", new BigDecimal(0));
                linkedHashMap.put("sumMarginAmt", new BigDecimal(0));
                linkedHashMap.put("totalNum", 0);
                linkedHashMap.put("repayDate", "");
                linkedHashMap.put("serialNum", "");
            }
            logger.debug("---根据批次号查询总结算金额、总保证金、总笔数、订单信息完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description:客户违约结算流程启动
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月19日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/startOverdueInfoStartWF", method = {RequestMethod.POST, RequestMethod.GET})
    public Map startOverdueInfoStartWF(@RequestBody JSONObject objs, HttpServletRequest request) {
        int resultMap = 0;
        try {
            logger.debug("---客户违约结算流程启动开始...{}", objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            Redisson redisson =
                    RedisUtils.getInstance()
                            .getRedisson(ymForeignConfig.getRedisHost(), ymForeignConfig.getRedisPort());
            // 发起违约流程只能单线程进行操作,防止同时间多用户操作同一笔订单
            RLock rLock = null;
            try {
                rLock = RedisUtils.getInstance().getRLock(redisson, "OverdueInfoStartWFRedis");
                if (rLock.tryLock(15, 15, TimeUnit.SECONDS)) { // 第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
                    resultMap = overdueInfoService.startOverdueInfoProcess(objs);
                } else {
                    return super.returnFailtrueInfo(new IqbException(OverDueReturnInfo.OVERDUE_DOUBLE));
                }
            } catch (Exception e) {
                return super.returnFailtrueInfo(e);
            } finally {
                rLock.unlock();
            }
            try {
                if (redisson != null) {
                    RedisUtils.getInstance().closeRedisson(redisson);
                }
            } catch (Exception e) {
                logger.error("---获取客户违约保证金结算提交的订单号出现异常---{}", e);
            }
            linkedHashMap.put("result", resultMap);
            logger.debug("---客户违约结算流程启动完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 保证金结算查询导出 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年3月20日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/exportMarginQuery", method = {RequestMethod.POST, RequestMethod.GET})
    public Map exportMarginQuery(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {}
        try {
            logger.debug("开始导出保证金结算查询报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, new String(para.trim()));
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = overdueInfoService.exportSelectOverdueInfoSettlementList(objs, response);
            logger.debug("导出保证金结算查询报表数据完成.结果：{}", result);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
            linkedHashMap.put("result", result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 档案资料查询 Description:
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年6月26日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/queryArchivesList", method = {RequestMethod.POST, RequestMethod.GET})
    public Map queryArchivesList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("---开始档案资料查询...{}", objs);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            PageInfo<ArchivesBean> list = overdueInfoService.queryArchivesList(objs);
            linkedHashMap.put("result", list);
            logger.debug("---档案资料查询完成.");
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB查询中阁错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
