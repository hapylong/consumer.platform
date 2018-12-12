package com.iqb.consumer.manage.front.contract;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.iqb.eatep.ec.contract.template.bean.ContractTemplateBean;
import com.iqb.eatep.ec.contract.template.bean.PaginationBean;
import com.iqb.eatep.ec.contract.template.service.ContractTemplateService;
import com.iqb.eatep.ec.contract.template.service.impl.ContractTemplateServiceImpl;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.common.utils.JSONUtil;
import com.iqb.etep.front.base.FrontBaseService;

/**
 * Description: 合同模板前置入口
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
@RequestMapping("/contractTemplate")
public class ContractTemplateController extends FrontBaseService {

    private static final Logger logger = LoggerFactory.getLogger(ContractTemplateController.class);

    @Autowired
    private ContractTemplateService contractTemplateServiceImpl;

    @ResponseBody
    @RequestMapping(value = "/paging", method = {RequestMethod.POST})
    public Object paging(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板分页开始...");
            PaginationBean contractTemplateBean = JSONUtil.toJavaObject(objs, PaginationBean.class);
            PageInfo<ContractTemplateBean> info =
                    this.contractTemplateServiceImpl.selectToListOfBean(contractTemplateBean);
            logger.info("合同模板分页结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", info);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板分页错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板分页错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Object insert(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板开始添加...");
            ContractTemplateBean contractTemplateBean = JSONUtil.toJavaObject(objs, ContractTemplateBean.class);
            this.contractTemplateServiceImpl.insert(ContractTemplateServiceImpl.setEcTplContentDataBlob(
                    contractTemplateBean, null));
            logger.info("合同模板添加结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板添加错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板添加错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/insertWithFile", method = {RequestMethod.POST})
    public Object insertWithFile(ContractTemplateBean contractTemplateBean,
            @RequestParam("file") CommonsMultipartFile file, HttpServletRequest request) {
        try {
            logger.info("合同模板开始添加...");
            this.contractTemplateServiceImpl.insert(ContractTemplateServiceImpl.setEcTplContentDataBlob(
                    contractTemplateBean, file));
            logger.info("合同模板添加结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板添加错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板添加错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getById", method = {RequestMethod.POST})
    public Object getById(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板主键查询开始...");
            Integer id = null;
            if (objs.containsKey("id")) {
                id = objs.getInteger("id");
            }
            ContractTemplateBean contractTemplateBean = this.contractTemplateServiceImpl.selectByPrimaryKey(id);
            logger.info("合同模板开始主键查询结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", contractTemplateBean);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板开始主键查询错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板开始主键查询错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public Object update(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板修改开始...");
            ContractTemplateBean contractTemplateBean = JSONUtil.toJavaObject(objs, ContractTemplateBean.class);
            this.contractTemplateServiceImpl.updateByPrimaryKey(contractTemplateBean);
            logger.info("合同模板修改结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板修改错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板修改错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/updateWithFile", method = {RequestMethod.POST})
    public Object update(ContractTemplateBean contractTemplateBean,
            @RequestParam(value = "file", required = false) CommonsMultipartFile file, HttpServletRequest request) {
        try {
            logger.info("合同模板修改开始...");
            this.contractTemplateServiceImpl.updateByPrimaryKey((ContractTemplateServiceImpl.setEcTplContentDataBlob(
                    contractTemplateBean, file)));
            logger.info("合同模板修改开始.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板修改错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板修改错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    @RequestMapping(value = "/downloadFile/{id}", method = {RequestMethod.POST})
    public void downloadFile(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("合同模板下载开始...");
            ContractTemplateBean bean = this.contractTemplateServiceImpl.selectByPrimaryKey(id);
            byte[] ecTplContentDataBlob = bean.getEcTplContentDataBlob();
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + sdf.format(now) + ".doc");
            ServletOutputStream out = response.getOutputStream();
            out.write(ecTplContentDataBlob);
            out.flush();
            logger.info("合同模板下载结束.");
        } catch (IqbException iqbe) {
            logger.error("合同模板下载错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
        } catch (Exception e) {
            logger.error("合同模板下载错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
        }
    }

    @RequestMapping(value = "/previewFile/{id}", method = {RequestMethod.POST})
    public void previewFile(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.info("合同模板预览开始...");
            this.contractTemplateServiceImpl.selectByPrimaryKey(id);
            // 设置编码
            response.setContentType("text/html;charset=utf-8");
            request.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.println("");
            out.close();
            logger.info("合同模板预览结束.");
        } catch (IqbException iqbe) {
            logger.error("合同模板预览错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
        } catch (Exception e) {
            logger.error("合同模板预览错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/getEcType", method = {RequestMethod.POST})
    public Object getEcType(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.info("合同模板开始查询合同模板类型...");
            List<Map<String, Object>> list = this.contractTemplateServiceImpl.selectEcTypeToListOfMap(objs);
            logger.info("合同模板查询合同模板类型结束.");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", list);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("合同模板查询合同模板类型错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("合同模板查询合同模板类型错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }
}
