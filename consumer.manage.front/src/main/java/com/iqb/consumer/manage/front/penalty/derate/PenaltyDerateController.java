package com.iqb.consumer.manage.front.penalty.derate;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.service.layer.penalty.derate.IPenaltyDerateService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 罚息减免控制层
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月9日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/unIntcpt-penaltyDerate")
public class PenaltyDerateController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(PenaltyDerateController.class);

    @Autowired
    private IPenaltyDerateService penaltyDerateServiceImpl;

    /**
     * 
     * Description: 获取罚息减免申请列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午2:47:52
     */
    @ResponseBody
    @RequestMapping(value = "/listPenaltyDerateApply", method = RequestMethod.POST)
    public Object listPenaltyDerateApply(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始获取罚息减免申请列表..." + objs.toJSONString());
            Object retObjs = this.penaltyDerateServiceImpl.listPenaltyDerateApply(objs);
            logger.info("IQB信息---获取罚息减免申请列表结束.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", retObjs);
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
     * Description: 获取可进行罚息减免列表
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月9日 下午3:57:06
     */
    @ResponseBody
    @RequestMapping(value = "/listPenaltyDeratable", method = RequestMethod.POST)
    public Object listPenaltyDeratable(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始获取可进行罚息减免列表..." + objs.toJSONString());
            Object retObjs = this.penaltyDerateServiceImpl.listPenaltyDeratable(objs);
            logger.info("IQB信息---获取可进行罚息减免列表结束.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", retObjs);
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
     * Description: 根据id获取罚息减免信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月15日 下午2:28:45
     */
    @ResponseBody
    @RequestMapping(value = "/getPenaltyDerateById", method = RequestMethod.POST)
    public Object getPenaltyDerateById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始根据id获取罚息减免信息..." + objs.toJSONString());
            Object retObjs = this.penaltyDerateServiceImpl.getPenaltyDerateById(objs);
            logger.info("IQB信息---根据id获取罚息减免信息结束.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", retObjs);
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
     * Description: 根据uuid获取罚息减免信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 下午3:01:55
     */
    @ResponseBody
    @RequestMapping(value = "/getPenaltyDerateByUUId", method = RequestMethod.POST)
    public Object getPenaltyDerateByUUId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始根据uuid获取罚息减免信息..." + objs.toJSONString());
            Object retObjs = this.penaltyDerateServiceImpl.getPenaltyDerateByUUId(objs);
            logger.info("IQB信息---根据uuid获取罚息减免信息结束.");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", retObjs);
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
     * Description: 根据installDetailId获取罚息减免信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月16日 下午8:58:38
     */
    @ResponseBody
    @RequestMapping(value = "/getPenaltyDerateByInstallDetailId", method = RequestMethod.POST)
    public Object getPenaltyDerateByInstallDetailId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始根据installDetailId获取罚息减免信息..." + objs.toJSONString());
            Object retObjs = this.penaltyDerateServiceImpl.getPenaltyDerateByInstallDetailId(objs);
            logger.info("IQB信息---根据installDetailId获取罚息减免信息结束.");
            String key = "SeqNo" + objs.getString("orderId");
            String seqNo = penaltyDerateServiceImpl.getSeqFromRedis(key, false);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", retObjs);
            linkedHashMap.put("seqNo", seqNo);
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
