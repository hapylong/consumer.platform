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
import com.iqb.eatep.ec.contract.template.bean.ContractTemplateSignerBean;
import com.iqb.eatep.ec.contract.template.service.ContractTemplateSingerService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.front.base.FrontBaseService;

/**
 * Description: 合同模板-签署方前置入口
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
@RequestMapping("/contractTemplateSigner")
public class ContractTemplateSignerController extends FrontBaseService {

    private static final Logger logger = LoggerFactory.getLogger(ContractTemplateSignerController.class);

    @Autowired
    private ContractTemplateSingerService contractTemplateSingerServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/paging", method = {RequestMethod.POST})
    public Object paging(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板-签署方开始分页...");
            ContractTemplateSignerBean bean = JSONUtil.toJavaObject(objs, ContractTemplateSignerBean.class);
            PageInfo<ContractTemplateSignerBean> info = this.contractTemplateSingerServiceImpl.selectToListOfBean(bean);
            logger.info("合同模板-签署方分页结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", info);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板-签署方分页错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板-签署方分页错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Object insert(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板-签署方开始添加...");
            ContractTemplateSignerBean bean = JSONUtil.toJavaObject(objs, ContractTemplateSignerBean.class);
            this.contractTemplateSingerServiceImpl.insert(bean);
            logger.info("合同模板-签署方添加结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板-签署方添加错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板-签署方添加错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = {RequestMethod.POST})
    public Object getById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板-签署方开始根据主键查询...");
            Integer id = null;
            if (objs.containsKey("id")) {
                id = objs.getInteger("id");
            }
            ContractTemplateSignerBean bean = this.contractTemplateSingerServiceImpl.selectByPrimaryKey(id);
            logger.info("合同模板-签署方开始根据主键查询结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", bean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板-签署方开始根据主键查询错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板-签署方开始根据主键查询错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public Object update(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板-签署方开始修改...");
            ContractTemplateSignerBean bean = JSONUtil.toJavaObject(objs, ContractTemplateSignerBean.class);
            this.contractTemplateSingerServiceImpl.updateByPrimaryKey(bean);
            logger.info("合同模板-签署方开始修改.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板-签署方开始修改错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板-签署方开始修改错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/remove", method = {RequestMethod.POST})
    public Object remove(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板-签署方开始删除...");
            Integer id = null;
            if (objs.containsKey("id")) {
                id = objs.getInteger("id");
            }
            this.contractTemplateSingerServiceImpl.deleteByPrimaryKey(id);
            logger.info("合同模板-签署方开始删除.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板-签署方开始删除错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板-签署方开始删除错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getSigner", method = {RequestMethod.POST})
    public Object getSigner(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("业务模板开始查询机构...");
            List<Map<String, Object>> list = this.contractTemplateSingerServiceImpl.selectSignerToListOfMap(objs);
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
}
