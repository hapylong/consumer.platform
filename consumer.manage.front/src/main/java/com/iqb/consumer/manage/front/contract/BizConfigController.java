package com.iqb.consumer.manage.front.contract;

import java.util.LinkedHashMap;

import java.util.List;
import java.util.Map;

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
import com.github.pagehelper.PageInfo;
import com.iqb.eatep.ec.bizconfig.bean.BizConfigBean;
import com.iqb.eatep.ec.bizconfig.bean.PaginationBean;
import com.iqb.eatep.ec.bizconfig.service.BizConfigService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.front.base.FrontBaseService;

/**
 * Description: 业务模板前置入口
 * 
 * @author baiyapeng
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年2月22日    baiyapeng       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/bizConfig")
public class BizConfigController extends FrontBaseService {

    private static final Logger logger = LoggerFactory.getLogger(BizConfigController.class);

    @Autowired
    private BizConfigService bizConfigServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/paging", method = {RequestMethod.POST})
    public Object paging(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始分页...");
            PaginationBean bean = JSONUtil.toJavaObject(objs, PaginationBean.class);
            PageInfo<BizConfigBean> info = this.bizConfigServiceImpl.selectToListOfBean(bean);
            logger.info("业务模板分页结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", info);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("业务模板分页错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("业务模板分页错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Object insert(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始添加...");
            BizConfigBean bean = JSONUtil.toJavaObject(objs, BizConfigBean.class);
            this.bizConfigServiceImpl.insert(bean);
            logger.info("业务模板添加结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("业务模板添加错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("业务模板添加错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = {RequestMethod.POST})
    public Object getById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始主键查询...");
            Integer id = null;
            if (objs.containsKey("id")) {
                id = objs.getInteger("id");
            }
            BizConfigBean bean = this.bizConfigServiceImpl.selectByPrimaryKey(id);
            logger.info("业务模板主键查询结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", bean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("业务模板主键查询错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("业务模板主键查询错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public Object update(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始修改...");
            BizConfigBean bean = JSONUtil.toJavaObject(objs, BizConfigBean.class);
            this.bizConfigServiceImpl.updateByPrimaryKey(bean);
            logger.info("业务模板修改结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("业务模板修改错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("业务模板修改错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getOrg", method = {RequestMethod.POST})
    public Object getOrg(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始查询机构...");
            List<Map<String, Object>> list = this.bizConfigServiceImpl.selectOrgToListOfMap(objs);
            logger.info("业务模板查询机构结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("业务模板查询机构错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("业务模板查询机构错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getBiz", method = {RequestMethod.POST})
    public Object getBiz(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始查询业务类型...");
            List<Map<String, Object>> list = this.bizConfigServiceImpl.selectBizToListOfMap(objs);
            logger.info("业务模板查询业务类型结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("业务模板查询业务类型错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("业务模板查询业务类型错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
