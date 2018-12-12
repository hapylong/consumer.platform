/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description:
 * @date 2016年9月7日 下午2:45:16
 * @version V1.0
 */
package com.iqb.consumer.manage.front.authoritycard;

import java.util.LinkedHashMap;
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

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.authoritycard.AuthorityCardBean;
import com.iqb.consumer.service.layer.authoritycard.AuthorityCardService;
import com.iqb.etep.common.base.CommonReturnInfo;
import com.iqb.etep.common.base.service.BaseService;
import com.iqb.etep.common.exception.IqbException;

/**
 * 权证资料处理类
 * 
 * @author <a href="gongxiaoyu@iqianbang.com">gxy</a>
 */
@Controller
@RequestMapping("/authoritycard")
public class AuthorityCardController extends BaseService {

    protected static final Logger logger = LoggerFactory.getLogger(AuthorityCardController.class);

    public final static String FES_RET_SUCC = "000000"; // 处理成功
    public final static String FES_RET_FAIL = "000001"; // 处理失败
    public final static String FES_TRAN_FAIL = "000002"; // 数据转换错误

    @Resource
    private AuthorityCardService authorityCardService;

    /*
     * Description: 保存权证资料信息
     */
    /**
     * 
     * Description: 如果已经存在改订单的记录则是修改。(该功能是用来节点回退使用的) 1.字段调整 GPS安装时间---GPS安装完成时间 字段保持不变
     * 增加车牌号/有线GPS安装位置/无线GPS安装位置 plate/lineGpsInstAdd/nolineGpsInstAdd 3.去掉有线GPS识别号/无线GPS识别号/是否交车
     * 这三个字段前端不在传后台，不需要做验证。 5.有线GPS识别号/无线GPS识别号位数限制去掉
     * 
     * @param
     * @return Map
     * @throws
     * @Author adam [改] Create Date: 2017年6月5日 上午10:42:26
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public Map save(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---【控制层】保存权证资料信息，开始...");
            int result = 1;
            AuthorityCardBean authorityCardBean = authorityCardService.selectOneByOrderId(objs);
            if (null == authorityCardBean) {
                logger.debug("IQB信息---【控制层】保存权证资料信息，无记录，插入新数据...");
                result = authorityCardService.insertAuthorityCard(objs);
            } else {
                logger.debug("IQB信息---【控制层】保存权证资料信息，有记录，更新原有数据...");
                result = authorityCardService.updateAuthorityCard(objs);
            }
            logger.debug("IQB信息---【控制层】保存权证资料信息，结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap = getResult(result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * Description: 查询单条权证资料信息
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/querryByOrderId", method = {RequestMethod.POST, RequestMethod.GET})
    public Map querryByOrderId(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---【控制层】查询单条权证资料信息，开始...");
            AuthorityCardBean authorityCardBean = authorityCardService.selectOneByOrderId(objs);
            logger.debug("IQB信息---【控制层】查询单条权证资料信息，结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            if (null == authorityCardBean) {
                linkedHashMap = getResult(10);
                linkedHashMap.put("AuthorityCardInfo", null);
            } else {
                linkedHashMap = getResult(11);
                linkedHashMap.put("AuthorityCardInfo", authorityCardBean);
            }
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /**
     * 
     * 更新车辆权证信息表颜色、里程
     * 
     * @author haojinlong
     * @param objs
     * @param request
     * @return 2018年7月6日
     */
    @SuppressWarnings("rawtypes")
    @ResponseBody
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    public Map update(@RequestBody JSONObject objs, HttpServletRequest request) {
        try {
            logger.debug("IQB信息---【控制层】保存权证资料信息，开始...");
            int result = authorityCardService.updateAuthorityCardInfo(objs);
            logger.debug("IQB信息---【控制层】保存权证资料信息，结束...");

            LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
            linkedHashMap = getResult(result);
            return super.returnSuccessInfo(linkedHashMap);
        } catch (Exception e) {// 系统发生异常
            logger.error("IQB错误信息：" + CommonReturnInfo.BASE00000001.getRetFactInfo(), e);
            return super.returnFailtrueInfo(new IqbException(CommonReturnInfo.BASE00000001));
        }
    }

    /*
     * 处理结果集
     */
    private LinkedHashMap<String, Object> getResult(int result) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        switch (result) {
            case 0:
                linkedHashMap.put("result", FES_RET_FAIL);
                linkedHashMap.put("msg", "保存权证资料信息失败");
                break;
            case 1:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "保存权证资料信息成功");
                break;
            case 2:
                linkedHashMap.put("result", FES_TRAN_FAIL);
                linkedHashMap.put("msg", "数据转换错误");
                break;
            case 10:
                linkedHashMap.put("result", FES_TRAN_FAIL);
                linkedHashMap.put("msg", "查询权证资料信息失败");
                break;
            case 11:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "查询权证资料信息成功");
                break;
            default:
                linkedHashMap.put("result", FES_RET_SUCC);
                linkedHashMap.put("msg", "处理成功");
                break;
        }
        return linkedHashMap;
    }
}
