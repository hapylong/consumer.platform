package com.iqb.consumer.manage.front.contract;

import java.io.IOException;
import java.util.Enumeration;
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
import com.iqb.eatep.ec.asyn.AsynSubmitEcService;
import com.iqb.eatep.ec.contract.ecinfo.service.IEcInfoService;
import com.iqb.eatep.ec.contract.entry.service.IThirdEcEntry;
import com.iqb.eatep.ec.contract.entry.service.IThirdEcService;
import com.iqb.eatep.ec.contract.ssq.dosign.service.IDoSignService;
import com.iqb.eatep.ec.contract.ssq.template.service.IEcTemplateService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;
import com.iqb.etep.front.base.FrontBaseService;

/**
 * s Description:
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年2月21日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/unIntcpt-ecTemplateApi")
public class EcTemplateApiController extends FrontBaseService {

    /** 日志 **/
    protected static final Logger logger = LoggerFactory.getLogger(EcTemplateApiController.class);

    @Autowired
    private IThirdEcEntry thirdEcEntryImpl;

    @Autowired
    private IEcTemplateService ecTemplateServiceImpl;

    @Autowired
    private IDoSignService doSignServiceImpl;

    @Autowired
    private IEcInfoService ecInfoServiceImpl;

    @Autowired
    private AsynSubmitEcService asynSubmitEcService;

    /**
     * 
     * Description: 生成电子合同
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年2月21日 下午4:16:55
     */
    @ResponseBody
    @RequestMapping(value = "/generateEc", method = {RequestMethod.POST})
    public Object generateEc(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("生成电子合同开始，传入信息:{}", JSONObject.toJSONString(objs));
            Object obj = ecTemplateServiceImpl.generateEc(objs);
            logger.info("生成电子合同结束");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", obj);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("生成电子合同错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("生成电子合同错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 查询电子合同
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年2月22日 下午5:10:08
     */
    @ResponseBody
    @RequestMapping(value = "/submitEc", method = {RequestMethod.POST})
    public Object submitEc(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("查询电子合同开始，传入信息:{}", JSONObject.toJSONString(objs));
            Object obj = this.asynSubmitEcService.submitEc(objs);
            logger.info("查询电子合同结束");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", obj);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("查询电子合同错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("查询电子合同错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 查询电子合同
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年2月22日 下午5:10:08
     */
    @ResponseBody
    @RequestMapping(value = "/selectContractInfo", method = {RequestMethod.POST})
    public Object selectContractInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("提交电子合同开始，传入信息:{}", JSONObject.toJSONString(objs));
            Object obj = this.ecInfoServiceImpl.selectContractInfo(objs);
            logger.info("提交电子合同结束");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", obj);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("提交电子合同错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("提交电子合同错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 生成电子合同
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年2月22日 下午5:10:08
     */
    @ResponseBody
    @RequestMapping(value = "/generateContract", method = {RequestMethod.POST})
    public Object generateContract(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("生成电子合同开始，传入信息:{}", JSONObject.toJSONString(objs));
            IThirdEcService thirdEcService = this.thirdEcEntryImpl.getThirdEcService();
            Object obj = thirdEcService.dealSsqManualSignNotify(objs);
            logger.info("生成电子合同结束");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", obj);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("生成电子合同错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("生成电子合同错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 上上签手动签署通知接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年2月23日 上午10:30:54
     */
    @ResponseBody
    @RequestMapping(value = "/ssqManualSignNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public Object ssqManualSignNotify(HttpServletRequest request) {
        try {
            int i = 0;
            JSONObject objs = new JSONObject();
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                logger.info("上上签通知接口返回数据方式为form");
                i = 1;
                String paraName = enu.nextElement();
                objs.put(paraName, request.getParameter(paraName));
            }
            if (i == 0) {
                logger.info("上上签通知接口返回数据方式为json");
                objs = this.getRequestPostStr(request);
            }
            logger.info("上上签手动签署通知接口，传入信息:{}", JSONObject.toJSONString(objs));
            IThirdEcService thirdEcService = this.thirdEcEntryImpl.getThirdEcService();
            Object obj = thirdEcService.dealSsqManualSignNotify(objs);
            logger.info("上上签手动签署通知接口处理完成");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", obj);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("上上签手动签署通知接口错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("上上签手动签署通知接口错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 描述:获取 post 请求的 byte[] 数组
     * 
     * <pre>
     * 举例：
     * </pre>
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public byte[] getRequestPostBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 描述:获取 post 请求内容
     * 
     * <pre>
     * 举例：
     * </pre>
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public JSONObject getRequestPostStr(HttpServletRequest request) throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        String str = new String(buffer, charEncoding);
        return JSONObject.parseObject(str);
    }

    /**
     * 
     * Description: 上上签手动签署同步返回接口
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年2月23日 下午1:31:53
     */
    @ResponseBody
    @RequestMapping(value = "/ssqManualSignReturnUrl", method = {RequestMethod.POST, RequestMethod.GET})
    public Object ssqManualSignReturnUrl(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("上上签手动签署同步返回接口，传入信息:{}", JSONObject.toJSONString(objs));
            IThirdEcService thirdEcService = this.thirdEcEntryImpl.getThirdEcService();
            Object obj = thirdEcService.dealSsqManualSignReturnUrl(objs);
            logger.info("上上签手动签署同步返回接口处理完成");
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put("result", obj);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (IqbException iqbe) {
            logger.error("上上签手动签署同步返回接口错误信息:" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("上上签手动签署同步返回接口错误信息:" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

}
