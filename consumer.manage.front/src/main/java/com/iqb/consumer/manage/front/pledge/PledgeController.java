package com.iqb.consumer.manage.front.pledge;

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
import com.github.pagehelper.PageInfo;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.Pledge.pojo.ConditionsGetCarInfoResponsePojo;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.pledge.IPledgeSerivce;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.exception.IqbException;

/**
 * 
 * Description: 抵押车服务
 * 
 * @author wangxinbang
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年3月29日    wangxinbang       1.0        1.0 Version 
 * </pre>
 */
@RestController
@RequestMapping("/pledge")
public class PledgeController extends BasicService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(PledgeController.class);

    @Autowired
    private IPledgeSerivce pledgeSerivceImpl;

    private static final int SERVICE_CODE_C_GET_CAR_INFO = 1;
    private static final int SERVICE_CODE_PERSIST_CAR_INFO = 2;
    private static final int SERVICE_CODE_UPDATE_CAR_INFO = 3;
    private static final int SERVICE_CODE_UPDATE_AMT = 4;

    /**
     * 
     * Description: 获取抵押车订单信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月29日 下午2:56:40
     */
    @ResponseBody
    @RequestMapping(value = "/getPledgeInfo", method = RequestMethod.POST)
    public Object getPledgeInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始获取抵押车订单信息" + objs.toJSONString());
            Object retObjs = this.pledgeSerivceImpl.getPledgeInfo(objs);
            logger.info("IQB信息---获取抵押车订单信息结束.");

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
     * Description: 保存车辆入库信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author wangxinbang Create Date: 2017年3月30日 下午6:32:06
     */
    @ResponseBody
    @RequestMapping(value = "/saveVehicleStorageInfo", method = RequestMethod.POST)
    public Object saveVehicleStorageInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始保存车辆入库信息信息" + objs.toJSONString());
            this.pledgeSerivceImpl.saveVehicleStorageInfo(objs);
            logger.info("IQB信息---保存车辆入库信息结束.");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (IqbException iqbe) {
            logger.error("IQB错误信息：" + iqbe.getRetInfo().getRetFactInfo(), iqbe);
            return super.returnFailtrueInfo(iqbe);
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 保存车辆是否有贷款
     * 
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/savePledgeInfo", method = RequestMethod.POST)
    public Object savePledgeInfo(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---开始保存车辆入库信息信息" + objs.toJSONString());
            this.pledgeSerivceImpl.savePledgeInfo(objs);
            logger.info("IQB信息---保存车辆入库信息结束.");
            return super.returnSuccessInfo(CommonReturnInfo.BASE00000000, new String[] {});
        } catch (Exception e) {
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * Description: 4.2.1 中阁管理后台 评估车辆查询(分页)
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月22日 上午11:06:12
     */
    @ResponseBody
    @RequestMapping(value = "/cget_car_info", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_C_GET_CAR_INFO)
    public Object cgetCarInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            PageInfo<ConditionsGetCarInfoResponsePojo> result = pledgeSerivceImpl.cgetCarInfo(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_C_GET_CAR_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_C_GET_CAR_INFO);
        }
    }

    /**
     * 
     * Description: 4.2.2保存车辆评估信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月22日 下午4:02:51
     */
    @ResponseBody
    @RequestMapping(value = "/persist_car_info", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_PERSIST_CAR_INFO)
    public Object persistCarInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            pledgeSerivceImpl.persistCarInfo(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_CAR_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_CAR_INFO);
        }
    }

    /**
     * 
     * Description: 4.2.3修改信息
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月23日 上午10:10:54
     */
    @ResponseBody
    @RequestMapping(value = "/update_car_info", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_UPDATE_CAR_INFO)
    public Object updateCarInfo(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            pledgeSerivceImpl.updateCarInfo(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CAR_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_CAR_INFO);
        }
    }

    /**
     * 
     * Description: 4.2.7保存车辆评估价格
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月23日 上午10:13:50
     */
    @ResponseBody
    @RequestMapping(value = "/update_amt", method = RequestMethod.POST)
    @ServiceCode(SERVICE_CODE_UPDATE_AMT)
    public Object updateAmt(@RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            pledgeSerivceImpl.updateAmt(requestMessage);
            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_AMT);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UPDATE_AMT);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_PLEDGE_CENTER;
    }

}
