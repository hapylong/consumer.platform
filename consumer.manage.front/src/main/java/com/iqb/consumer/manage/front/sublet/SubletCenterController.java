package com.iqb.consumer.manage.front.sublet;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.common.constant.GroupCode;
import com.iqb.consumer.common.constant.ServiceCode;
import com.iqb.consumer.common.exception.GenerallyException;
import com.iqb.consumer.data.layer.bean.sublet.db.entity.InstSubletRecordEntity;
import com.iqb.consumer.data.layer.bean.sublet.pojo.GetSubletRecordPojo;
import com.iqb.consumer.data.layer.bean.sublet.pojo.SubletInfoByOidPojo;
import com.iqb.consumer.manage.front.BasicService;
import com.iqb.consumer.service.layer.sublet.SubletCenterService;

/**
 * Description: 转租
 * 
 * @author adam
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date         Author      Version     Description 
------------------------------------------------------------------
 * 2017年5月8日    adam       1.0        1.0 Version 
 * </pre>
 */
@Controller
@RequestMapping("/sublet")
public class SubletCenterController extends BasicService {
    protected static final Logger logger = LoggerFactory.getLogger(SubletCenterController.class);
    private static final int SERVICE_CODE_GET_DIVISION_ASSETS_DETAILS = 1;
    private static final int SERVICE_CODE_PERSIST_SUBLET_INFO = 2;
    private static final int SERVICE_CODE_UNINTCPT_GET_SUBLET_RECORD = 3;
    private static final int SERVICE_CODE_GET_SUBLET_RECORD = 4;
    @Autowired
    private SubletCenterService subletCenterServiceImpl;

    /**
     * 
     * Description: 4.1通过订单号查询信息(中阁)
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月8日 上午11:05:07
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/get_sublet_info"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_DIVISION_ASSETS_DETAILS)
    public Object getSubletInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            SubletInfoByOidPojo result = subletCenterServiceImpl
                    .getSubletInfoByOid(requestMessage);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put(KEY_RESULT, result);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_DIVISION_ASSETS_DETAILS);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_DIVISION_ASSETS_DETAILS);
        }
    }

    /**
     * Description: 4.2保存转租记录
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月8日 下午6:13:53
     */

    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/persist_sublet_entity"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_PERSIST_SUBLET_INFO)
    public Object persistSubletInfo(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            boolean result = subletCenterServiceImpl
                    .persistSubletInfo(requestMessage);
            if (result) {
                linkedHashMap.put(KEY_RESULT, StatusEnum.success);
            } else {
                linkedHashMap.put(KEY_RESULT, StatusEnum.fail);
            }
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_SUBLET_INFO);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_PERSIST_SUBLET_INFO);
        }
    }

    /**
     * 
     * Description: 4.3查询转租记录 账务系统 使用
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月8日 下午6:14:05
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/unIntcpt-get_sublet_record"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_UNINTCPT_GET_SUBLET_RECORD)
    public Object getSubletEntityChat(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            InstSubletRecordEntity isre = subletCenterServiceImpl
                    .getSubletEntity(requestMessage);
            linkedHashMap.put(KEY_RESULT, isre);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UNINTCPT_GET_SUBLET_RECORD);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_UNINTCPT_GET_SUBLET_RECORD);
        }
    }

    /**
     * 
     * Description: 4.3查询转租记录 中阁使用
     * 
     * @param
     * @return Object
     * @throws
     * @Author adam Create Date: 2017年5月8日 下午6:14:05
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @ResponseBody
    @RequestMapping(value = {"/get_sublet_record"}, method = {RequestMethod.POST})
    @ServiceCode(SERVICE_CODE_GET_SUBLET_RECORD)
    public Object getSubletEntity(
            @RequestBody JSONObject requestMessage, HttpServletRequest request) {
        try {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            GetSubletRecordPojo isre = subletCenterServiceImpl
                    .getSubletRecord(requestMessage);
            linkedHashMap.put(KEY_RESULT, isre);
            return returnSuccessInfo(linkedHashMap);
        } catch (GenerallyException e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_SUBLET_RECORD);
        } catch (Throwable e) {
            return generateFailResponseMessage(e, SERVICE_CODE_GET_SUBLET_RECORD);
        }
    }

    @Override
    public int getGroupCode() {
        return GroupCode.GROUP_CODE_SUBLET_CENTER;
    }
}
