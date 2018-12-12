package com.iqb.consumer.manage.front.pushRecord;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.utils.StringUtil;
import com.iqb.consumer.data.layer.bean.pushRecord.PushRecordBean;
import com.iqb.consumer.service.layer.pushRecord.IPushRecordService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 推标删除
 * 
 * @author chengzhen
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2018年2月26日 18:52:15    chengzhen       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/pushRecord")
public class PushRecordController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(PushRecordController.class);

    @Resource
    private IPushRecordService pushRecordServiceImpl;

    /**
     * 
     * Description: 根据订单号获取推标记录列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author chengzhen Create Date: 2018年2月26日 18:54:03
     */
    @ResponseBody
    @RequestMapping(value = "/pushRecordByOrderIdList", method = RequestMethod.POST)
    public Object pushRecordByOrderIdList(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始根据订单号获取推标记录列表列表..." + objs.toJSONString());
            if (objs.entrySet().size() == 2) {
                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
                linkedHashMap.put("result", null);
                return super.returnSuccessInfo(linkedHashMap);
            }
            PageInfo<PushRecordBean> pushRecordByOrderIdList = pushRecordServiceImpl.pushRecordByOrderIdList(objs);
            logger.info("IQB信息---根据订单号获取推标记录列表列表结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", pushRecordByOrderIdList);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 根据订单号获取推标记录列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author chengzhen Create Date: 2018年2月27日 14:37:41
     */
    @ResponseBody
    @RequestMapping(value = "/delPushRecordById", method = RequestMethod.POST)
    public Object delPushRecordById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始根据id删除推标记录..." + objs.toJSONString());
            int res = pushRecordServiceImpl.delPushRecordById(objs);
            logger.info("IQB信息---根据id删除推标记录结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (res == 0) {
                linkedHashMap.put("result", "该推送已经打包或拆分完毕不能删除");
            } else if (res == 2) {
                linkedHashMap.put("result", "该数据是老数据,洗数据后再进行删除");
            } else if (res == 4) {
                linkedHashMap.put("result", "该数据已经赎回,不能删除");
            } else {
                linkedHashMap.put("result", "删除成功");
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

}
