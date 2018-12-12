package com.iqb.consumer.manage.front.ec;

import java.util.Map;

import javax.annotation.Resource;
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
import com.iqb.eatep.ec.contract.bizretbean.SelectContractInfoRetBean;
import com.iqb.eatep.ec.contract.ecinfo.service.IEcInfoService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;

@Controller
@SuppressWarnings({"rawtypes"})
@RequestMapping("/ec")
public class eContractController extends BaseService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(eContractController.class);

    @Resource
    private IEcInfoService ecInfoServiceImpl;

    /**
     * 查询用户电子合同接口
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-selectContractInfo"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String selectContractInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("电子合同查询，入参：{}", objs);
            SelectContractInfoRetBean contractInfo = ecInfoServiceImpl.selectContractInfo((Map) objs);
            logger.debug("电子合同查询完成.");
            return JSON.toJSONString(contractInfo);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return null;
        }
    }

}
